package microservice.perfume.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisponibilidadProductoResponse {

    private Long productoId;
    private Integer stockDisponible;
}
