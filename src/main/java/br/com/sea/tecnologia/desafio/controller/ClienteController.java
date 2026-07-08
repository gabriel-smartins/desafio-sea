package br.com.sea.tecnologia.desafio.controller;

import br.com.sea.tecnologia.desafio.dto.request.ClienteRequestDTO;
import br.com.sea.tecnologia.desafio.dto.response.ClienteResponseDTO;
import br.com.sea.tecnologia.desafio.mapper.ClienteMapper;
import br.com.sea.tecnologia.desafio.model.Cliente;
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
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteRequestDTO request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente saved = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponseDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<ClienteResponseDTO> clientes = clienteService.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable UUID id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(clienteMapper.toResponseDTO(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody @Valid ClienteRequestDTO request) {
        clienteService.update(id, clienteMapper.toEntity(request));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}