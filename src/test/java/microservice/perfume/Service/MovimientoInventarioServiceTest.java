package microservice.perfume.Service;

import static microservice.perfume.TestDataFactory.inventario;
import static microservice.perfume.TestDataFactory.movimiento;
import static microservice.perfume.TestDataFactory.movimientoRequest;
import static microservice.perfume.TestDataFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.MovimientoInventario;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.MovimientoInventarioRepository;
import microservice.perfume.Repository.UsuarioInventarioRepository;

@ExtendWith(MockitoExtension.class)
class MovimientoInventarioServiceTest {

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private UsuarioInventarioRepository usuarioInventarioRepository;

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private MovimientoInventarioService movimientoInventarioService;

    @Test
    void registrarMovimientoEntradaAjustaStockYGuardaMovimiento() {
        Inventario inventario = inventario(1L, 10, 5, 20);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(usuarioInventarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L)));
        when(movimientoInventarioRepository.save(any(MovimientoInventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoInventario resultado = movimientoInventarioService.registrarMovimiento(movimientoRequest("ENTRADA", 5));

        assertEquals(15, inventario.getStockActual());
        assertEquals("ENTRADA", resultado.getTipoMovimiento());
        assertNotNull(resultado.getFechaMovimiento());
        verify(inventarioRepository).save(inventario);
        verify(inventarioService).verificarStockBajo(1L);
    }

    @Test
    void registrarMovimientoSinAjustarStockSoloGuardaHistorial() {
        Inventario inventario = inventario(1L, 10, 5, 20);
        MovimientoInventarioRequest request = movimientoRequest("SALIDA", 2);
        request.setUsuarioId(null);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(movimientoInventarioRepository.save(any(MovimientoInventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoInventario resultado = movimientoInventarioService.registrarMovimiento(request, false);

        assertEquals(10, inventario.getStockActual());
        assertNull(resultado.getUsuario());
        verify(inventarioRepository, never()).save(any());
        verify(inventarioService, never()).verificarStockBajo(1L);
    }

    @Test
    void registrarMovimientoSalidaNoPermiteStockNegativo() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario(1L, 1, 5, 20)));

        assertThrows(IllegalArgumentException.class,
                () -> movimientoInventarioService.registrarMovimiento(movimientoRequest("SALIDA", 2)));
    }

    @Test
    void registrarMovimientoAjusteNoPermiteSuperarMaximo() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario(1L, 10, 5, 20)));

        assertThrows(IllegalArgumentException.class,
                () -> movimientoInventarioService.registrarMovimiento(movimientoRequest("AJUSTE", 21)));
    }

    @Test
    void registrarMovimientoRechazaTipoInvalido() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario(1L, 10, 5, 20)));

        assertThrows(IllegalArgumentException.class,
                () -> movimientoInventarioService.registrarMovimiento(movimientoRequest("TRASPASO", 1)));
    }

    @Test
    void registrarMovimientoLanzaSiUsuarioNoExiste() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario(1L, 10, 5, 20)));
        when(usuarioInventarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> movimientoInventarioService.registrarMovimiento(movimientoRequest("SALIDA", 1), false));
    }

    @Test
    void consultarHistorialInventarioValidaInventarioYRetornaOrdenado() {
        MovimientoInventario movimiento = movimiento(1L);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario(1L, 10, 5, 20)));
        when(movimientoInventarioRepository.findByInventarioIdInventarioOrderByFechaMovimientoDesc(1L))
                .thenReturn(List.of(movimiento));

        assertEquals(List.of(movimiento), movimientoInventarioService.consultarHistorialInventario(1L));
    }

    @Test
    void consultarHistorialRetornaTodosLosMovimientos() {
        when(movimientoInventarioRepository.findAll()).thenReturn(List.of(movimiento(1L), movimiento(2L)));

        assertEquals(2, movimientoInventarioService.consultarHistorial().size());
    }
}
