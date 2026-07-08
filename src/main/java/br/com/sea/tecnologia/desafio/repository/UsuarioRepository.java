package br.com.sea.tecnologia.desafio.repository;

import br.com.sea.tecnologia.desafio.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, UUID> {

    Optional<Usuario> findByUsername(String username);
}
