package microservice.perfume.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AjusteInventarioRequest {

    private Long usuarioId;

    @NotBlank
    private String tipoMovimiento;

    @NotNull
    @Positive
    private Integer cantidad;

    @NotBlank
    private String motivo;
}
