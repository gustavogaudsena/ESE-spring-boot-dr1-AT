package br.com.infnet.assessment.repository;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByAlunoAndDisciplina(Aluno aluno, Disciplina disciplina);

    Optional<Matricula> findByAlunoAndDisciplina(Aluno aluno, Disciplina disciplina);

    @Query("SELECT m FROM Matricula m JOIN m.avaliacoes a WHERE m.disciplina = :disciplina GROUP BY m.aluno HAVING AVG(a.nota) >= :nota" )
    List<Matricula> findMatriculasAprovadas(Disciplina disciplina, Double nota);

    @Query("SELECT m FROM Matricula m JOIN m.avaliacoes a WHERE m.disciplina = :disciplina GROUP BY m.aluno HAVING AVG(a.nota) < :nota")
    List<Matricula> findMatriculasReprovadas(Disciplina disciplina, Double nota);
}
