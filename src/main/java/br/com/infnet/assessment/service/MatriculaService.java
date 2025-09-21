package br.com.infnet.assessment.service;

import br.com.infnet.assessment.dto.CriarAlunoMatriculaDTO;
import br.com.infnet.assessment.model.*;
import br.com.infnet.assessment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final MatriculaRepository matriculaRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    private static final Double NOTA_DE_CORTE = 7.0;

    @Transactional
    public Aluno criarAlunoEMatricular(CriarAlunoMatriculaDTO dto) {
        Aluno novoAluno = new Aluno();
        novoAluno.setNome(dto.getNome());
        novoAluno.setCpf(dto.getCpf());
        novoAluno.setEmail(dto.getEmail());

        Aluno aluno = alunoRepository.save(novoAluno);

        Matricula matricula = this.matricularAluno(aluno.getId(), dto.getDisciplinaCodigo());

        aluno.setMatriculas(List.of(matricula));

        return aluno;
    }

    @Transactional
    public Matricula matricularAluno(Long alunoId, String disciplinaCodigo) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno não encontrado com o ID: " + alunoId));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaCodigo)
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada com o código: " + disciplinaCodigo));

        if (matriculaRepository.existsByAlunoAndDisciplina(aluno, disciplina)) {
            throw new IllegalStateException("Aluno já matriculado nesta disciplina.");
        }

        Matricula novaMatricula = new Matricula(aluno, disciplina);
        return matriculaRepository.save(novaMatricula);
    }

    @Transactional
    public Avaliacao atribuirNota(Long alunoId, String disciplinaCodigo, Double nota) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno não encontrado com o ID: " + alunoId));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaCodigo)
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada com o código: " + disciplinaCodigo));

        Matricula matricula = matriculaRepository.findByAlunoAndDisciplina(aluno, disciplina)
                .orElseThrow(() -> new IllegalStateException("O aluno " + aluno.getNome() + " não está matriculado na disciplina " + disciplina.getNome()));

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setMatricula(matricula);
        novaAvaliacao.setNota(nota);

        return avaliacaoRepository.save(novaAvaliacao);
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarAlunosAprovados(String disciplinaCodigo) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaCodigo)
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada com o código: " + disciplinaCodigo));

        List<Matricula> matriculasAprovadas = matriculaRepository.findMatriculasAprovadas(disciplina, NOTA_DE_CORTE);
        return matriculasAprovadas.stream().map(Matricula::getAluno).distinct().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarAlunosReprovados(String disciplinaCodigo) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaCodigo)
                .orElseThrow(() -> new NoSuchElementException("Disciplina não encontrada com o código: " + disciplinaCodigo));

        List<Matricula> matriculasReprovadas = matriculaRepository.findMatriculasReprovadas(disciplina, NOTA_DE_CORTE);
        return matriculasReprovadas.stream().map(Matricula::getAluno).distinct().collect(Collectors.toList());
    }
}