package microservice.perfume.Service;

import static microservice.perfume.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.perfume.Dto.AjusteInventarioRequest;
import microservice.perfume.Dto.InventarioRequest;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.ProductoRepository;
import microservice.perfume.Repository.SucursalRepository;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private MovimientoInventarioService movimientoInventarioService;

    @Mock
    private AlertaStockService alertaStockService;

    @InjectMocks
    private InventarioService inventarioService;

    private Producto producto;
    private Sucursal sucursal;

    @BeforeEach
    void setup() {
        producto = producto(1L);
        sucursal = sucursal(1L);
    }

    // ==============================
    // CREAR INVENTARIO
    // ==============================

    @Test
    void crearInventario_ok() {
        InventarioRequest request = inventarioRequest(10, 5, 20);

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursal));

        when(inventarioRepository.save(any(Inventario.class)))
                .thenReturn(inventario(1L, 10, 5, 20));

        // 🔥 FIX IMPORTANTE por llamada interna a verificarStockBajo()
        when(inventarioRepository.findById(anyLong()))
                .thenReturn(Optional.of(inventario(1L, 10, 5, 20)));

        Inventario resultado = inventarioService.crearInventario(request);

        assertEquals(10, resultado.getStockActual());
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void crearInventario_stockMinimoMayorMaximo_lanzaExcepcion() {
        InventarioRequest request = inventarioRequest(10, 30, 20);

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursal));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.crearInventario(request));
    }

    // ==============================
    // LISTAR INVENTARIOS
    // ==============================

    @Test
    void listarInventarios_ok() {
        when(inventarioRepository.findAll())
                .thenReturn(List.of(inventario(1L, 10, 5, 20)));

        List<Inventario> lista = inventarioService.listarInventarios();

        assertEquals(1, lista.size());
    }

    // ==============================
    // OBTENER INVENTARIO
    // ==============================

    @Test
    void obtenerInventario_ok() {
        Inventario inv = inventario(1L, 10, 5, 20);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        Inventario resultado = inventarioService.obtenerInventario(1L);

        assertEquals(1L, resultado.getIdInventario());
    }

    @Test
    void obtenerInventario_noExiste_lanzaExcepcion() {
        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> inventarioService.obtenerInventario(1L));
    }

    // ==============================
    // ACTUALIZAR INVENTARIO
    // ==============================

    @Test
    void actualizarInventario_ok() {
        Inventario existente = inventario(1L, 10, 5, 20);
        InventarioRequest request = inventarioRequest(15, 5, 25);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(sucursalRepository.findById(1L))
                .thenReturn(Optional.of(sucursal));

        when(inventarioRepository.save(any(Inventario.class)))
                .thenReturn(existente);

        when(inventarioRepository.findById(anyLong()))
                .thenReturn(Optional.of(existente));

        Inventario resultado = inventarioService.actualizarInventario(1L, request);

        assertEquals(15, resultado.getStockActual());
        verify(inventarioRepository).save(any(Inventario.class));
    }

    // ==============================
    // ELIMINAR INVENTARIO
    // ==============================

    @Test
    void eliminarInventario_ok() {
        Inventario inv = inventario(1L, 10, 5, 20);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        inventarioService.eliminarInventario(1L);

        verify(inventarioRepository).delete(inv);
    }

    // ==============================
    // AJUSTAR INVENTARIO
    // ==============================

    @Test
    void ajustarInventario_entrada_ok() {
        Inventario inv = inventario(1L, 10, 5, 20);
        AjusteInventarioRequest request = ajusteRequest("ENTRADA", 5);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        when(inventarioRepository.save(any(Inventario.class)))
                .thenReturn(inv);

        when(inventarioRepository.findById(anyLong()))
                .thenReturn(Optional.of(inv));

        Inventario resultado = inventarioService.ajustarInventario(1L, request);

        assertEquals(15, resultado.getStockActual());
        verify(movimientoInventarioService)
                .registrarMovimiento(any(), eq(false));
    }

    @Test
    void ajustarInventario_salida_sinStock_lanzaExcepcion() {
        Inventario inv = inventario(1L, 2, 1, 20);
        AjusteInventarioRequest request = ajusteRequest("SALIDA", 5);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.ajustarInventario(1L, request));
    }

    @Test
    void ajustarInventario_tipoInvalido_lanzaExcepcion() {
        Inventario inv = inventario(1L, 10, 5, 20);
        AjusteInventarioRequest request = ajusteRequest("INVALIDO", 5);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.ajustarInventario(1L, request));
    }

    // ==============================
    // VERIFICAR STOCK BAJO
    // ==============================

    @Test
    void verificarStockBajo_true() {
        Inventario inv = inventario(1L, 3, 5, 20);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        boolean resultado = inventarioService.verificarStockBajo(1L);

        assertTrue(resultado);
        verify(alertaStockService).generarAlerta(inv);
    }

    @Test
    void verificarStockBajo_false() {
        Inventario inv = inventario(1L, 10, 5, 20);

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(inv));

        boolean resultado = inventarioService.verificarStockBajo(1L);

        assertFalse(resultado);
        verify(alertaStockService, never()).generarAlerta(any());
    }
}