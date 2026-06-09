package microservice.perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @NotBlank(message = "El nombre del producto no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripcion del producto no puede estar vacia")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El precio del producto no puede ser nulo")
    @PositiveOrZero(message = "El precio del producto no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @NotBlank(message = "El estado del producto no puede estar vacio")
    @Column(nullable = false)
    private String estado;
}
