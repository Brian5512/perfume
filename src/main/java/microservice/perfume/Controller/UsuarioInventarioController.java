package microservice.perfume.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.perfume.Dto.AjusteInventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.UsuarioInventario;
import microservice.perfume.Service.UsuarioInventarioService;

@RestController
@RequestMapping("api/v1/usuarios-inventario")
public class UsuarioInventarioController {

    private final UsuarioInventarioService usuarioInventarioService;

    public UsuarioInventarioController(UsuarioInventarioService usuarioInventarioService) {
        this.usuarioInventarioService = usuarioInventarioService;
    }

    @PostMapping
    public UsuarioInventario agregarUsuario(@Valid @RequestBody UsuarioInventario usuario) {
        return usuarioInventarioService.agregarUsuario(usuario);
    }

    @GetMapping
    public List<UsuarioInventario> listarUsuarios() {
        return usuarioInventarioService.listarUsuarios();
    }

    @GetMapping("{id}")
    public UsuarioInventario obtenerUsuario(@PathVariable Long id) {
        return usuarioInventarioService.obtenerUsuario(id);
    }

    @PutMapping("{id}")
    public UsuarioInventario actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioInventario usuario) {
        return usuarioInventarioService.actualizarUsuario(id, usuario);
    }

    @DeleteMapping("{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioInventarioService.eliminarUsuario(id);
    }

    @PostMapping("productos")
    public Producto agregarProducto(@Valid @RequestBody Producto producto) {
        return usuarioInventarioService.agregarProducto(producto);
    }

    @PutMapping("inventarios/{idInventario}/stock")
    public Inventario actualizarStock(
            @PathVariable Long idInventario,
            @Valid @RequestBody AjusteInventarioRequest request) {
        return usuarioInventarioService.actualizarStock(idInventario, request);
    }

    @PutMapping("inventarios/{idInventario}/ajuste")
    public Inventario ajustarInventario(
            @PathVariable Long idInventario,
            @Valid @RequestBody AjusteInventarioRequest request) {
        return usuarioInventarioService.ajustarInventario(idInventario, request);
    }
}
