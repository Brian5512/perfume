package microservice.perfume.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.MovimientoInventario;
import microservice.perfume.Service.MovimientoInventarioService;

@RestController
@RequestMapping("api/v1/movimientos-inventario")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoInventarioService) {
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @PostMapping
    public MovimientoInventario registrarMovimiento(@Valid @RequestBody MovimientoInventarioRequest request) {
        return movimientoInventarioService.registrarMovimiento(request);
    }

    @GetMapping
    public List<MovimientoInventario> consultarHistorial() {
        return movimientoInventarioService.consultarHistorial();
    }

    @GetMapping("inventarios/{idInventario}")
    public List<MovimientoInventario> consultarHistorialInventario(@PathVariable Long idInventario) {
        return movimientoInventarioService.consultarHistorialInventario(idInventario);
    }
}
