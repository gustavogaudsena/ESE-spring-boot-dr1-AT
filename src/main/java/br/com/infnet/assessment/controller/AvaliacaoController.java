package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.model.Avaliacao;
import br.com.infnet.assessment.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacao")
@RequiredArgsConstructor
public class AvaliacaoController {
    private final AvaliacaoService service;

    @GetMapping
    public ResponseEntity<List<Avaliacao>> findAll() {
        List<Avaliacao> avaliacoes = service.findAll();
        return ResponseEntity.ok().body(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> findAll(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Avaliacao> create(@RequestBody Avaliacao avaliacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(avaliacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> update(@PathVariable Long id, @RequestBody Avaliacao avaliacao) {
        avaliacao.setId(id);
        return ResponseEntity.ok(service.save(avaliacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
