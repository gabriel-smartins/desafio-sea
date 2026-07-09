package br.com.sea.tecnologia.desafio.mapper;

import br.com.sea.tecnologia.desafio.dto.request.ClienteRequestDTO;
import br.com.sea.tecnologia.desafio.dto.request.ClienteUpdateRequestDTO;
import br.com.sea.tecnologia.desafio.dto.request.EnderecoRequestDTO;
import br.com.sea.tecnologia.desafio.dto.response.ClienteResponseDTO;
import br.com.sea.tecnologia.desafio.dto.response.EnderecoResponseDTO;
import br.com.sea.tecnologia.desafio.model.Cliente;
import br.com.sea.tecnologia.desafio.model.Email;
import br.com.sea.tecnologia.desafio.model.Endereco;
import br.com.sea.tecnologia.desafio.model.Telefone;
import br.com.sea.tecnologia.desafio.util.MascaraUtil;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .endereco(toEndereco(dto.getEndereco()))
                .build();

        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(t -> cliente.addTelefone(
                    Telefone.builder().tipo(t.getTipo()).numero(t.getNumero()).build()
            ));
        }

        if (dto.getEmails() != null) {
            dto.getEmails().forEach(e -> cliente.addEmail(
                    Email.builder().email(e.getEmail()).build()
            ));
        }

        return cliente;
    }

    public Cliente toEntity(ClienteUpdateRequestDTO dto) {
        Cliente.ClienteBuilder builder = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .endereco(dto.getEndereco() != null ? toEndereco(dto.getEndereco()) : null);

        if (dto.getTelefones() == null) {
            builder.telefones(null);
        }
        if (dto.getEmails() == null) {
            builder.emails(null);
        }

        Cliente cliente = builder.build();

        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(t -> cliente.addTelefone(
                    Telefone.builder().tipo(t.getTipo()).numero(t.getNumero()).build()
            ));
        }

        if (dto.getEmails() != null) {
            dto.getEmails().forEach(e -> cliente.addEmail(
                    Email.builder().email(e.getEmail()).build()
            ));
        }

        return cliente;

    }

    private Endereco toEndereco(EnderecoRequestDTO dto) {
        return Endereco.builder()
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .uf(dto.getUf())
                .complemento(dto.getComplemento())
                .build();
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(MascaraUtil.aplicarCpf(cliente.getCpf()))
                .endereco(toEnderecoResponseDTO(cliente.getEndereco()))
                .telefones(cliente.getTelefones().stream()
                        .map(t -> ClienteResponseDTO.TelefoneResponseDTO.builder()
                                .tipo(t.getTipo())
                                .numero(MascaraUtil.aplicarTelefone(t.getNumero(), t.getTipo()))
                                .build())
                        .collect(Collectors.toSet()))
                .emails(cliente.getEmails().stream()
                        .map(Email::getEmail)
                        .collect(Collectors.toSet()))
                .build();
    }

    private EnderecoResponseDTO toEnderecoResponseDTO(Endereco endereco) {
        if (endereco == null) return null;
        return EnderecoResponseDTO.builder()
                .cep(MascaraUtil.aplicarCep(endereco.getCep()))
                .logradouro(endereco.getLogradouro())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .uf(endereco.getUf())
                .complemento(endereco.getComplemento())
                .build();
    }
}