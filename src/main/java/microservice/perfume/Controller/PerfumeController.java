package microservice.perfume.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.perfume.Model.Perfume;
import microservice.perfume.Service.PerfumeService;

@RestController
@RequestMapping("api/v1/perfumes")
public class PerfumeController {
    @Autowired
    private PerfumeService perfumeService;

    @PostMapping()
    public Perfume postPerfume(@RequestBody Perfume perfume){
        return perfumeService.crearPerfume(perfume);
    }
    
    @GetMapping()
    public List<Perfume> getPerfumes() {
        return perfumeService.obtenerPerfume();
    }
    
    @GetMapping("{id}")
    public Perfume getPerfumeById(@PathVariable Long id){
        return perfumeService.obtenerPerfumePorId(id);
    }
    @PutMapping("{id}")
    public Perfume putPerfume(@PathVariable Long id, @RequestBody Perfume perfume){
        return perfumeService.updatePerfume(id,perfume);
    }

    @DeleteMapping("{id}")
    public void deletePerfume(@PathVariable Long id){
        perfumeService.deletePerfume(id);
    }
}
