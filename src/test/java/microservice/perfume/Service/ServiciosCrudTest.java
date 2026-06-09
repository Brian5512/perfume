package microservice.perfume.Service;

import static microservice.perfume.TestDataFactory.ajusteRequest;
import static microservice.perfume.TestDataFactory.alerta;
import static microservice.perfume.TestDataFactory.inventario;
import static microservice.perfume.TestDataFactory.producto;
import static microservice.perfume.TestDataFactory.sucursal;
import static microservice.perfume.TestDataFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import microservice.perfume.Model.AlertaStock;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Model.UsuarioInventario;
import microservice.perfume.Repository.AlertaStockRepository;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.ProductoRepository;
import microservice.perfume.Repository.SucursalRepository;
import microservice.perfume.Repository.UsuarioInventarioRepository;

@ExtendWith(MockitoExtension.class)
class ServiciosCrudTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private UsuarioInventarioRepository usuarioInventarioRepository;

    @Mock
    private AlertaStockRepository alertaStockRepository;

    @Mock
    private ProductoService productoServiceMock;

    @Mock
    private InventarioService inventarioServiceMock;

    @InjectMocks
    private ProductoService productoService;

    @InjectMocks
    private SucursalService sucursalService;

    @InjectMocks
    private AlertaStockService alertaStockService;

    @Test
    void productoServiceCreaActualizaConsultaDisponibilidadYElimina() {
        Producto original = producto(1L);
        Producto cambios = producto(2L);
        cambios.setNombre("Perfume Floral");
        cambios.setPrecio(29990.0);

        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(original));
        when(productoRepository.findAll()).thenReturn(List.of(original));
        when(inventarioRepository.consultarDisponibilidadProducto(1L)).thenReturn(12);

        assertEquals(original, productoService.agregarProducto(original));
        assertEquals(1, productoService.listarProductos().size());
        assertEquals("Perfume Floral", productoService.actualizarProducto(1L, cambios).getNombre());
        assertEquals(12, productoService.consultarDisponibilidad(1L).getStockDisponible());

        productoService.eliminarProducto(1L);
        verify(productoRepository).delete(original);
    }

    @Test
    void productoServiceLanzaSiNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productoService.obtenerProducto(99L));
    }

    @Test
    void sucursalServiceCreaActualizaConsultaInventarioYElimina() {
        Sucursal original = sucursal(1L);
        Sucursal cambios = sucursal(2L);
        cambios.setNombre("Sucursal Norte");
        Inventario inventario = inventario(1L, 7, 5, 20);

        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(original));
        when(sucursalRepository.findAll()).thenReturn(List.of(original));
        when(inventarioRepository.findBySucursalIdSucursal(1L)).thenReturn(List.of(inventario));

        assertEquals(original, sucursalService.asignarSucursal(original));
        assertEquals(1, sucursalService.listarSucursales().size());
        assertEquals("Sucursal Norte", sucursalService.actualizarSucursal(1L, cambios).getNombre());
        assertEquals(List.of(inventario), sucursalService.consultarInventario(1L));

        sucursalService.eliminarSucursal(1L);
        verify(sucursalRepository).delete(original);
    }

    @Test
    void alertaStockServiceReutilizaActivaOCreaNuevaYCierra() {
        Inventario inventario = inventario(1L, 0, 5, 20);
        AlertaStock existente = alerta(1L);
        AlertaStock nueva = alerta(2L);
        nueva.setEstado(null);

        when(alertaStockRepository.findByInventarioIdInventarioAndEstado(1L, "ACTIVA"))
                .thenReturn(List.of(existente))
                .thenReturn(List.of());
        when(alertaStockRepository.save(any(AlertaStock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertaStockRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(alertaStockRepository.findAll()).thenReturn(List.of(existente));

        assertEquals(existente, alertaStockService.generarAlerta(inventario));
        assertEquals("CRITICA", alertaStockService.generarAlerta(inventario).getNivelPrioridad());
        assertEquals(List.of(existente), alertaStockService.listarAlertas());
        assertEquals(existente, alertaStockService.notificar(1L));
        assertEquals("RESUELTA", alertaStockService.cerrarAlerta(1L).getEstado());
    }

    @Test
    void alertaStockServiceLanzaSiNoExiste() {
        when(alertaStockRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> alertaStockService.notificar(99L));
    }

    @Test
    void usuarioInventarioServiceDelegaOperaciones() {
        UsuarioInventarioService usuarioService = new UsuarioInventarioService(
                usuarioInventarioRepository,
                productoServiceMock,
                inventarioServiceMock);
        UsuarioInventario usuario = usuario(1L);
        UsuarioInventario cambios = usuario(2L);
        cambios.setRol("OPERADOR");
        Producto producto = producto(1L);
        Inventario inventario = inventario(1L, 5, 5, 20);

        when(usuarioInventarioRepository.save(any(UsuarioInventario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioInventarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioInventarioRepository.findAll()).thenReturn(List.of(usuario));
        when(productoServiceMock.agregarProducto(producto)).thenReturn(producto);
        when(inventarioServiceMock.ajustarInventario(1L, ajusteRequest("AJUSTE", 5))).thenReturn(inventario);

        assertEquals(usuario, usuarioService.agregarUsuario(usuario));
        assertEquals(1, usuarioService.listarUsuarios().size());
        assertEquals("OPERADOR", usuarioService.actualizarUsuario(1L, cambios).getRol());
        assertEquals(producto, usuarioService.agregarProducto(producto));
        assertEquals(inventario, usuarioService.ajustarInventario(1L, ajusteRequest("AJUSTE", 5)));
        assertEquals(inventario, usuarioService.actualizarStock(1L, ajusteRequest("AJUSTE", 5)));

        usuarioService.eliminarUsuario(1L);
        verify(usuarioInventarioRepository).delete(usuario);
    }
}
