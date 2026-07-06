package br.com.sea.tecnologia.desafio.controller.request;

import br.com.sea.tecnologia.desafio.model.Endereco;
import br.com.sea.tecnologia.desafio.model.enums.TipoTelefone;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
public class ClienteRequestDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$", message = "Permite apenas letras, espaços e números")
    private String nome;

    @NotBlank
    private String cpf;

    @Valid
    @NotNull
    private Endereco endereco;

    @NotEmpty(message = "Pelo menos um telefone deve ser cadastrado")
    private List<TelefoneRequestDTO> telefones;

    @NotEmpty(message = "Pelo menos um e-mail deve ser cadastrado")
    private List<EmailRequestDTO> emails;

    @Data
    public static class TelefoneRequestDTO {
        @NotNull
        private TipoTelefone tipo;
        @NotBlank
        private String numero;
    }

    @Data
    public static class EmailRequestDTO {
        @NotBlank
        @Email
        private String endereco;
    }
}