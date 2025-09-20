package br.com.infnet.assessment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarAlunoMatriculaDTO {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;

    private String disciplinaCodigo;
}