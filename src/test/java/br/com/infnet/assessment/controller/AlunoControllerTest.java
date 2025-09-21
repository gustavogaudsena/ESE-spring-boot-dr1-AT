package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.config.TestSecurityConfig;
import br.com.infnet.assessment.dto.AtribuirNotaDTO;
import br.com.infnet.assessment.dto.CriarAlunoMatriculaDTO;
import br.com.infnet.assessment.dto.MatricularAlunoDTO;
import br.com.infnet.assessment.model.*;
import br.com.infnet.assessment.repository.ProfessorRepository;
import br.com.infnet.assessment.service.AlunoService;
import br.com.infnet.assessment.service.MatriculaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlunoController.class)
@Import(TestSecurityConfig.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @MockBean
    private MatriculaService matriculaService;

    @MockBean
    private ProfessorRepository professorRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    private final Random random = new Random();
    private final Long ALUNO_ID = random.nextLong();
    private final Long MATRICULA_ID = random.nextLong();
    private final Long AVALIACAO_ID = random.nextLong();
    private final Double NOTA_PADRAO = 7d;
    private final String NOME_PADRAO = "Gustavo";
    private final String CODIGO_DISCIPLINA_PADRAO = "25GRLEDS01BAD101";
    private final String CPF_PADRAO = "123.456.789-00";

    private Aluno aluno;
    private Matricula matricula;
    private Avaliacao avaliacao;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        aluno = new Aluno();
        aluno.setId(ALUNO_ID);
        aluno.setNome(NOME_PADRAO);
        aluno.setCpf(CPF_PADRAO);
        matricula = new Matricula();
        matricula.setId(MATRICULA_ID);
        matricula.setAluno(aluno);
        avaliacao = new Avaliacao();
        avaliacao.setId(AVALIACAO_ID);
        avaliacao.setMatricula(matricula);
        avaliacao.setNota(NOTA_PADRAO);

        objectMapper = new ObjectMapper();
    }

    @Test
    void listar() throws Exception {
        int p = 0;
        int s = 5;
        when(alunoService.listar(p, s)).thenReturn(new PageImpl<>(List.of(aluno)));
        mockMvc.perform(get("/aluno")
                        .header("X-PAGE", p)
                        .header("X-SIZE", s))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value(NOME_PADRAO));

        verify(alunoService, times(1)).listar(p, s);
    }

    @Test
    void obterPorId() throws Exception {
        Long id = aluno.getId();
        when(alunoService.obterPorId(id)).thenReturn(Optional.ofNullable(aluno));

        mockMvc.perform(get("/aluno/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(NOME_PADRAO));

        verify(alunoService, times(1)).obterPorId(id);
    }

    @Test
    void listarAprovados() throws Exception {
        when(matriculaService.listarAlunosAprovados("25GRLEDS01BAD101")).thenReturn(List.of(aluno));
        mockMvc.perform(get("/aluno/aprovados").param("disciplina", CODIGO_DISCIPLINA_PADRAO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value(NOME_PADRAO));

        verify(matriculaService, times(1)).listarAlunosAprovados(CODIGO_DISCIPLINA_PADRAO);
    }

    @Test
    void listarReprovados() throws Exception {
        when(matriculaService.listarAlunosReprovados("25GRLEDS01BAD101")).thenReturn(List.of(aluno));
        mockMvc.perform(get("/aluno/reprovados").param("disciplina", CODIGO_DISCIPLINA_PADRAO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value(NOME_PADRAO));

        verify(matriculaService, times(1)).listarAlunosReprovados(CODIGO_DISCIPLINA_PADRAO);
    }

    @Test
    void criarAlunoEMatricular() throws Exception {
        CriarAlunoMatriculaDTO dto = new CriarAlunoMatriculaDTO();
        dto.setNome(NOME_PADRAO);
        dto.setCpf(CPF_PADRAO);
        dto.setDisciplinaCodigo(CODIGO_DISCIPLINA_PADRAO);

        when(matriculaService.criarAlunoEMatricular(any(CriarAlunoMatriculaDTO.class))).thenReturn(aluno);
        mockMvc.perform(post("/aluno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(NOME_PADRAO));

        verify(matriculaService, times(1)).criarAlunoEMatricular(any(CriarAlunoMatriculaDTO.class));
    }

    @Test
    void matricularAluno() throws Exception {
        MatricularAlunoDTO dto = new MatricularAlunoDTO();
        dto.setDisciplinaCodigo(CODIGO_DISCIPLINA_PADRAO);

        when(matriculaService.matricularAluno(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO)).thenReturn(matricula);
        mockMvc.perform(post("/aluno/" + ALUNO_ID + "/matricula")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.aluno.nome").value(NOME_PADRAO));

        verify(matriculaService, times(1)).matricularAluno(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO);
    }

    @Test
    void atribuirNota() throws Exception {
        AtribuirNotaDTO dto = new AtribuirNotaDTO();
        dto.setDisciplinaCodigo(CODIGO_DISCIPLINA_PADRAO);
        dto.setNota(NOTA_PADRAO);

        when(matriculaService.atribuirNota(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO, NOTA_PADRAO)).thenReturn(avaliacao);
        mockMvc.perform(post("/aluno/" + ALUNO_ID + "/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(AVALIACAO_ID));

        verify(matriculaService, times(1)).atribuirNota(ALUNO_ID, CODIGO_DISCIPLINA_PADRAO, NOTA_PADRAO);
    }

    @Test
    void atualizar() throws Exception {
        when(alunoService.atualizar(eq(ALUNO_ID), any(Aluno.class))).thenReturn(aluno);

        mockMvc.perform(put("/aluno/" + ALUNO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aluno)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(aluno.getNome()));

        verify(alunoService, times(1)).atualizar(eq(ALUNO_ID), any(Aluno.class));
    }

    @Test
    void deletar() throws Exception {
        doNothing().when(alunoService).deletar(ALUNO_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/aluno/" + ALUNO_ID))
                .andExpect(status().isNoContent());

        verify(alunoService, times(1)).deletar(ALUNO_ID);
    }
}