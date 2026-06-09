package microservice.perfume.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class InventarioRequest {

    @NotNull
    private Long productoId;

    @NotNull
    private Long sucursalId;

    @NotNull
    @PositiveOrZero
    private Integer stockActual;

    @NotNull
    @PositiveOrZero
    private Integer stockMinimo;

    @NotNull
    @PositiveOrZero
    private Integer stockMaximo;

    @NotBlank
    private String ubicacion;
}
