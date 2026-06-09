package microservice.perfume.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.SucursalRepository;

@Service
@Transactional
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final InventarioRepository inventarioRepository;

    public SucursalService(SucursalRepository sucursalRepository, InventarioRepository inventarioRepository) {
        this.sucursalRepository = sucursalRepository;
        this.inventarioRepository = inventarioRepository;
    }

    public Sucursal asignarSucursal(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    public List<Sucursal> listarSucursales() {
        return sucursalRepository.findAll();
    }

    public Sucursal obtenerSucursal(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sucursal no encontrada con id " + id));
    }

    public Sucursal actualizarSucursal(Long id, Sucursal sucursal) {
        Sucursal existente = obtenerSucursal(id);
        existente.setNombre(sucursal.getNombre());
        existente.setDireccion(sucursal.getDireccion());
        existente.setEstado(sucursal.getEstado());
        return sucursalRepository.save(existente);
    }

    public void eliminarSucursal(Long id) {
        Sucursal sucursal = obtenerSucursal(id);
        sucursalRepository.delete(sucursal);
    }

    public List<Inventario> consultarInventario(Long idSucursal) {
        obtenerSucursal(idSucursal);
        return inventarioRepository.findBySucursalIdSucursal(idSucursal);
    }
}
