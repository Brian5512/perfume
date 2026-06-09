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
public class UsuarioInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "El nombre del usuario no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El rol del usuario no puede estar vacio")
    @Column(nullable = false)
    private String rol;

    @NotBlank(message = "El estado del usuario no puede estar vacio")
    @Column(nullable = false)
    private String estado;
}
