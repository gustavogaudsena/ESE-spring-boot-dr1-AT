package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.model.Professor;
import br.com.infnet.assessment.repository.AlunoRepository;
import br.com.infnet.assessment.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository repository;

    @Transactional
    public Professor criar(Professor professor) {
        return repository.save(professor);
    }

}
