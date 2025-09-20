package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Disciplina;
import br.com.infnet.assessment.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository repository;

    @Transactional(readOnly = true)
    public Page<Disciplina> listar(Integer page, Integer size) {
        int p = page == null ? 0 : Math.max(0, page);
        int s = size == null ? 0 : Math.max(1, size);
        PageRequest pageRequest = PageRequest.of(p, s);

        return repository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Optional<Disciplina> obterPorCodigo(String codigo) {
        return repository.findById(codigo);
    }

    @Transactional
    public Disciplina criar(Disciplina disciplina) {
        return repository.save(disciplina);
    }

    @Transactional
    public Disciplina atualizar(String codigo, Disciplina disciplina) {
        disciplina.setCodigo(codigo);
        return repository.save(disciplina);
    }

    @Transactional
    public void deletar(String codigo) {
        repository.deleteById(codigo);
    }
}
