package microservice.perfume.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Model.AlertaStock;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Repository.AlertaStockRepository;

@Service
@Transactional
public class AlertaStockService {

    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_RESUELTA = "RESUELTA";

    private final AlertaStockRepository alertaStockRepository;

    public AlertaStockService(AlertaStockRepository alertaStockRepository) {
        this.alertaStockRepository = alertaStockRepository;
    }

    public AlertaStock generarAlerta(Inventario inventario) {
        List<AlertaStock> alertasActivas = alertaStockRepository
                .findByInventarioIdInventarioAndEstado(inventario.getIdInventario(), ESTADO_ACTIVA);

        if (!alertasActivas.isEmpty()) {
            return alertasActivas.get(0);
        }

        AlertaStock alerta = new AlertaStock();
        alerta.setFechaAlerta(LocalDate.now());
        alerta.setMensaje("Stock bajo para " + inventario.getProducto().getNombre()
                + " en " + inventario.getSucursal().getNombre());
        alerta.setNivelPrioridad(calcularPrioridad(inventario));
        alerta.setEstado(ESTADO_ACTIVA);
        alerta.setInventario(inventario);
        return alertaStockRepository.save(alerta);
    }

    public List<AlertaStock> listarAlertas() {
        return alertaStockRepository.findAll();
    }

    public AlertaStock notificar(Long idAlerta) {
        return obtenerAlerta(idAlerta);
    }

    public AlertaStock cerrarAlerta(Long idAlerta) {
        AlertaStock alerta = obtenerAlerta(idAlerta);
        alerta.setEstado(ESTADO_RESUELTA);
        return alertaStockRepository.save(alerta);
    }

    private AlertaStock obtenerAlerta(Long idAlerta) {
        return alertaStockRepository.findById(idAlerta)
                .orElseThrow(() -> new EntityNotFoundException("Alerta no encontrada con id " + idAlerta));
    }

    private String calcularPrioridad(Inventario inventario) {
        if (inventario.getStockActual() == 0) {
            return "CRITICA";
        }
        return "ALTA";
    }
}
