package br.com.sea.tecnologia.desafio.dto.request;

import br.com.sea.tecnologia.desafio.validation.CpfValido;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class ClienteRequestDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$", message = "Permite apenas letras, espaços e números")
    private String nome;

    @CpfValido
    @NotBlank
    private String cpf;

    @NotNull(message = "O endereço é obrigatório")
    @Valid
    private EnderecoRequestDTO endereco;

    @NotEmpty(message = "Pelo menos um telefone deve ser cadastrado")
    @Valid
    private Set<TelefoneRequestDTO> telefones;

    @NotEmpty(message = "Pelo menos um e-mail deve ser cadastrado")
    private Set<EmailRequestDTO> emails;
}