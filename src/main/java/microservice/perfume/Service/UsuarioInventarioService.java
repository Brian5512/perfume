package microservice.perfume.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Dto.AjusteInventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.UsuarioInventario;
import microservice.perfume.Repository.UsuarioInventarioRepository;

@Service
@Transactional
public class UsuarioInventarioService {

    private final UsuarioInventarioRepository usuarioInventarioRepository;
    private final ProductoService productoService;
    private final InventarioService inventarioService;

    public UsuarioInventarioService(
            UsuarioInventarioRepository usuarioInventarioRepository,
            ProductoService productoService,
            InventarioService inventarioService) {
        this.usuarioInventarioRepository = usuarioInventarioRepository;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
    }

    public UsuarioInventario agregarUsuario(UsuarioInventario usuario) {
        return usuarioInventarioRepository.save(usuario);
    }

    public List<UsuarioInventario> listarUsuarios() {
        return usuarioInventarioRepository.findAll();
    }

    public UsuarioInventario obtenerUsuario(Long id) {
        return usuarioInventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario de inventario no encontrado con id " + id));
    }

    public UsuarioInventario actualizarUsuario(Long id, UsuarioInventario usuario) {
        UsuarioInventario existente = obtenerUsuario(id);
        existente.setNombre(usuario.getNombre());
        existente.setRol(usuario.getRol());
        existente.setEstado(usuario.getEstado());
        return usuarioInventarioRepository.save(existente);
    }

    public void eliminarUsuario(Long id) {
        UsuarioInventario usuario = obtenerUsuario(id);
        usuarioInventarioRepository.delete(usuario);
    }

    public Producto agregarProducto(Producto producto) {
        return productoService.agregarProducto(producto);
    }

    public Inventario actualizarStock(Long idInventario, AjusteInventarioRequest request) {
        return inventarioService.ajustarInventario(idInventario, request);
    }

    public Inventario ajustarInventario(Long idInventario, AjusteInventarioRequest request) {
        return inventarioService.ajustarInventario(idInventario, request);
    }
}
