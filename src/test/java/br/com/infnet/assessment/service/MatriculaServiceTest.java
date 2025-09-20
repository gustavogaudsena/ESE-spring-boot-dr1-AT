package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.model.Avaliacao;
import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.model.Matricula;
import br.com.infnet.assessment.repository.AlunoRepository;
import br.com.infnet.assessment.repository.AvaliacaoRepository;
import br.com.infnet.assessment.repository.DisciplinaRepository;
import br.com.infnet.assessment.repository.MatriculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks
    private MatriculaService matriculaService;
    private final Random random = new Random();
    private final Long ALUNO_ID = random.nextLong(Long.MAX_VALUE);
    private final Double NOTA_PADRAO = 7d;
    private final String NOME_PADRAO = "Gustavo";
    private final String CODIGO_DISCIPLINA_PADRAO = "25GRLEDS01BAD101";

    private Aluno aluno;
    private Disciplina disciplina;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(ALUNO_ID);
        aluno.setNome(NOME_PADRAO);

        disciplina = new Disciplina();
        disciplina.setCodigo(CODIGO_DISCIPLINA_PADRAO);
        disciplina.setNome("Introdução à Computação");

        matricula = new Matricula(aluno, disciplina);
    }

    @Test
    @DisplayName("Deve matricular aluno com sucesso")
    void criarAlunoEMatricular() {

        when(alunoRepository.findById(ALUNO_ID)).thenReturn(Optional.of(aluno));
        when(disciplinaRepository.findById(CODIGO_DISCIPLINA_PADRAO)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.existsByAlunoAndDisciplina(aluno, disciplina)).thenReturn(false);
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        Matricula result = matriculaService.matricularAluno(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO);

        assertThat(result).isNotNull();
        assertThat(result.getAluno().getNome()).isEqualTo(NOME_PADRAO);
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Não deve matricular aluno já matriculado na disciplina")
    void criarAlunoEMatricular_jaMatriculado() {
        when(alunoRepository.findById(ALUNO_ID)).thenReturn(Optional.of(aluno));
        when(disciplinaRepository.findById(CODIGO_DISCIPLINA_PADRAO)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.existsByAlunoAndDisciplina(aluno, disciplina)).thenReturn(true);

        assertThatThrownBy(() -> matriculaService.matricularAluno(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Aluno já matriculado nesta disciplina.");
    }

    @Test
    @DisplayName("Deve atribuir nota com sucesso")
    void atribuirNota() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(NOTA_PADRAO);
        avaliacao.setMatricula(matricula);

        when(alunoRepository.findById(ALUNO_ID)).thenReturn(Optional.of(aluno));
        when(disciplinaRepository.findById(CODIGO_DISCIPLINA_PADRAO)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.findByAlunoAndDisciplina(aluno, disciplina)).thenReturn(Optional.of(matricula));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        Avaliacao result = matriculaService.atribuirNota(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO, NOTA_PADRAO);

        assertThat(result).isNotNull();
        assertThat(result.getNota()).isEqualTo(NOTA_PADRAO);
        verify(avaliacaoRepository, times(1)).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("Não deve atribuir nota se aluno não estiver matriculado")
    void atribuirNota_naoMatriculado() {
        when(alunoRepository.findById(ALUNO_ID)).thenReturn(Optional.of(aluno));
        when(disciplinaRepository.findById(CODIGO_DISCIPLINA_PADRAO)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.findByAlunoAndDisciplina(aluno, disciplina)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matriculaService.atribuirNota(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO, NOTA_PADRAO))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Deve listar alunos aprovados")
    void listarAlunosAprovados() {
        when(disciplinaRepository.findById(CODIGO_DISCIPLINA_PADRAO)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.findMatriculasAprovadas(disciplina, NOTA_PADRAO)).thenReturn(List.of(matricula));

        List<Aluno> result = matriculaService.listarAlunosAprovados(CODIGO_DISCIPLINA_PADRAO);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo(NOME_PADRAO);
    }

    @Test
    @DisplayName("Deve listar alunos reprovados")
    void listarAlunosReprovados() {
        when(disciplinaRepository.findById(CODIGO_DISCIPLINA_PADRAO)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.findMatriculasReprovadas(disciplina, NOTA_PADRAO)).thenReturn(List.of(matricula));

        List<Aluno> result = matriculaService.listarAlunosReprovados(CODIGO_DISCIPLINA_PADRAO);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo(NOME_PADRAO);
    }
}
