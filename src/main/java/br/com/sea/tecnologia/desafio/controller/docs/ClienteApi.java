package br.com.sea.tecnologia.desafio.controller.docs;

import br.com.sea.tecnologia.desafio.dto.request.ClienteRequestDTO;
import br.com.sea.tecnologia.desafio.dto.request.ClienteUpdateRequestDTO;
import br.com.sea.tecnologia.desafio.dto.response.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "Clientes", description = "Endpoints para gerenciamento do cadastro de clientes (Requer autenticação JWT)")
public interface ClienteApi {

    @Operation(summary = "Cadastrar um novo cliente", description = "Cria um novo cliente na base de dados. Preenche automaticamente o endereço via integração com ViaCEP. (Requer permissão de ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação, CPF duplicado, inválido ou CEP não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão de ADMIN"),
            @ApiResponse(responseCode = "502", description = "Falha de integração com o serviço do ViaCEP")
    })
    ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteRequestDTO request);

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não está autenticado")
    })
    ResponseEntity<List<ClienteResponseDTO>> findAll();

    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico baseado no seu identificador único (UUID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado na base de dados")
    })
    ResponseEntity<ClienteResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou CPF pertencente a outro cliente"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado na base de dados")
    })
    ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody @Valid ClienteUpdateRequestDTO request);

    @Operation(summary = "Excluir cliente", description = "Remove um cliente da base de dados através do seu identificador único (UUID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado na base de dados")
    })
    ResponseEntity<Void> delete(@PathVariable UUID id);
}
