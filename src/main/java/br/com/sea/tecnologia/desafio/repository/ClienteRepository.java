package br.com.sea.tecnologia.desafio.repository;

import br.com.sea.tecnologia.desafio.model.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, UUID> {

    List<Cliente> findAll();
}
