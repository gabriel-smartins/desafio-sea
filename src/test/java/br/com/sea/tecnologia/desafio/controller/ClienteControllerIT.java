package br.com.sea.tecnologia.desafio.controller;

import br.com.sea.tecnologia.desafio.controller.response.ViaCepResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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

        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(viaCepMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"123.456.789-00\",\n" +
                "  \"endereco\": { \"cep\": \"01001-000\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"endereco\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Gabriel Martins"))

                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))

                .andExpect(jsonPath("$.endereco.logradouro").value("Praça da Sé"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deveRetornarBadRequestQuandoCepNaoExistirNoViaCep() throws Exception {

        ViaCepResponseDTO viaCepErroMock = new ViaCepResponseDTO();
        viaCepErroMock.setErro(true);

        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(viaCepErroMock));

        String jsonRequest = "{\n" +
                "  \"nome\": \"Gabriel Martins\",\n" +
                "  \"cpf\": \"123.456.789-00\",\n" +
                "  \"endereco\": { \"cep\": \"99999-999\" },\n" +
                "  \"telefones\": [ { \"tipo\": \"CELULAR\", \"numero\": \"(11) 98765-4321\" } ],\n" +
                "  \"emails\": [ { \"endereco\": \"gabriel@email.com\" } ]\n" +
                "}";

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("O CEP informado não existe na base dos Correios."));

    }

    @Test
    public void deveRetornarUnauthorized401QuandoRequisicaoNaoTiverAutenticacao() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    public void deveRetornarForbidden403QuandoUsuarioPadraoTentarCriarCliente() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    public void deveRetornarOk200QuandoUsuarioPadraoTentarApenasLerDados() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());
    }
}
