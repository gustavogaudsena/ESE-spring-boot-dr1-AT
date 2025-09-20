package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.model.Avaliacao;
import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.model.Matricula;
import br.com.infnet.assessment.repository.AlunoRepository;
import br.com.infnet.assessment.repository.AlunoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoServiceTest {


    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    private final Random random = new Random();
    private final Long ALUNO_ID = random.nextLong();
    private final String NOME_PADRAO = "Gustavo";
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
    }


    @Test
    void listar() {
        int p = 0;
        int s = 5;
        PageRequest pageRequest = PageRequest.of(p, s);
        when(alunoRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(aluno)));
        Page<Aluno> resultado = alunoService.listar(p, s);
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(List.of(aluno), resultado.stream().toList());
        verify(alunoRepository, times(1)).findAll(pageRequest);
    }


    @Test
    void obterPorCodigo() {
        when(alunoRepository.findById(ALUNO_ID)).thenReturn(Optional.ofNullable(aluno));
        Optional<Aluno> resultado = alunoService.obterPorId(ALUNO_ID);
        assertNotNull(resultado);
        assertEquals(NOME_PADRAO, resultado.map(Aluno::getNome).orElseThrow());
        verify(alunoRepository, times(1)).findById(ALUNO_ID);
    }

    @Test
    void criar() {
        when(alunoRepository.save(aluno)).thenReturn(aluno);
        Aluno resultado = alunoService.criar(aluno);
        assertNotNull(resultado);
        assertEquals(NOME_PADRAO, resultado.getNome());
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void atualizar() {
        when(alunoRepository.save(aluno)).thenReturn(aluno);
        Aluno resultado = alunoService.atualizar(ALUNO_ID, aluno);
        assertNotNull(resultado);
        assertEquals(NOME_PADRAO, resultado.getNome());
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void deletar() {
        doNothing().when(alunoRepository).deleteById(ALUNO_ID);
        alunoService.deletar(ALUNO_ID);
        verify(alunoRepository, times(1)).deleteById(ALUNO_ID);
    }
}