package microservice.perfume.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventario;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Producto producto;

    @NotNull(message = "La sucursal es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sucursal", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Sucursal sucursal;

    @NotNull(message = "El stock actual no puede ser nulo")
    @PositiveOrZero(message = "El stock actual no puede ser negativo")
    @Column(nullable = false)
    private Integer stockActual;

    @NotNull(message = "El stock minimo no puede ser nulo")
    @PositiveOrZero(message = "El stock minimo no puede ser negativo")
    @Column(nullable = false)
    private Integer stockMinimo;

    @NotNull(message = "El stock maximo no puede ser nulo")
    @PositiveOrZero(message = "El stock maximo no puede ser negativo")
    @Column(nullable = false)
    private Integer stockMaximo;

    @NotBlank(message = "La ubicacion no puede estar vacia")
    @Column(nullable = false)
    private String ubicacion;
}
