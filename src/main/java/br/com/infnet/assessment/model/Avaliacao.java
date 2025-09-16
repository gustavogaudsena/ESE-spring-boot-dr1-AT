package br.com.infnet.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Avaliacao {

    @Id
    @GeneratedValue
    private Long id;
    private Disciplina disciplina;
    private Aluno aluno;
    private String observacao;
    private Double nota;
}
