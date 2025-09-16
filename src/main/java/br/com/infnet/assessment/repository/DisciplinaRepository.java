package br.com.infnet.assessment.repository;

import br.com.infnet.assessment.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, String> {
}
