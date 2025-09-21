package br.com.infnet.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Avaliacao {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_id")
    @JsonIgnore
    private Matricula matricula;

    @Column(nullable = false)
    private Double nota;
}
