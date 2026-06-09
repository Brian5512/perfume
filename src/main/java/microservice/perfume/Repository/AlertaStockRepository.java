package microservice.perfume.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.AlertaStock;

public interface AlertaStockRepository extends JpaRepository<AlertaStock, Long> {

    List<AlertaStock> findByInventarioIdInventarioAndEstado(Long idInventario, String estado);
}
