package microservice.perfume.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityNotFoundException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundRetornaRespuestaConEstado404() {
        Map<String, Object> respuesta = handler.handleNotFound(new EntityNotFoundException("No existe"));

        assertEquals(404, respuesta.get("estado"));
        assertEquals("No existe", respuesta.get("mensaje"));
        assertTrue(respuesta.containsKey("fecha"));
    }

    @Test
    void handleBadRequestRetornaRespuestaConEstado400() {
        Map<String, Object> respuesta = handler.handleBadRequest(new IllegalArgumentException("Dato invalido"));

        assertEquals(400, respuesta.get("estado"));
        assertEquals("Dato invalido", respuesta.get("mensaje"));
    }
}
