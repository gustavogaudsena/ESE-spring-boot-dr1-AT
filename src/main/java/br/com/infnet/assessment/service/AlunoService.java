package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Aluno;
import br.com.infnet.assessment.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;

    @Transactional(readOnly = true)
    public Page<Aluno> listar(Integer page, Integer size) {
        int p = page == null ? 0 : Math.max(0, page);
        int s = size == null ? 0 : Math.max(1, size);
        PageRequest pageRequest = PageRequest.of(p, s);

        return repository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Optional<Aluno> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Aluno criar(Aluno aluno) {
        return repository.save(aluno);
    }

    @Transactional
    public Aluno atualizar(Long id, Aluno aluno) {
        aluno.setId(id);
        return repository.save(aluno);
    }

    @Transactional
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
