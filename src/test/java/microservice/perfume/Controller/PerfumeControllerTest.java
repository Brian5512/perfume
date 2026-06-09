package microservice.perfume.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import microservice.perfume.Model.Perfume;
import microservice.perfume.Service.PerfumeService;

class PerfumeControllerTest {

    private final PerfumeService perfumeService = org.mockito.Mockito.mock(PerfumeService.class);
    private final PerfumeController perfumeController = new PerfumeController();

    @Test
    void controllerPerfumeDelegaOperacionesCrud() throws Exception {
        setPrivateService(perfumeController, perfumeService);
        Perfume perfume = perfume();

        when(perfumeService.crearPerfume(perfume)).thenReturn(perfume);
        when(perfumeService.obtenerPerfume()).thenReturn(List.of(perfume));
        when(perfumeService.obtenerPerfumePorId(1L)).thenReturn(perfume);
        when(perfumeService.updatePerfume(1L, perfume)).thenReturn(perfume);

        assertEquals(perfume, perfumeController.postPerfume(perfume));
        assertEquals(List.of(perfume), perfumeController.getPerfumes());
        assertEquals(perfume, perfumeController.getPerfumeById(1L));
        assertEquals(perfume, perfumeController.putPerfume(1L, perfume));
        perfumeController.deletePerfume(1L);

        verify(perfumeService).deletePerfume(1L);
    }

    private Perfume perfume() {
        Perfume perfume = new Perfume();
        perfume.setIdPerfume(1L);
        perfume.setNombrePerfume("Citrus");
        perfume.setMarcaPerfume("Marca A");
        perfume.setDescripcionPerfume("Fresco");
        perfume.setPrecioPerfume(19990.0);
        return perfume;
    }

    private void setPrivateService(PerfumeController controller, PerfumeService service) throws Exception {
        java.lang.reflect.Field field = PerfumeController.class.getDeclaredField("perfumeService");
        field.setAccessible(true);
        field.set(controller, service);
    }
}
