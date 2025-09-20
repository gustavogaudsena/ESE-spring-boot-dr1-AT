package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.dto.AtribuirNotaDTO;
import br.com.infnet.assessment.dto.CriarAlunoMatriculaDTO;
import br.com.infnet.assessment.dto.MatricularAlunoDTO;
import br.com.infnet.assessment.model.*;
import br.com.infnet.assessment.service.AlunoService;
import br.com.infnet.assessment.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aluno")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoService alunoService;
    private final MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<List<Aluno>> listar(
            @RequestHeader(value = "X-Page", required = false, defaultValue = "0") int page,
            @RequestHeader(value = "X-Size", required = false, defaultValue = "10") int size
    ) {
        Page<Aluno> transacoes = alunoService.listar(page, size);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(transacoes.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(transacoes.getTotalPages()))
                .header("X-Page-Size", String.valueOf(size))
                .body(transacoes.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> obterPorId(@PathVariable Long id) {
        return alunoService.obterPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/aprovados")
    public ResponseEntity<List<Aluno>> listarAprovados(
            @RequestParam(name = "disciplina") String disciplinaCodigo
    ) {
        List<Aluno> alunosAprovados = matriculaService.listarAlunosAprovados(disciplinaCodigo);
        return ResponseEntity.ok(alunosAprovados);
    }

    @GetMapping("/reprovados")
    public ResponseEntity<List<Aluno>> listarReprovados(
            @RequestParam(name = "disciplina") String disciplinaCodigo
    ) {
        List<Aluno> alunosReprovados = matriculaService.listarAlunosReprovados(disciplinaCodigo);
        return ResponseEntity.ok(alunosReprovados);
    }

    @PostMapping
    public ResponseEntity<Aluno> criarAlunoEMatricular(@RequestBody CriarAlunoMatriculaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.criarAlunoEMatricular(dto));
    }

    @PostMapping("/{alunoId}/matricula")
    public ResponseEntity<Matricula> matricularAluno(
            @PathVariable Long alunoId,
            @RequestBody MatricularAlunoDTO dto
    ) {
        Matricula matricula = matriculaService.matricularAluno(alunoId, dto.getDisciplinaCodigo());
        return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
    }

    @PostMapping("/{alunoId}/avaliacoes")
    public ResponseEntity<Avaliacao> atribuirNota(
            @PathVariable Long alunoId,
            @RequestBody AtribuirNotaDTO dto
    ) {
        Avaliacao avaliacao = matriculaService.atribuirNota(alunoId, dto.getDisciplinaCodigo(), dto.getNota());
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @RequestBody Aluno aluno) {
        return ResponseEntity.ok(alunoService.atualizar(id, aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        alunoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
