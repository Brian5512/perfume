package microservice.perfume.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import microservice.perfume.Model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByProductoIdProducto(Long idProducto);

    List<Inventario> findBySucursalIdSucursal(Long idSucursal);

    Optional<Inventario> findByProductoIdProductoAndSucursalIdSucursal(Long idProducto, Long idSucursal);

    @Query("select coalesce(sum(i.stockActual), 0) from Inventario i where i.producto.idProducto = :idProducto")
    Integer consultarDisponibilidadProducto(@Param("idProducto") Long idProducto);
}
