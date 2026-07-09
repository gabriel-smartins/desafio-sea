package br.com.sea.tecnologia.desafio.controller;

import br.com.sea.tecnologia.desafio.controller.docs.ClienteApi;
import br.com.sea.tecnologia.desafio.dto.request.ClienteRequestDTO;
import br.com.sea.tecnologia.desafio.dto.request.ClienteUpdateRequestDTO;
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
public class ClienteController implements ClienteApi {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteRequestDTO request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente saved = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponseDTO(saved));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<ClienteResponseDTO> clientes = clienteService.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable("id") UUID id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(clienteMapper.toResponseDTO(cliente));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @RequestBody @Valid ClienteUpdateRequestDTO request) {
        clienteService.update(id, clienteMapper.toEntity(request));
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}