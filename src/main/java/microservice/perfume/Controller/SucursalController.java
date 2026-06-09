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
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Service.SucursalService;

@RestController
@RequestMapping("api/v1/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @PostMapping
    public Sucursal asignarSucursal(@Valid @RequestBody Sucursal sucursal) {
        return sucursalService.asignarSucursal(sucursal);
    }

    @GetMapping
    public List<Sucursal> listarSucursales() {
        return sucursalService.listarSucursales();
    }

    @GetMapping("{id}")
    public Sucursal obtenerSucursal(@PathVariable Long id) {
        return sucursalService.obtenerSucursal(id);
    }

    @PutMapping("{id}")
    public Sucursal actualizarSucursal(@PathVariable Long id, @Valid @RequestBody Sucursal sucursal) {
        return sucursalService.actualizarSucursal(id, sucursal);
    }

    @DeleteMapping("{id}")
    public void eliminarSucursal(@PathVariable Long id) {
        sucursalService.eliminarSucursal(id);
    }

    @GetMapping("{id}/inventario")
    public List<Inventario> consultarInventario(@PathVariable Long id) {
        return sucursalService.consultarInventario(id);
    }
}
