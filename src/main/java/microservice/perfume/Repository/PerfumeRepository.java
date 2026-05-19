package microservice.perfume.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.Perfume;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    
}
