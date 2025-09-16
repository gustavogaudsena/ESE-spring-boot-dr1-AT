package br.com.infnet.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter@Setter
public class Aluno {

    @Id@GeneratedValue
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private int telefone;
    private String endereco;
    private List<Disciplina> disciplinas;
}
