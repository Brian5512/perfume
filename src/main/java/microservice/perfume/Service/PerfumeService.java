package microservice.perfume.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

