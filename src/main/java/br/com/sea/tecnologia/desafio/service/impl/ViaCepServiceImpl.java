package br.com.sea.tecnologia.desafio.service.impl;

import br.com.sea.tecnologia.desafio.dto.response.ViaCepResponseDTO;
import br.com.sea.tecnologia.desafio.exception.ExternalServiceUnavailableException;
import br.com.sea.tecnologia.desafio.exception.InvalidCepException;
import br.com.sea.tecnologia.desafio.service.ViaCepService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepServiceImpl implements ViaCepService {

    @Value("${api.via-cep.url}")
    private String VIA_CEP_URL;

    private final RestTemplate restTemplate;

    public ViaCepServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ViaCepResponseDTO buscarPorCep(String cep) {

        try {
            ResponseEntity<ViaCepResponseDTO> response = restTemplate.getForEntity(VIA_CEP_URL, ViaCepResponseDTO.class, cep);

            ViaCepResponseDTO body = response.getBody();

            if (!response.getStatusCode().is2xxSuccessful() || body == null) {
                throw new ExternalServiceUnavailableException("Resposta inválida do serviço de CEP.");
            }

            if (Boolean.TRUE.equals(body.getErro())) {
                throw new InvalidCepException("O CEP informado não existe na base dos Correios.");
            }

            return body;
        } catch (RestClientException e) {
            throw new ExternalServiceUnavailableException("Erro ao comunicar com o serviço do ViaCEP: " + e.getMessage());
        }
    }
}
