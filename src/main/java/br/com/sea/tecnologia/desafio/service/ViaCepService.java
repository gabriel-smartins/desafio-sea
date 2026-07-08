package br.com.sea.tecnologia.desafio.service;

import br.com.sea.tecnologia.desafio.dto.response.ViaCepResponseDTO;

public interface ViaCepService {
    ViaCepResponseDTO buscarPorCep(String cep);
}
