package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.repository.DisciplinaRepository;
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

class DisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private DisciplinaService disciplinaService;

    private final String CODIGO_DISCIPLINA = "25GRLEDS01BAD101";
    private final String NOME_DISCIPLINA = "Desenvolvimento de Serviços com Spring Boot";

    private Disciplina disciplina;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        disciplina = new Disciplina();
        disciplina.setCodigo(CODIGO_DISCIPLINA);
        disciplina.setNome(NOME_DISCIPLINA);
    }


    @Test
    void listar() {
        int p = 0;
        int s = 5;
        PageRequest pageRequest = PageRequest.of(p, s);
        when(disciplinaRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(disciplina)));
        Page<Disciplina> resultado = disciplinaService.listar(p, s);
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(List.of(disciplina), resultado.stream().toList());
        verify(disciplinaRepository, times(1)).findAll(pageRequest);
    }

   @Test
    void listar_SemPaginacao() {
        int p = 0;
        int s = 10;
        PageRequest pageRequest = PageRequest.of(p, s);
        when(disciplinaRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(disciplina)));
        Page<Disciplina> resultado = disciplinaService.listar(null, null);
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(List.of(disciplina), resultado.stream().toList());
        verify(disciplinaRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void obterPorCodigo() {
        when(disciplinaRepository.findById(disciplina.getCodigo())).thenReturn(Optional.ofNullable(disciplina));
        Optional<Disciplina> resultado = disciplinaService.obterPorCodigo(disciplina.getCodigo());
        assertNotNull(resultado);
        assertEquals(NOME_DISCIPLINA, resultado.map(Disciplina::getNome).orElseThrow());
        verify(disciplinaRepository, times(1)).findById(disciplina.getCodigo());
    }

    @Test
    void criar() {
        when(disciplinaRepository.save(disciplina)).thenReturn(disciplina);
        Disciplina resultado = disciplinaService.criar(disciplina);
        assertNotNull(resultado);
        assertEquals(NOME_DISCIPLINA, resultado.getNome());
        verify(disciplinaRepository, times(1)).save(disciplina);
    }

    @Test
    void atualizar() {
        when(disciplinaRepository.save(disciplina)).thenReturn(disciplina);
        Disciplina resultado = disciplinaService.atualizar(CODIGO_DISCIPLINA, disciplina);
        assertNotNull(resultado);
        assertEquals(NOME_DISCIPLINA, resultado.getNome());
        verify(disciplinaRepository, times(1)).save(disciplina);
    }

    @Test
    void deletar() {
        doNothing().when(disciplinaRepository).deleteById(CODIGO_DISCIPLINA);
        disciplinaService.deletar(CODIGO_DISCIPLINA);
        verify(disciplinaRepository, times(1)).deleteById(CODIGO_DISCIPLINA);
    }
}