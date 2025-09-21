package br.com.infnet.assessment.security;

import br.com.infnet.assessment.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessorDetailsService implements UserDetailsService {

    private final ProfessorRepository professorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return professorRepository.findByUsername(username)
                .map(professor -> new ProfessorAuthenticated(professor))
                .orElseThrow(() -> new UsernameNotFoundException("Professor não encontrado com o nome de usuário " + username));
    }
}
