package microservice.perfume.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.transaction.Transactional;
import microservice.perfume.Model.Perfume;
import microservice.perfume.Repository.PerfumeRepository;

@Service
@Transactional

public class PerfumeService {
    @Autowired
    private PerfumeRepository perfumeRepository;

    public Perfume crearPerfume(Perfume perfume){
        return perfumeRepository.save(perfume);
    }

    public List<Perfume> obtenerPerfume(){
        return perfumeRepository.findAll();
    }

    public void deletePerfume(Long id) {
        perfumeRepository.deleteById(id);
    }

    public Perfume updatePerfume(Long id,  Perfume perfume) {
        Perfume perfumeExistente = perfumeRepository.findById(id).orElse(null);
        if (perfumeExistente != null) {
            perfumeExistente.setNombrePerfume(perfume.getNombrePerfume());
            perfumeExistente.setMarcaPerfume(perfume.getMarcaPerfume());
            perfumeExistente.setDescripcionPerfume(perfume.getDescripcionPerfume());
            perfumeExistente.setPrecioPerfume(perfume.getPrecioPerfume());
        }
        return perfumeRepository.save(perfumeExistente);
    }

    public Perfume obtenerPerfumePorId(Long id) {
        return perfumeRepository.findById(id).orElse(null);
    }
    

}

