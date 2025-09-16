package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.service.DisciplinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplina")
@RequiredArgsConstructor
public class DisciplinaController {
    private final DisciplinaService service;

    @GetMapping
    public ResponseEntity<List<Disciplina>> findAll() {
        List<Disciplina> disciplinas = service.findAll();
        return ResponseEntity.ok().body(disciplinas);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> findAll(@PathVariable String codigo) {
        return service.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Disciplina> create(@RequestBody Disciplina disciplina) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(disciplina));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Disciplina> update(@PathVariable String codigo, @RequestBody Disciplina disciplina) {
        disciplina.setCodigo(codigo);
        return ResponseEntity.ok(service.save(disciplina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String codigo) {
        service.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
