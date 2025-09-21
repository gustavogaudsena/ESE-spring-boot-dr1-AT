package br.com.infnet.assessment;

import br.com.infnet.assessment.model.Professor;
import br.com.infnet.assessment.repository.ProfessorRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(ProfessorRepository repo, PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "flavio";
            if (repo.findByUsername(username).isEmpty()) {
                Professor p = new Professor();
                p.setUsername(username);
                p.setPassword(passwordEncoder.encode("123456"));
                repo.save(p);
                System.out.println("Professor criado: " + username + " / password");
            }
            System.out.println(passwordEncoder.encode("password"));
        };
    }

}
