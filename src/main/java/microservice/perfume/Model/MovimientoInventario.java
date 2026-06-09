package microservice.perfume.Model;

import java.time.LocalDateTime;

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
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @NotBlank(message = "El tipo de movimiento no puede estar vacio")
    @Column(nullable = false)
    private String tipoMovimiento;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private LocalDateTime fechaMovimiento;

    @NotBlank(message = "El motivo no puede estar vacio")
    @Column(nullable = false)
    private String motivo;

    @NotNull(message = "El inventario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_inventario", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Inventario inventario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioInventario usuario;
}
