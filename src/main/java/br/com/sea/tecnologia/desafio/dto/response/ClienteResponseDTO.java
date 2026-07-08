package br.com.sea.tecnologia.desafio.dto.response;

import br.com.sea.tecnologia.desafio.model.enums.TipoTelefone;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ClienteResponseDTO {
    private UUID id;
    private String nome;
    private String cpf;
    private EnderecoResponseDTO endereco;
    private Set<TelefoneResponseDTO> telefones;
    private Set<String> emails;

    @Data
    @Builder
    public static class TelefoneResponseDTO {
        private TipoTelefone tipo;
        private String numero;
    }
}
