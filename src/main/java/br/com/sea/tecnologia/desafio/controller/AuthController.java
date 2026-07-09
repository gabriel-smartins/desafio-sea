package br.com.sea.tecnologia.desafio.controller;

import br.com.sea.tecnologia.desafio.controller.docs.AuthApi;
import br.com.sea.tecnologia.desafio.dto.request.LoginRequestDTO;
import br.com.sea.tecnologia.desafio.dto.response.AuthResponse;
import br.com.sea.tecnologia.desafio.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDTO request) {

        UsernamePasswordAuthenticationToken login =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(login);

        String token = tokenService.generateToken(authentication.getName());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .role(authentication.getAuthorities().iterator().next().getAuthority())
                .build());
    }
}
