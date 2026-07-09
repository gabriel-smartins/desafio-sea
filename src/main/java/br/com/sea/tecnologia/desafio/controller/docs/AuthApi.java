package br.com.sea.tecnologia.desafio.controller.docs;

import br.com.sea.tecnologia.desafio.dto.request.LoginRequestDTO;
import br.com.sea.tecnologia.desafio.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Endpoints para gerenciamento de acesso e tokens JWT")
public interface AuthApi {
    @Operation(
            summary = "Efetuar login",
            description = "Autentica as credenciais do usuário e devolve um token JWT válido.\n\n" +
                    "**Credenciais pré-definidas para teste:**\n" +
                    "* **Administrador:** `admin` / `123qwe!@#` (Acesso total)\n" +
                    "* **Usuário Padrão:** `user` / `123qwe123` (Acesso apenas para leitura)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso e token gerado"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário não encontrado")
    })
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDTO request);
}
