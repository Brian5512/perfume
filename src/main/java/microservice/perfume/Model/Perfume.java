package microservice.perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Perfume {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfume;
    
    @NotBlank(message = "El nombre del perfume no puede estar vacío")
    @NotEmpty(message = "El nombre del perfume no puede estar vacío")
    @Column(nullable=false)
    private String nombrePerfume;

    @NotBlank(message = "El nombre del perfume no puede estar vacío")
    @NotEmpty(message = "El nombre del perfume no puede estar vacío")
    @Column(nullable=false)
    private String marcaPerfume;

    @NotBlank(message = "El nombre del perfume no puede estar vacío")
    @NotEmpty(message = "El nombre del perfume no puede estar vacío")
    @Column(nullable=false)
    private String descripcionPerfume;

    @NotNull(message = "El precio del perfume no puede ser nulo")
    @Column(nullable=false)
    private double precioPerfume;

}

