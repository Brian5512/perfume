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
import microservice.perfume.Dto.InventarioRequest;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Service.InventarioService;

@RestController
@RequestMapping("api/v1/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public Inventario crearInventario(@Valid @RequestBody InventarioRequest request) {
        return inventarioService.crearInventario(request);
    }

    @GetMapping
    public List<Inventario> listarInventarios() {
        return inventarioService.listarInventarios();
    }

    @GetMapping("{id}")
    public Inventario obtenerInventario(@PathVariable Long id) {
        return inventarioService.obtenerInventario(id);
    }

    @PutMapping("{id}")
    public Inventario actualizarInventario(@PathVariable Long id, @Valid @RequestBody InventarioRequest request) {
        return inventarioService.actualizarInventario(id, request);
    }

    @DeleteMapping("{id}")
    public void eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
    }

    @PutMapping("{id}/ajustar")
    public Inventario ajustarInventario(
            @PathVariable Long id,
            @Valid @RequestBody AjusteInventarioRequest request) {
        return inventarioService.ajustarInventario(id, request);
    }

    @GetMapping("{id}/stock-bajo")
    public boolean verificarStockBajo(@PathVariable Long id) {
        return inventarioService.verificarStockBajo(id);
    }
}
