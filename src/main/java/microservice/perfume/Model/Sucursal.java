package microservice.perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSucursal;

    @NotBlank(message = "El nombre de la sucursal no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La direccion de la sucursal no puede estar vacia")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "El estado de la sucursal no puede estar vacio")
    @Column(nullable = false)
    private String estado;
}
