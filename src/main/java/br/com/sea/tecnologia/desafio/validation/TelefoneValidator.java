package br.com.sea.tecnologia.desafio.validation;

import br.com.sea.tecnologia.desafio.dto.request.TelefoneRequestDTO;
import br.com.sea.tecnologia.desafio.util.MascaraUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<TelefoneValido, TelefoneRequestDTO> {

    @Override
    public boolean isValid(TelefoneRequestDTO telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.getTipo() == null || telefone.getNumero() == null) {
            return true;
        }

        String digitos = MascaraUtil.remover(telefone.getNumero());
        return digitos.length() == telefone.getTipo().getQuantidadeDigitos();
    }
}