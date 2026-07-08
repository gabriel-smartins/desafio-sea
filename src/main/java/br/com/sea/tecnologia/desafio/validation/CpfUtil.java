package br.com.sea.tecnologia.desafio.validation;

import br.com.sea.tecnologia.desafio.util.MascaraUtil;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CpfUtil {

    public static boolean isValido(String cpf) {
        if (cpf == null) {
            return false;
        }

        String digitos = MascaraUtil.remover(cpf);

        if (digitos.length() != 11 || todosDigitosIguais(digitos)) {
            return false;
        }

        int primeiroDigitoVerificador = calcularDigitoVerificador(digitos, 9);
        int segundoDigitoVerificador = calcularDigitoVerificador(digitos, 10);

        return digitos.charAt(9) - '0' == primeiroDigitoVerificador
                && digitos.charAt(10) - '0' == segundoDigitoVerificador;
    }

    private static boolean todosDigitosIguais(String digitos) {
        return digitos.chars().distinct().count() == 1;
    }

    private static int calcularDigitoVerificador(String digitos, int quantidadeDigitosBase) {
        int soma = 0;
        int peso = quantidadeDigitosBase + 1;

        for (int i = 0; i < quantidadeDigitosBase; i++) {
            soma += (digitos.charAt(i) - '0') * peso;
            peso--;
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
