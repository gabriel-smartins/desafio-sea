package br.com.sea.tecnologia.desafio.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EnderecoRequestDTO {

    @NotBlank
    @Pattern(regexp = "\\d{5}-?\\d{3}")
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;

    @Pattern(regexp = "[A-Za-z]{2}")
    private String uf;

    private String complemento;
}
