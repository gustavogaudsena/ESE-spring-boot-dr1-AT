package br.com.infnet.assessment.controller;

import br.com.infnet.assessment.dto.LoginDTO;
import br.com.infnet.assessment.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginDTO dto) {
        String token = authenticationService.authenticate(dto.username(), dto.password());
        return ResponseEntity.ok().body(new TokenResponse(token));
    }

    private record TokenResponse(String token) {}
}
