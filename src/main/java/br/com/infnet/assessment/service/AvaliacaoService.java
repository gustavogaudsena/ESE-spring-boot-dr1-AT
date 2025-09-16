package br.com.infnet.assessment.service;

import br.com.infnet.assessment.model.Avaliacao;
import br.com.infnet.assessment.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;

    public List<Avaliacao> findAll() {
        return avaliacaoRepository.findAll();
    }

    public Optional<Avaliacao> findById(Long id) {
        return avaliacaoRepository.findById(id);
    }

    public Avaliacao save(Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    public void deleteById(Long id) {
        avaliacaoRepository.deleteById(id);
    }
}
