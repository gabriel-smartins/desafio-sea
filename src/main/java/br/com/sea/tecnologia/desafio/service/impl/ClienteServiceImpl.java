package br.com.sea.tecnologia.desafio.service.impl;

import br.com.sea.tecnologia.desafio.dto.response.ViaCepResponseDTO;
import br.com.sea.tecnologia.desafio.exception.BusinessException;
import br.com.sea.tecnologia.desafio.exception.ResourceNotFoundException;
import br.com.sea.tecnologia.desafio.model.Cliente;
import br.com.sea.tecnologia.desafio.model.Email;
import br.com.sea.tecnologia.desafio.model.Endereco;
import br.com.sea.tecnologia.desafio.model.Telefone;
import br.com.sea.tecnologia.desafio.repository.ClienteRepository;
import br.com.sea.tecnologia.desafio.service.ClienteService;
import br.com.sea.tecnologia.desafio.service.ViaCepService;
import br.com.sea.tecnologia.desafio.util.MascaraUtil;
import br.com.sea.tecnologia.desafio.validation.CpfUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ViaCepService viaCepService;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ViaCepService viaCepService) {
        this.clienteRepository = clienteRepository;
        this.viaCepService = viaCepService;
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {

        if (!CpfUtil.isValido(cliente.getCpf())) {
            throw new BusinessException("CPF inválido: " + cliente.getCpf());
        }

        removerMascaras(cliente);

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
        }

        garantirVinculoBidirecional(cliente);
        preencherEnderecoViaCep(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com id: " + id + " não encontrado."));
    }

    @Override
    @Transactional
    public void update(UUID id, Cliente cliente) {
        Cliente clienteExistente = findById(id);

        if (!CpfUtil.isValido(cliente.getCpf())) {
            throw new BusinessException("CPF inválido: " + cliente.getCpf());
        }

        removerMascaras(cliente);

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
        }

        clienteExistente.atualizarDadosBasicos(cliente);

        atualizarTelefones(clienteExistente, cliente.getTelefones());
        atualizarEmails(clienteExistente, cliente.getEmails());

        clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Cliente cliente = findById(id);
        clienteRepository.delete(cliente);
    }

    private void preencherEnderecoViaCep(Cliente cliente) {
        if (cliente.getEndereco() == null || cliente.getEndereco().getCep() == null) {
            return;
        }

        ViaCepResponseDTO dados = viaCepService.buscarPorCep(cliente.getEndereco().getCep());
        Endereco endereco = cliente.getEndereco();

        if (dados.getLogradouro() != null) endereco.setLogradouro(dados.getLogradouro());
        if (dados.getBairro() != null) endereco.setBairro(dados.getBairro());
        if (dados.getLocalidade() != null) endereco.setCidade(dados.getLocalidade());
        if (dados.getUf() != null) endereco.setUf(dados.getUf());
    }

    private void atualizarTelefones(Cliente existente, Set<Telefone> novosTelefones) {
        existente.getTelefones().clear();
        if (novosTelefones != null) {
            novosTelefones.forEach(existente::addTelefone);
        }
    }

    private void atualizarEmails(Cliente existente, Set<Email> novosEmails) {
        existente.getEmails().clear();
        if (novosEmails != null) {
            novosEmails.forEach(existente::addEmail);
        }
    }

    private void garantirVinculoBidirecional(Cliente cliente) {
        if (cliente.getTelefones() != null) {
            new ArrayList<>(cliente.getTelefones()).forEach(cliente::addTelefone);
        }
        if (cliente.getEmails() != null) {
            new ArrayList<>(cliente.getEmails()).forEach(cliente::addEmail);
        }
    }

    private void removerMascaras(Cliente cliente) {
        if (cliente.getCpf() != null) {
            cliente.setCpf(MascaraUtil.remover(cliente.getCpf()));
        }
        if (cliente.getEndereco() != null && cliente.getEndereco().getCep() != null) {
            cliente.getEndereco().setCep(MascaraUtil.remover(cliente.getEndereco().getCep()));
        }
        if (cliente.getTelefones() != null) {
            cliente.getTelefones().forEach(t -> t.setNumero(MascaraUtil.remover(t.getNumero())));
        }
    }
}