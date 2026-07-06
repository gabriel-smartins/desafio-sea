package br.com.sea.tecnologia.desafio.model.enums;

import lombok.Getter;

@Getter
public enum TipoTelefone {

    CELULAR(11),
    COMERCIAL(10),
    RESIDENCIAL(10);

    private final int quantidadeDigitos;

    TipoTelefone(int quantidadeDigitos) {
        this.quantidadeDigitos = quantidadeDigitos;
    }

}
