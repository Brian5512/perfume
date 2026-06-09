package microservice.perfume.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.MovimientoInventario;
import microservice.perfume.Model.UsuarioInventario;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.MovimientoInventarioRepository;
import microservice.perfume.Repository.UsuarioInventarioRepository;

@Service
@Transactional
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final InventarioRepository inventarioRepository;
    private final UsuarioInventarioRepository usuarioInventarioRepository;
    private final InventarioService inventarioService;

    public MovimientoInventarioService(
            MovimientoInventarioRepository movimientoInventarioRepository,
            InventarioRepository inventarioRepository,
            UsuarioInventarioRepository usuarioInventarioRepository,
            @Lazy InventarioService inventarioService) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.inventarioRepository = inventarioRepository;
        this.usuarioInventarioRepository = usuarioInventarioRepository;
        this.inventarioService = inventarioService;
    }

    public MovimientoInventario registrarMovimiento(MovimientoInventarioRequest request) {
        return registrarMovimiento(request, true);
    }

    public MovimientoInventario registrarMovimiento(MovimientoInventarioRequest request, boolean ajustarStock) {
        Inventario inventario = buscarInventario(request.getInventarioId());

        if (ajustarStock) {
            int stockNuevo = calcularStock(inventario.getStockActual(), request.getTipoMovimiento(), request.getCantidad());
            if (stockNuevo > inventario.getStockMaximo()) {
                throw new IllegalArgumentException("El stock no puede superar el stock maximo configurado");
            }
            inventario.setStockActual(stockNuevo);
            inventarioRepository.save(inventario);
        }

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setInventario(inventario);
        movimiento.setUsuario(buscarUsuarioOpcional(request.getUsuarioId()));
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setCantidad(request.getCantidad());
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setMotivo(request.getMotivo());
        MovimientoInventario guardado = movimientoInventarioRepository.save(movimiento);

        if (ajustarStock) {
            inventarioService.verificarStockBajo(request.getInventarioId());
        }

        return guardado;
    }

    public List<MovimientoInventario> consultarHistorial() {
        return movimientoInventarioRepository.findAll();
    }

    public List<MovimientoInventario> consultarHistorialInventario(Long idInventario) {
        buscarInventario(idInventario);
        return movimientoInventarioRepository.findByInventarioIdInventarioOrderByFechaMovimientoDesc(idInventario);
    }

    private Inventario buscarInventario(Long idInventario) {
        return inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id " + idInventario));
    }

    private UsuarioInventario buscarUsuarioOpcional(Long idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        return usuarioInventarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario de inventario no encontrado con id " + idUsuario));
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
