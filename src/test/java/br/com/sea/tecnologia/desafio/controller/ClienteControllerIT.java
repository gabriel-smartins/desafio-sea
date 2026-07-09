package br.com.sea.tecnologia.desafio.controller;

import br.com.sea.tecnologia.desafio.dto.response.ViaCepResponseDTO;
import br.com.sea.tecnologia.desafio.exception.ExternalServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"JWT_SECRET=meu-segredo-de-teste-12345"})
@AutoConfigureMockMvc
@Transactional
public class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveCriarClienteComSucessoEAplicarMascara() throws Exception {

        ViaCepResponseDTO viaCepMock = new ViaCepResponseDTO();
        viaCepMock.setLogradouro("Praça da Sé");
        viaCepMock.setBairro("Sé");
        viaCepMock.setLocalidade("São Paulo");
        viaCepMock.setUf("SP");

        when(restTemplate.getForEntity(anyString(), any(), anyString())).thenReturn(ResponseEntity.ok(viaCepMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"529.982.247-25\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"email\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Gabriel Martins"))
                .andExpect(jsonPath("$.cpf").value("529.982.247-25"))
                .andExpect(jsonPath("$.endereco.logradouro").value("Praça da Sé"));
    }

    @Test
    @WithMockUser(username = "user")
    public void deveRetornarOk200QuandoUsuarioPadraoTentarApenasLerDados() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void devePreservarDadosInformadosNoEnderecoQuandoViaCepForConsultado() throws Exception {

        ViaCepResponseDTO viaCepMock = new ViaCepResponseDTO();
        viaCepMock.setLogradouro("Praça da Sé");
        viaCepMock.setBairro("Sé");
        viaCepMock.setLocalidade("São Paulo");
        viaCepMock.setUf("SP");

        when(restTemplate.getForEntity(anyString(), any(), anyString())).thenReturn(ResponseEntity.ok(viaCepMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"529.982.247-25\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\", \"logradouro\": \"Rua Personalizada\", \"bairro\": \"Bairro Personalizado\", \"cidade\": \"Cidade Personalizada\", \"uf\": \"DF\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"email\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.endereco.logradouro").value("Rua Personalizada"))
                .andExpect(jsonPath("$.endereco.bairro").value("Bairro Personalizado"))
                .andExpect(jsonPath("$.endereco.cidade").value("Cidade Personalizada"))
                .andExpect(jsonPath("$.endereco.uf").value("DF"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveRetornarBadRequestQuandoEnderecoObrigatorioEstiverIncompleto() throws Exception {
        ViaCepResponseDTO viaCepMock = new ViaCepResponseDTO();
        viaCepMock.setLogradouro(null);
        viaCepMock.setBairro(null);
        viaCepMock.setLocalidade(null);
        viaCepMock.setUf(null);

        when(restTemplate.getForEntity(anyString(), any(), anyString())).thenReturn(ResponseEntity.ok(viaCepMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"529.982.247-25\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\", \"logradouro\": \"\", \"bairro\": \"\", \"cidade\": \"\", \"uf\": \"\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"email\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Preencha todos os campos do endereço."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveRetornarBadRequestQuandoCpfForInvalido() throws Exception {
        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"111.111.111-11\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"email\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveRetornarErroQuandoTentarCadastrarCpfDuplicado() throws Exception {

        ViaCepResponseDTO viaCepMock = new ViaCepResponseDTO();
        viaCepMock.setLogradouro("Rua Teste");
        viaCepMock.setBairro("Sé");
        viaCepMock.setLocalidade("São Paulo");
        viaCepMock.setUf("SP");

        when(restTemplate.getForEntity(anyString(), any(), anyString())).thenReturn(ResponseEntity.ok(viaCepMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Cliente Teste Duplicado\",\n" +
                "  \"cpf\": \"529.982.247-25\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 99999-9999\" } ],\n" +
                "  \"emails\": [ { \"email\": \"teste@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de négocio"))
                .andExpect(jsonPath("$.message").value("Já existe um cliente cadastrado com este CPF."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveRetornarBadRequestQuandoCepNaoExistirNoViaCep() throws Exception {

        ViaCepResponseDTO viaCepErroMock = new ViaCepResponseDTO();
        viaCepErroMock.setErro(true);

        when(restTemplate.getForEntity(anyString(), any(), anyString())).thenReturn(ResponseEntity.ok(viaCepErroMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"529.982.247-25\",\n" +
                "  \"endereco\": { \"cep\": \"99999-999\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"email\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("CEP Inválido"))
                .andExpect(jsonPath("$.message").value("O CEP informado não existe na base dos Correios."))
                .andExpect(jsonPath("$.path").value("/clientes"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveRetornarBadGatewayQuandoViaCepEstiverForaDoAr() throws Exception {

        when(restTemplate.getForEntity(anyString(), any(), anyString())).thenThrow(new ExternalServiceUnavailableException("Timeout"));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"529.982.247-25\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"email\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").value("Falha de Integração"))
                .andExpect(jsonPath("$.message").value("Timeout"))
                .andExpect(jsonPath("$.path").value("/clientes"));
    }

    @Test
    @WithMockUser(username = "user")
    public void deveRetornarForbidden403QuandoUsuarioPadraoTentarCriarCliente() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }
}