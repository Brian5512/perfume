package microservice.perfume.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByInventarioIdInventarioOrderByFechaMovimientoDesc(Long idInventario);
}
