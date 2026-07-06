package br.com.sea.tecnologia.desafio.config;

import br.com.sea.tecnologia.desafio.model.Usuario;
import br.com.sea.tecnologia.desafio.model.enums.UsuarioRole;
import br.com.sea.tecnologia.desafio.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {

            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123qwe!@#"))
                    .role(UsuarioRole.ADMIN)
                    .build();

            Usuario padrao = Usuario.builder()
                    .username("user")
                    .password(passwordEncoder.encode("123qwe123"))
                    .role(UsuarioRole.USER)
                    .build();

            usuarioRepository.saveAll(Arrays.asList(admin, padrao));
            System.out.println("Usuários de teste inseridos com sucesso!");
        }
    }
}