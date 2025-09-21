package br.com.infnet.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Disciplina {

    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Matricula> matriculas;
}
