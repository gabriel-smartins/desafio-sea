package br.com.sea.tecnologia.desafio.controller.response;

import br.com.sea.tecnologia.desafio.model.Endereco;
import br.com.sea.tecnologia.desafio.model.enums.TipoTelefone;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ClienteResponseDTO {
    private UUID id;
    private String nome;
    private String cpf;
    private Endereco endereco;
    private List<TelefoneResponseDTO> telefones;
    private List<String> emails;

    @Data
    @Builder
    public static class TelefoneResponseDTO {
        private TipoTelefone tipo;
        private String numero;
    }
}
