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
import microservice.perfume.Dto.DisponibilidadProductoResponse;
import microservice.perfume.Model.Producto;
import microservice.perfume.Service.ProductoService;

@RestController
@RequestMapping("api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public Producto agregarProducto(@Valid @RequestBody Producto producto) {
        return productoService.agregarProducto(producto);
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("{id}")
    public Producto obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProducto(id);
    }

    @PutMapping("{id}")
    public Producto actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }

    @DeleteMapping("{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }

    @GetMapping("{id}/disponibilidad")
    public DisponibilidadProductoResponse consultarDisponibilidad(@PathVariable Long id) {
        return productoService.consultarDisponibilidad(id);
    }
}
