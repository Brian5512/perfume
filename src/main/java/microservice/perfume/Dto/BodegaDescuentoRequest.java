package microservice.perfume.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BodegaDescuentoRequest {

    private Long productoId;
    private Long sucursalId;
    private Long ventaId;
    private Integer cantidad;
    private String motivo;
}
