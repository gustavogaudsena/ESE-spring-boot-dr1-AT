package br.com.infnet.assessment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "professor")
public class Professor {
    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;
}
