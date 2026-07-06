package br.com.sea.tecnologia.desafio.service;

import br.com.sea.tecnologia.desafio.controller.response.ViaCepResponseDTO;
import br.com.sea.tecnologia.desafio.exception.InvalidCepException;
import br.com.sea.tecnologia.desafio.exception.ExternalServiceUnavailableException;
import br.com.sea.tecnologia.desafio.exception.ResourceNotFoundException;
import br.com.sea.tecnologia.desafio.model.Cliente;
import br.com.sea.tecnologia.desafio.model.Email;
import br.com.sea.tecnologia.desafio.model.Telefone;
import br.com.sea.tecnologia.desafio.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final RestTemplate restTemplate;

    public ClienteService(ClienteRepository clienteRepository, RestTemplate restTemplate) {
        this.clienteRepository = clienteRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        removerMascaras(cliente);
        garantirVinculoBidirecional(cliente);

        preencherEnderecoViaCep(cliente);

        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente findById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com id: " + id + " não encontrado."));
    }

    @Transactional()
    public void update(UUID id, Cliente cliente) {
        Cliente clienteExistente = findById(id);

        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setCpf(cliente.getCpf());
        clienteExistente.setEndereco(cliente.getEndereco());

        atualizarTelefones(clienteExistente, cliente.getTelefones());
        atualizarEmails(clienteExistente, cliente.getEmails());

        clienteRepository.save(clienteExistente);
    }

    @Transactional()
    public void delete(UUID id) {
        Cliente cliente = findById(id);

        clienteRepository.delete(cliente);
    }

    private void preencherEnderecoViaCep(Cliente cliente) {
        if (cliente.getEndereco() != null && cliente.getEndereco().getCep() != null) {
            String cepLimpo = cliente.getEndereco().getCep();
            String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";

            try {
                ResponseEntity<ViaCepResponseDTO> response = restTemplate.getForEntity(url, ViaCepResponseDTO.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    ViaCepResponseDTO viaCepData = response.getBody();

                    if (Boolean.TRUE.equals(viaCepData.getErro())) {
                        throw new InvalidCepException("O CEP informado não existe na base dos Correios.");
                    }

                    cliente.getEndereco().setLogradouro(viaCepData.getLogradouro());
                    cliente.getEndereco().setBairro(viaCepData.getBairro());
                    cliente.getEndereco().setCidade(viaCepData.getLocalidade());
                    cliente.getEndereco().setUf(viaCepData.getUf());
                }
            } catch (RestClientException e) {
                throw new ExternalServiceUnavailableException("Erro ao comunicar com o serviço do ViaCEP: " + e.getMessage());
            }

        }
    }

    private void atualizarTelefones(Cliente existente, List<Telefone> novosTelefones) {
        existente.getTelefones().clear();
        if (novosTelefones != null) {
            novosTelefones.forEach(existente::addTelefone);
        }
    }

    private void atualizarEmails(Cliente existente, List<Email> novosEmails) {
        existente.getEmails().clear();
        if (novosEmails != null) {
            novosEmails.forEach(existente::addEmail);
        }
    }

    private void garantirVinculoBidirecional(Cliente cliente) {
        if (cliente.getTelefones() != null) {
            cliente.getTelefones().forEach(t -> t.setCliente(cliente));
        }
        if (cliente.getEmails() != null) {
            cliente.getEmails().forEach(e -> e.setCliente(cliente));
        }
    }

    private void removerMascaras(Cliente cliente) {
        if (cliente.getCpf() != null) {
            cliente.setCpf(cliente.getCpf().replaceAll("\\D", ""));
        }
        if (cliente.getEndereco() != null && cliente.getEndereco().getCep() != null) {
            cliente.getEndereco().setCep(cliente.getEndereco().getCep().replaceAll("\\D", ""));
        }
        if (cliente.getTelefones() != null) {
            cliente.getTelefones().forEach(t -> {
                if (t.getNumero() != null) {
                    t.setNumero(t.getNumero().replaceAll("\\D", ""));
                }
            });
        }
    }

}
