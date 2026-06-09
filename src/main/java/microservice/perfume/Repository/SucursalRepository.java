package microservice.perfume.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
