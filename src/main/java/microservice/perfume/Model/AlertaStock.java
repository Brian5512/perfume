package microservice.perfume.Model;

import java.time.LocalDate;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AlertaStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;

    @Column(nullable = false)
    private LocalDate fechaAlerta;

    @NotBlank(message = "El mensaje no puede estar vacio")
    @Column(nullable = false)
    private String mensaje;

    @NotBlank(message = "El nivel de prioridad no puede estar vacio")
    @Column(nullable = false)
    private String nivelPrioridad;

    @NotBlank(message = "El estado no puede estar vacio")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El inventario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_inventario", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Inventario inventario;
}
