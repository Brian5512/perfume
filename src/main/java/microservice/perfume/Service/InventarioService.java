package microservice.perfume.Service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Dto.AjusteInventarioRequest;
import microservice.perfume.Dto.BodegaDescuentoRequest;
import microservice.perfume.Dto.DescuentoVentaRequest;
import microservice.perfume.Dto.InventarioRequest;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.ProductoRepository;
import microservice.perfume.Repository.SucursalRepository;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final MovimientoInventarioService movimientoInventarioService;
    private final AlertaStockService alertaStockService;
    private final BodegaClient bodegaClient;

    public InventarioService(
            InventarioRepository inventarioRepository,
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository,
            MovimientoInventarioService movimientoInventarioService,
            AlertaStockService alertaStockService,
            BodegaClient bodegaClient) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
        this.movimientoInventarioService = movimientoInventarioService;
        this.alertaStockService = alertaStockService;
        this.bodegaClient = bodegaClient;
    }

    public Inventario crearInventario(InventarioRequest request) {
        Inventario inventario = new Inventario();
        inventario.setProducto(buscarProducto(request.getProductoId()));
        inventario.setSucursal(buscarSucursal(request.getSucursalId()));
        inventario.setStockActual(request.getStockActual());
        inventario.setStockMinimo(request.getStockMinimo());
        inventario.setStockMaximo(request.getStockMaximo());
        inventario.setUbicacion(request.getUbicacion());
        validarRangosStock(inventario);

        Inventario guardado = inventarioRepository.save(inventario);
        verificarStockBajo(guardado.getIdInventario());
        return guardado;
    }

    public List<Inventario> listarInventarios() {
        return inventarioRepository.findAll();
    }

    public Inventario obtenerInventario(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id " + id));
    }

    public Inventario actualizarInventario(Long id, InventarioRequest request) {
        Inventario inventario = obtenerInventario(id);
        inventario.setProducto(buscarProducto(request.getProductoId()));
        inventario.setSucursal(buscarSucursal(request.getSucursalId()));
        inventario.setStockActual(request.getStockActual());
        inventario.setStockMinimo(request.getStockMinimo());
        inventario.setStockMaximo(request.getStockMaximo());
        inventario.setUbicacion(request.getUbicacion());
        validarRangosStock(inventario);

        Inventario guardado = inventarioRepository.save(inventario);
        verificarStockBajo(guardado.getIdInventario());
        return guardado;
    }

    public void eliminarInventario(Long id) {
        Inventario inventario = obtenerInventario(id);
        inventarioRepository.delete(inventario);
    }

    public Inventario ajustarInventario(Long idInventario, AjusteInventarioRequest request) {
        Inventario inventario = obtenerInventario(idInventario);
        int stockNuevo = calcularStock(inventario.getStockActual(), request.getTipoMovimiento(), request.getCantidad());

        if (stockNuevo > inventario.getStockMaximo()) {
            throw new IllegalArgumentException("El stock no puede superar el stock maximo configurado");
        }

        inventario.setStockActual(stockNuevo);
        Inventario guardado = inventarioRepository.save(inventario);

        MovimientoInventarioRequest movimiento = new MovimientoInventarioRequest();
        movimiento.setInventarioId(idInventario);
        movimiento.setUsuarioId(request.getUsuarioId());
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setCantidad(request.getCantidad());
        movimiento.setMotivo(request.getMotivo());
        movimientoInventarioService.registrarMovimiento(movimiento, false);

        verificarStockBajo(idInventario);
        return guardado;
    }

    public Inventario descontarVenta(DescuentoVentaRequest request) {
        Inventario inventario = buscarInventarioVenta(request);

        AjusteInventarioRequest ajuste = new AjusteInventarioRequest();
        ajuste.setUsuarioId(request.getUsuarioId());
        ajuste.setTipoMovimiento("SALIDA");
        ajuste.setCantidad(request.getCantidad());
        ajuste.setMotivo("Venta" + (request.getVentaId() == null ? "" : " #" + request.getVentaId()));

        Inventario guardado = ajustarInventario(inventario.getIdInventario(), ajuste);

        bodegaClient.descontarPorVenta(new BodegaDescuentoRequest(
                request.getProductoId(),
                request.getSucursalId(),
                request.getVentaId(),
                request.getCantidad(),
                ajuste.getMotivo()));

        return guardado;
    }

    public boolean verificarStockBajo(Long idInventario) {
        Inventario inventario = obtenerInventario(idInventario);
        if (inventario.getStockActual() <= inventario.getStockMinimo()) {
            alertaStockService.generarAlerta(inventario);
            return true;
        }
        return false;
    }

    private Producto buscarProducto(Long idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id " + idProducto));
    }

    private Sucursal buscarSucursal(Long idSucursal) {
        return sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new EntityNotFoundException("Sucursal no encontrada con id " + idSucursal));
    }

    private Inventario buscarInventarioVenta(DescuentoVentaRequest request) {
        if (request.getInventarioId() != null) {
            return obtenerInventario(request.getInventarioId());
        }
        return inventarioRepository.findByProductoIdProductoAndSucursalIdSucursal(request.getProductoId(), request.getSucursalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Inventario no encontrado para producto " + request.getProductoId()
                                + " y sucursal " + request.getSucursalId()));
    }

    private void validarRangosStock(Inventario inventario) {
        if (inventario.getStockMinimo() > inventario.getStockMaximo()) {
            throw new IllegalArgumentException("El stock minimo no puede ser mayor al stock maximo");
        }
        if (inventario.getStockActual() > inventario.getStockMaximo()) {
            throw new IllegalArgumentException("El stock actual no puede superar el stock maximo");
        }
    }

    private int calcularStock(int stockActual, String tipoMovimiento, int cantidad) {
        String tipo = tipoMovimiento.toUpperCase(Locale.ROOT);
        if ("ENTRADA".equals(tipo)) {
            return stockActual + cantidad;
        }
        if ("SALIDA".equals(tipo)) {
            int stockNuevo = stockActual - cantidad;
            if (stockNuevo < 0) {
                throw new IllegalArgumentException("No hay stock suficiente para registrar la salida");
            }
            return stockNuevo;
        }
        if ("AJUSTE".equals(tipo)) {
            return cantidad;
        }
        throw new IllegalArgumentException("Tipo de movimiento invalido. Use ENTRADA, SALIDA o AJUSTE");
    }
}
