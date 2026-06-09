package microservice.perfume.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(EntityNotFoundException exception) {
        return crearRespuesta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(IllegalArgumentException exception) {
        return crearRespuesta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> respuesta = crearRespuesta(HttpStatus.BAD_REQUEST, "Error de validacion");
        respuesta.put("errores", errores);
        return respuesta;
    }

    private Map<String, Object> crearRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}
