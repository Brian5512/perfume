package microservice.perfume.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.perfume.Dto.BodegaDescuentoRequest;

@Service
public class BodegaClient {

    private final RestTemplate restTemplate;
    private final String descuentoVentaUrl;

    public BodegaClient(
            RestTemplate restTemplate,
            @Value("${bodega.api.descuento-venta-url:http://localhost:8093/api/v1/despachos-producto/ventas/descontar}") String descuentoVentaUrl) {
        this.restTemplate = restTemplate;
        this.descuentoVentaUrl = descuentoVentaUrl;
    }

    public void descontarPorVenta(BodegaDescuentoRequest request) {
        try {
            restTemplate.postForEntity(descuentoVentaUrl, request, Void.class);
        } catch (RestClientException exception) {
            throw new IllegalStateException("No se pudo descontar stock en bodega: " + exception.getMessage(), exception);
        }
    }
}
