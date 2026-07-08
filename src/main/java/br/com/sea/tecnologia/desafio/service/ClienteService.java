package br.com.sea.tecnologia.desafio.service;

import br.com.sea.tecnologia.desafio.model.Cliente;

import java.util.List;
import java.util.UUID;

public interface ClienteService {

    Cliente save(Cliente cliente);

    List<Cliente> findAll();

    Cliente findById(UUID id);

    void update(UUID id, Cliente cliente);

    void delete(UUID id);
}
