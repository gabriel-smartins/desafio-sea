package br.com.sea.tecnologia.desafio.util;

import br.com.sea.tecnologia.desafio.model.enums.TipoTelefone;

public class MascaraUtil {

    private MascaraUtil() {}

    public static String remover(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    public static String aplicarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String aplicarCep(String cep) {
        if (cep == null || cep.length() != 8) return cep;
        return cep.replaceFirst("(\\d{5})(\\d{3})", "$1-$2");
    }

    public static String aplicarTelefone(String numero, TipoTelefone tipo) {
        if (numero == null || tipo == null) return numero;
        int digitos = tipo.getQuantidadeDigitos();

        if (digitos == 11 && numero.length() == 11) {
            return numero.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (digitos == 10 && numero.length() == 10) {
            return numero.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return numero;
    }
}