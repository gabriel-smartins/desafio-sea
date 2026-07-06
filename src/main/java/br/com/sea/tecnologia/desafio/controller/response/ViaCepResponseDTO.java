package br.com.sea.tecnologia.desafio.controller.response;

import lombok.Data;

@Data
public class ViaCepResponseDTO {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private Boolean erro;
}
