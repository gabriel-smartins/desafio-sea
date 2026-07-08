package br.com.sea.tecnologia.desafio.dto.request;

import br.com.sea.tecnologia.desafio.model.enums.TipoTelefone;
import br.com.sea.tecnologia.desafio.validation.TelefoneValido;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TelefoneValido
public class TelefoneRequestDTO {

    @NotNull(message = "Tipo de telefone é obrigatório")
    private TipoTelefone tipo;

    @NotBlank(message = "Número é obrigatório")
    private String numero;
}
