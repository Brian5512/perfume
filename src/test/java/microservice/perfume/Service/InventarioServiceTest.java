package microservice.perfume.Service;

import static microservice.perfume.TestDataFactory.ajusteRequest;
import static microservice.perfume.TestDataFactory.inventario;
import static microservice.perfume.TestDataFactory.inventarioRequest;
import static microservice.perfume.TestDataFactory.producto;
import static microservice.perfume.TestDataFactory.sucursal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.Inventario;
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

    @Test
    void crearInventarioGuardaYGeneraAlertaSiStockEstaBajo() {
        Inventario guardado = inventario(1L, 3, 5, 20);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto(1L)));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal(1L)));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(guardado);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(guardado));

        Inventario resultado = inventarioService.crearInventario(inventarioRequest(3, 5, 20));

        assertEquals(1L, resultado.getIdInventario());
        verify(alertaStockService).generarAlerta(guardado);
    }

    @Test
    void crearInventarioRechazaStockMinimoMayorAlMaximo() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto(1L)));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.crearInventario(inventarioRequest(3, 10, 5)));

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void ajustarInventarioConSalidaDescuentaStockYRegistraMovimiento() {
        Inventario inventario = inventario(1L, 10, 5, 20);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.ajustarInventario(1L, ajusteRequest("SALIDA", 4));

        assertEquals(6, resultado.getStockActual());
        ArgumentCaptor<MovimientoInventarioRequest> captor = ArgumentCaptor.forClass(MovimientoInventarioRequest.class);
        verify(movimientoInventarioService).registrarMovimiento(captor.capture(), eq(false));
        assertEquals("SALIDA", captor.getValue().getTipoMovimiento());
        assertEquals(4, captor.getValue().getCantidad());
        verify(alertaStockService, never()).generarAlerta(any());
    }

    @Test
    void ajustarInventarioConEntradaNoPuedeSuperarStockMaximo() {
        Inventario inventario = inventario(1L, 19, 5, 20);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.ajustarInventario(1L, ajusteRequest("ENTRADA", 2)));
    }

    @Test
    void ajustarInventarioConSalidaNoPermiteStockNegativo() {
        Inventario inventario = inventario(1L, 2, 5, 20);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.ajustarInventario(1L, ajusteRequest("SALIDA", 3)));
    }

    @Test
    void verificarStockBajoRetornaFalseCuandoHayStockSuficiente() {
        Inventario inventario = inventario(1L, 8, 5, 20);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        assertFalse(inventarioService.verificarStockBajo(1L));
        verify(alertaStockService, never()).generarAlerta(any());
    }

    @Test
    void obtenerInventarioLanzaSiNoExiste() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> inventarioService.obtenerInventario(99L));
    }

    @Test
    void listarYEliminarInventarioDeleganEnRepositorio() {
        Inventario inventario = inventario(1L, 10, 5, 20);
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        assertEquals(1, inventarioService.listarInventarios().size());
        inventarioService.eliminarInventario(1L);

        verify(inventarioRepository).delete(inventario);
    }
}
