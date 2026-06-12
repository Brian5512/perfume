package microservice.perfume.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DescuentoVentaRequest {

    private Long inventarioId;

    @NotNull
    private Long productoId;

    @NotNull
    private Long sucursalId;

    private Long usuarioId;

    private Long ventaId;

    @NotNull
    @Positive
    private Integer cantidad;
}
