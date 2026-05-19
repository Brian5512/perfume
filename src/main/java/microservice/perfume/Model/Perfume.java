package microservice.perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



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
    
    @Column(nullable=false)
    private String nombrePerfume;

    @Column(nullable=false)
    private String marcaPerfume;

    @Column(nullable=false)
    private String descripcionPerfume;

    @Column(nullable=false)
    private double precioPerfume;

}

