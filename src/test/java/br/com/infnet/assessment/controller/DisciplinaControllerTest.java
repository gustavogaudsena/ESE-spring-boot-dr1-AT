package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.service.DisciplinaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DisciplinaController.class)
class DisciplinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisciplinaService disciplinaService;

    private final Random random = new Random();
    private final String CODIGO_DISCIPLINA = "25GRLEDS01BAD101";
    private final String NOME_DISCIPLINA = "Desenvolvimento de Serviços com Spring Boot";

    private Disciplina disciplina;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        disciplina = new Disciplina();
        disciplina.setCodigo(CODIGO_DISCIPLINA);
        disciplina.setNome(NOME_DISCIPLINA);

        objectMapper = new ObjectMapper();
    }


    @Test
    void listar() throws Exception {
        int p = 0;
        int s = 5;
        PageRequest pageRequest = PageRequest.of(p, s);
        when(disciplinaService.listar(p, s)).thenReturn(new PageImpl<>(List.of(disciplina)));

        mockMvc.perform(get("/disciplina")
                        .header("X-PAGE", p)
                        .header("X-SIZE", s)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(disciplinaService, times(1)).listar(p, s);
    }

    @Test
    void obterPorCodigo() throws Exception {
        when(disciplinaService.obterPorCodigo(CODIGO_DISCIPLINA)).thenReturn(Optional.ofNullable(disciplina));

        mockMvc.perform(get("/disciplina/" + CODIGO_DISCIPLINA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(disciplina.getNome()));

        verify(disciplinaService, times(1)).obterPorCodigo(CODIGO_DISCIPLINA);
    }

    @Test
    void criar() throws Exception {
        when(disciplinaService.criar(any(Disciplina.class))).thenReturn(disciplina);

        mockMvc.perform(post("/disciplina")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disciplina)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(disciplina.getNome()));

        verify(disciplinaService, times(1)).criar(any(Disciplina.class));
    }

    @Test
    void atualizar() throws Exception {
        when(disciplinaService.atualizar(eq(CODIGO_DISCIPLINA), any(Disciplina.class))).thenReturn(disciplina);

        mockMvc.perform(put("/disciplina/" + CODIGO_DISCIPLINA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disciplina)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(disciplina.getNome()));

        verify(disciplinaService, times(1)).atualizar(eq(CODIGO_DISCIPLINA), any(Disciplina.class));
    }

    @Test
    void deletar() throws Exception {
        doNothing().when(disciplinaService).deletar(eq(CODIGO_DISCIPLINA));

        mockMvc.perform(MockMvcRequestBuilders.delete("/disciplina/" + CODIGO_DISCIPLINA))
                .andExpect(status().isNoContent());

        verify(disciplinaService, times(1)).deletar(eq(CODIGO_DISCIPLINA));
    }
}