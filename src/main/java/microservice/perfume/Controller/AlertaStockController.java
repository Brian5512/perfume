package microservice.perfume.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.perfume.Model.AlertaStock;
import microservice.perfume.Service.AlertaStockService;

@RestController
@RequestMapping("api/v1/alertas-stock")
public class AlertaStockController {

    private final AlertaStockService alertaStockService;

    public AlertaStockController(AlertaStockService alertaStockService) {
        this.alertaStockService = alertaStockService;
    }

    @GetMapping
    public List<AlertaStock> listarAlertas() {
        return alertaStockService.listarAlertas();
    }

    @GetMapping("{id}/notificar")
    public AlertaStock notificar(@PathVariable Long id) {
        return alertaStockService.notificar(id);
    }

    @PatchMapping("{id}/cerrar")
    public AlertaStock cerrarAlerta(@PathVariable Long id) {
        return alertaStockService.cerrarAlerta(id);
    }
}
