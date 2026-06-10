package microservice.perfume.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.persistence.EntityNotFoundException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundRetornaRespuestaConEstado404() {
        Map<String, Object> respuesta =
                handler.handleNotFound(new EntityNotFoundException("No existe"));

        assertEquals(404, respuesta.get("estado"));
        assertEquals("No existe", respuesta.get("mensaje"));
        assertTrue(respuesta.containsKey("fecha"));
        assertEquals("Not Found", respuesta.get("error"));
    }

    @Test
    void handleBadRequestRetornaRespuestaConEstado400() {
        Map<String, Object> respuesta =
                handler.handleBadRequest(new IllegalArgumentException("Dato invalido"));

        assertEquals(400, respuesta.get("estado"));
        assertEquals("Dato invalido", respuesta.get("mensaje"));
        assertEquals("Bad Request", respuesta.get("error"));
    }

    @Test
    void handleValidationRetornaErroresDeCampos() {

        // Creamos un objeto "dummy" para el binding result
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "obj");

        // Agregamos errores simulados
        bindingResult.addError(new FieldError("obj", "nombre", "no puede ser vacío"));
        bindingResult.addError(new FieldError("obj", "precio", "debe ser mayor a 0"));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        Map<String, Object> respuesta = handler.handleValidation(exception);

        assertEquals(400, respuesta.get("estado"));
        assertEquals("Error de validacion", respuesta.get("mensaje"));
        assertTrue(respuesta.containsKey("errores"));

        Map<String, String> errores =
                (Map<String, String>) respuesta.get("errores");

        assertEquals("no puede ser vacío", errores.get("nombre"));
        assertEquals("debe ser mayor a 0", errores.get("precio"));
        assertEquals(2, errores.size());

        assertTrue(respuesta.containsKey("fecha"));
        assertEquals("Bad Request", respuesta.get("error"));
    }
}