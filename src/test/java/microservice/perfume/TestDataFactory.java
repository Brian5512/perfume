package microservice.perfume;

import microservice.perfume.Dto.AjusteInventarioRequest;
import microservice.perfume.Dto.InventarioRequest;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.AlertaStock;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.MovimientoInventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Model.UsuarioInventario;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static Producto producto(Long id) {
        Producto producto = new Producto();
        producto.setIdProducto(id);
        producto.setNombre("Perfume Citrus");
        producto.setDescripcion("Fragancia fresca");
        producto.setPrecio(19990.0);
        producto.setEstado("ACTIVO");
        return producto;
    }

    public static Sucursal sucursal(Long id) {
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(id);
        sucursal.setNombre("Sucursal Centro");
        sucursal.setDireccion("Av. Principal 123");
        sucursal.setEstado("ACTIVA");
        return sucursal;
    }

    public static UsuarioInventario usuario(Long id) {
        UsuarioInventario usuario = new UsuarioInventario();
        usuario.setIdUsuario(id);
        usuario.setNombre("Encargado Inventario");
        usuario.setRol("ADMIN");
        usuario.setEstado("ACTIVO");
        return usuario;
    }

    public static Inventario inventario(Long id, int stockActual, int stockMinimo, int stockMaximo) {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(id);
        inventario.setProducto(producto(1L));
        inventario.setSucursal(sucursal(1L));
        inventario.setStockActual(stockActual);
        inventario.setStockMinimo(stockMinimo);
        inventario.setStockMaximo(stockMaximo);
        inventario.setUbicacion("Bodega A");
        return inventario;
    }

    public static InventarioRequest inventarioRequest(int stockActual, int stockMinimo, int stockMaximo) {
        InventarioRequest request = new InventarioRequest();
        request.setProductoId(1L);
        request.setSucursalId(1L);
        request.setStockActual(stockActual);
        request.setStockMinimo(stockMinimo);
        request.setStockMaximo(stockMaximo);
        request.setUbicacion("Bodega A");
        return request;
    }

    public static AjusteInventarioRequest ajusteRequest(String tipoMovimiento, int cantidad) {
        AjusteInventarioRequest request = new AjusteInventarioRequest();
        request.setUsuarioId(1L);
        request.setTipoMovimiento(tipoMovimiento);
        request.setCantidad(cantidad);
        request.setMotivo("Venta");
        return request;
    }

    public static MovimientoInventarioRequest movimientoRequest(String tipoMovimiento, int cantidad) {
        MovimientoInventarioRequest request = new MovimientoInventarioRequest();
        request.setInventarioId(1L);
        request.setUsuarioId(1L);
        request.setTipoMovimiento(tipoMovimiento);
        request.setCantidad(cantidad);
        request.setMotivo("Venta");
        return request;
    }

    public static MovimientoInventario movimiento(Long id) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdMovimiento(id);
        movimiento.setInventario(inventario(1L, 10, 5, 20));
        movimiento.setUsuario(usuario(1L));
        movimiento.setTipoMovimiento("SALIDA");
        movimiento.setCantidad(2);
        movimiento.setMotivo("Venta");
        return movimiento;
    }

    public static AlertaStock alerta(Long id) {
        AlertaStock alerta = new AlertaStock();
        alerta.setIdAlerta(id);
        alerta.setInventario(inventario(1L, 3, 5, 20));
        alerta.setMensaje("Stock bajo");
        alerta.setNivelPrioridad("ALTA");
        alerta.setEstado("ACTIVA");
        return alerta;
    }
}
