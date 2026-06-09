package microservice.perfume.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Dto.DisponibilidadProductoResponse;
import microservice.perfume.Model.Producto;
import microservice.perfume.Repository.InventarioRepository;
import microservice.perfume.Repository.ProductoRepository;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public ProductoService(ProductoRepository productoRepository, InventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    public Producto agregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id " + id));
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        Producto existente = obtenerProducto(id);
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setEstado(producto.getEstado());
        return productoRepository.save(existente);
    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerProducto(id);
        productoRepository.delete(producto);
    }

    public DisponibilidadProductoResponse consultarDisponibilidad(Long idProducto) {
        obtenerProducto(idProducto);
        Integer stockDisponible = inventarioRepository.consultarDisponibilidadProducto(idProducto);
        return new DisponibilidadProductoResponse(idProducto, stockDisponible);
    }
}
