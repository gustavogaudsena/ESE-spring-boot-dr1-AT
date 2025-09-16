package br.com.infnet.assessment.repository;

import br.com.infnet.assessment.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends  JpaRepository<Aluno, Long> {}

