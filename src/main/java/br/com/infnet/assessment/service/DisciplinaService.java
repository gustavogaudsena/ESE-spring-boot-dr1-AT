package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository;

    public List<Disciplina> findAll() {
        return disciplinaRepository.findAll();
    }

    public Optional<Disciplina> findById(String codigo) {
        return disciplinaRepository.findById(codigo);
    }

    public Disciplina save(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    public void deleteById(String codigo) {
        disciplinaRepository.deleteById(codigo);
    }
}
