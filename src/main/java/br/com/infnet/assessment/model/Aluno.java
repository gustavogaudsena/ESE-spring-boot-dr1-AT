package br.com.infnet.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 30, unique = true)
    private String cpf;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 30)
    private String telefone;

    @Column(length = 500)
    private String endereco;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"aluno"})
    private List<Matricula> matriculas;
}
