package microservice.perfume.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
