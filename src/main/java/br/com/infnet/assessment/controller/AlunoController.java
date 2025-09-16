package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aluno")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoService service;

    @GetMapping
    public ResponseEntity<List<Aluno>> findAll() {
        List<Aluno> alunos = service.findAll();
        return ResponseEntity.ok().body(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> findAll(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aluno> create(@RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(aluno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> update(@PathVariable Long id, @RequestBody Aluno aluno) {
        aluno.setId(id);
        return ResponseEntity.ok(service.save(aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
