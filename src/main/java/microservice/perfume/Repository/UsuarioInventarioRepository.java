package microservice.perfume.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.UsuarioInventario;

public interface UsuarioInventarioRepository extends JpaRepository<UsuarioInventario, Long> {
}
