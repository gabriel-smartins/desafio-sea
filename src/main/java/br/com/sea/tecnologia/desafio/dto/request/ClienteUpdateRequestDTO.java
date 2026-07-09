package br.com.sea.tecnologia.desafio.dto.request;

import br.com.sea.tecnologia.desafio.validation.CpfValido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ClienteUpdateRequestDTO {

    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Pattern(regexp = "[\\p{L}0-9 ]+", message = "Nome permite apenas letras, espaços e números")
    private String nome;

    @CpfValido
    private String cpf;

    @Valid
    private EnderecoRequestDTO endereco;

    @Valid
    @Size(min = 1, message = "Se enviado, ao menos um telefone deve ser informado")
    private List<TelefoneRequestDTO> telefones;

    @Size(min = 1, message = "Se enviado, ao menos um e-mail deve ser informado")
    private List<EmailRequestDTO> emails;
}