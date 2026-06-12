package microservice.perfume.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.perfume.Dto.BodegaDescuentoRequest;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BodegaClientTest {

    private static final String URL = "http://localhost:8093/api/v1/despachos-producto/ventas/descontar";

    @Mock
    private RestTemplate restTemplate;

    private BodegaClient bodegaClient;

    @BeforeEach
    void setup() {
        bodegaClient = new BodegaClient(restTemplate, URL);
    }

    @Test
    void descontarPorVenta_ok_llamaEndpointBodega() {
        BodegaDescuentoRequest request = new BodegaDescuentoRequest(1L, 1L, 100L, 2, "Venta #100");

        when(restTemplate.postForEntity(URL, request, Void.class))
                .thenReturn(ResponseEntity.ok().build());

        bodegaClient.descontarPorVenta(request);

        verify(restTemplate).postForEntity(URL, request, Void.class);
    }

    @Test
    void descontarPorVenta_errorRestTemplate_lanzaIllegalStateException() {
        BodegaDescuentoRequest request = new BodegaDescuentoRequest(1L, 1L, 100L, 2, "Venta #100");

        when(restTemplate.postForEntity(URL, request, Void.class))
                .thenThrow(new RestClientException("conexion rechazada"));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> bodegaClient.descontarPorVenta(request));

        assertTrue(exception.getMessage().contains("No se pudo descontar stock en bodega"));
        assertInstanceOf(RestClientException.class, exception.getCause());
    }
}
