package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.service.DisciplinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/disciplina")
@RequiredArgsConstructor
public class DisciplinaController {
    private final DisciplinaService service;

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestHeader(value = "X-Page", required = false, defaultValue = "0") int page,
            @RequestHeader(value = "X-Size", required = false, defaultValue = "10") int size
    ) {
        Page<Disciplina> transacoes = service.listar(page, size);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(transacoes.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(transacoes.getTotalPages()))
                .header("X-Page-Size", String.valueOf(size))
                .body(transacoes.getContent());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Disciplina> obterPorCodigo(@PathVariable String codigo) {
        return service.obterPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Disciplina> criar (@RequestBody Disciplina disciplina) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(disciplina));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Disciplina> atualizar(@PathVariable String codigo, @RequestBody Disciplina disciplina) {
        return ResponseEntity.ok(service.atualizar(codigo, disciplina));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deletar(@PathVariable String codigo) {
        service.deletar(codigo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
