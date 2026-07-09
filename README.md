# Desafio Sea Tecnologia - Backend

Este repositório contém a implementação do backend do desafio técnico da Sea Tecnologia, seguindo os requisitos de autenticação, cadastro de clientes, validações, máscaras e integração com o serviço ViaCEP.

## Visão geral

A aplicação fornece uma API REST para gerenciamento de clientes com:

- autenticação via JWT;
- controle de permissões por perfil (`ADMIN` e `USER`);
- cadastro, listagem, consulta, atualização e exclusão de clientes;
- validação de CPF, telefone, e-mail e endereço;
- integração com o serviço ViaCEP para consulta de CEP;
- documentação da API com Swagger/OpenAPI.

## Repositório do frontend

O frontend do desafio está em outro repositório:

- https://github.com/gabriel-smartins/desafio-sea-frontend

Esse backend pode ser executado de forma independente e consumido pelo frontend quando disponível.

## Decisões arquiteturais

### 1. Arquitetura em camadas

A aplicação foi organizada com separação clara entre:

- `controller`: expõe os endpoints REST;
- `service`: concentra a regra de negócio;
- `repository`: acesso aos dados;
- `model`: entidades do domínio;
- `dto`: contratos de entrada e saída da API;
- `exception`: tratamento centralizado de erros.

Essa estrutura facilita manutenção, testes e evolução futura da aplicação.

### 2. Uso de DTOs para desacoplar API e domínio

Os objetos de request/response foram separados das entidades JPA para evitar vazamento de detalhes internos e permitir validações mais objetivas no contrato da API.

### 3. Segurança com Spring Security + JWT

A autenticação é feita via JWT e o acesso a recursos é controlado por roles.

- `ADMIN`: pode criar, atualizar e excluir clientes;
- `USER`: pode apenas consultar clientes.

### 4. Validação centralizada

As regras de negócio e de entrada foram aplicadas tanto em DTOs quanto no service, com tratamento padronizado de erros por meio de `@RestControllerAdvice`.

### 5. Integração com ViaCEP

O cadastro de endereço usa a consulta ao ViaCEP para enriquecer automaticamente os dados quando o CEP é informado. Os campos preenchidos manualmente pelo usuário têm prioridade sobre os dados retornados pelo serviço, preservando a intenção do cadastro.

### 6. Persistência simples para desenvolvimento

O projeto usa H2 em memória para facilitar a execução local sem necessidade de configurar um banco externo.

## Requisitos

- Java 8
- Maven 3.8+
- Git

## Como rodar o projeto localmente

### 1. Clone o repositório

```bash
git clone https://github.com/gabriel-smartins/desafio-sea.git
cd desafio-sea
```

### 2. Defina a variável de ambiente do JWT

```bash
export JWT_SECRET=meu-segredo-local
```

No Windows PowerShell:

```powershell
$env:JWT_SECRET="meu-segredo-local"
```

### 3. Execute o projeto

Com Maven Wrapper:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Ou, se o wrapper não estiver disponível para execução no seu ambiente:

```bash
mvn spring-boot:run
```

A aplicação ficará disponível em:

- http://localhost:8080

### 4. Acesse a documentação Swagger

Depois de subir a aplicação, acesse:

- http://localhost:8080/swagger-ui/index.html

### 5. Faça login para obter o token

A API disponibiliza o endpoint de autenticação:

- POST /auth/login

Exemplo de corpo:

```json
{
  "username": "admin",
  "password": "123qwe!@#"
}
```

Também existe um usuário padrão:

```json
{
  "username": "user",
  "password": "123qwe123"
}
```

## Endpoints principais

### Autenticação

- POST /auth/login

### Clientes

- POST /clientes
- GET /clientes
- GET /clientes/{id}
- PUT /clientes/{id}
- DELETE /clientes/{id}

## Executar os testes

```bash
./mvnw test
```

## Observações úteis

- O projeto usa H2 em memória, então os dados são reiniciados ao reiniciar a aplicação.
- Os usuários iniciais são criados automaticamente na inicialização da aplicação.
- O console do H2 está habilitado para facilitar a inspeção do estado do banco em tempo de execução. Acesse http://localhost:8080/h2-console e use os dados de conexão padrão: URL JDBC `jdbc:h2:mem:desafio`, usuário `sa` e senha em branco.
- O backend não depende de um frontend para funcionar; ele pode ser consumido diretamente via Swagger ou por qualquer cliente HTTP.
