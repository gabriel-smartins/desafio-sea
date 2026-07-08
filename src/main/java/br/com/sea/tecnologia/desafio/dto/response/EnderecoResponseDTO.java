package br.com.sea.tecnologia.desafio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnderecoResponseDTO {
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private String complemento;
}