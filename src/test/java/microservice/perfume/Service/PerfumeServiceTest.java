package microservice.perfume.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import microservice.perfume.Model.Perfume;
import microservice.perfume.Repository.PerfumeRepository;

@ExtendWith(MockitoExtension.class)
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @InjectMocks
    private PerfumeService perfumeService;

    @Test
    void crearListarObtenerEliminarYActualizarPerfumeExistente() {
        Perfume perfume = perfume(1L, "Citrus", "Marca A", "Fresco", 19990.0);
        Perfume cambios = perfume(1L, "Floral", "Marca B", "Dulce", 29990.0);

        when(perfumeRepository.save(any(Perfume.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(perfumeRepository.findAll()).thenReturn(List.of(perfume));
        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));

        assertEquals(perfume, perfumeService.crearPerfume(perfume));
        assertEquals(List.of(perfume), perfumeService.obtenerPerfume());
        assertEquals(perfume, perfumeService.obtenerPerfumePorId(1L));
        assertEquals("Floral", perfumeService.updatePerfume(1L, cambios).getNombrePerfume());

        perfumeService.deletePerfume(1L);
        verify(perfumeRepository).deleteById(1L);
    }

    @Test
    void obtenerPerfumePorIdRetornaNullSiNoExiste() {
        when(perfumeRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(perfumeService.obtenerPerfumePorId(99L));
    }

    private Perfume perfume(Long id, String nombre, String marca, String descripcion, double precio) {
        Perfume perfume = new Perfume();
        perfume.setIdPerfume(id);
        perfume.setNombrePerfume(nombre);
        perfume.setMarcaPerfume(marca);
        perfume.setDescripcionPerfume(descripcion);
        perfume.setPrecioPerfume(precio);
        return perfume;
    }
}
