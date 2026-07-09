package br.com.sea.tecnologia.desafio.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EnderecoRequestDTO {

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;

    private String uf;

    private String complemento;
}
