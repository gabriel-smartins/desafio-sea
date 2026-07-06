package br.com.sea.tecnologia.desafio.controller;

import br.com.sea.tecnologia.desafio.controller.request.ClienteRequestDTO;
import br.com.sea.tecnologia.desafio.controller.response.ClienteResponseDTO;
import br.com.sea.tecnologia.desafio.model.Cliente;
import br.com.sea.tecnologia.desafio.model.Email;
import br.com.sea.tecnologia.desafio.model.Telefone;
import br.com.sea.tecnologia.desafio.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteRequestDTO request) {
        Cliente cliente = toEntity(request);
        Cliente saved = clienteService.save(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<ClienteResponseDTO> clientes = clienteService.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable UUID id) {
        Cliente cliente = clienteService.findById(id);

        return ResponseEntity.ok(toResponseDTO(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid ClienteRequestDTO request) {
        clienteService.update(id, toEntity(request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clienteService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .endereco(dto.getEndereco())
                .build();

        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(t -> cliente.addTelefone(
                    Telefone.builder().tipo(t.getTipo()).numero(t.getNumero()).build()
            ));
        }

        if (dto.getEmails() != null) {
            dto.getEmails().forEach(e -> cliente.addEmail(
                    Email.builder().endereco(e.getEndereco()).build()
            ));
        }
        return cliente;
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {

        if (cliente.getEndereco() != null && cliente.getEndereco().getCep() != null) {
            String cep = cliente.getEndereco().getCep();
            if (cep.length() == 8) {
                cliente.getEndereco().setCep(cep.replaceFirst("(\\d{5})(\\d{3})", "$1-$2"));
            }
        }

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(formatarCpf(cliente.getCpf()))
                .endereco(cliente.getEndereco())
                .telefones(cliente.getTelefones().stream()
                        .map(t -> ClienteResponseDTO.TelefoneResponseDTO.builder()
                                .tipo(t.getTipo())
                                .numero(formatarTelefone(t.getNumero(), t.getTipo().getQuantidadeDigitos()))
                                .build())
                        .collect(Collectors.toList()))
                .emails(cliente.getEmails().stream()
                        .map(Email::getEndereco)
                        .collect(Collectors.toList()))
                .build();
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String numero, int digitos) {
        if (numero == null) return numero;
        if (digitos == 11 && numero.length() == 11) {
            return numero.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        } else if (digitos == 10 && numero.length() == 10) { // Fixo
            return numero.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return numero;
    }
}
