# Desafio SCTec - Entrepreneur CRUD

## Descrição da Solução

Este projeto implementa uma API RESTful para gerenciamento de empreendedores catarinenses. A solução permite realizar operações de CRUD (Create, Read, Update, Delete) para cadastro de empreendedores, com validação de cidades pertencentes ao estado de Santa Catarina utilizando a API BrasilAPI.

### Funcionalidades Implementadas

- **Criar Empreendedor** (`POST /entrepreneurs`): Cadastra novos empreendedores com validação de cidade
- **Buscar Empreendedor por ID** (`GET /entrepreneurs/{id}`): Retorna os dados de um empreendedor específico
- **Listar Empreendedores** (`GET /entrepreneurs`): Lista todos os empreendedores com paginação
- **Atualizar Empreendedor** (`PUT /entrepreneurs/{id}`): Atualiza os dados de um empreendedor existente
- **Excluir Empreendedor** (`DELETE /entrepreneurs/{id}`): Realiza exclusão lógica (soft delete), alterando o status para INATIVO

### Validações

- Nome do empreendimento (obrigatório, máximo 255 caracteres)
- Nome do empreendedor (obrigatório, máximo 255 caracteres)
- Cidade (obrigatória, deve pertencer a Santa Catarina)
- Segmento de atuação (obrigatório)
- Contato (obrigatório, máximo 255 caracteres)
- Status (ativo/inativo)

## Tecnologias Utilizadas

- **Framework**: Spring Boot 4.0.3
- **Linguagem**: Java 25
- **Banco de Dados**: PostgreSQL
- **Migrações**: Flyway
- **Validação**: Spring Validation + BrasilAPI
- **Build Tool**: Maven
- **Testes**: JUnit 5 + Mockito

## Estrutura do Projeto

```
br.com.starosky.entrepreneur/
├── entrepreneur/
│   ├── EntrepreneurApplication.java    # Classe principal
│   ├── config/
│   │   └── WebClientConfig.java        # Configuração do WebClient
│   ├── controller/
│   │   └── EntrepreneurController.java # Endpoints REST
│   ├── dto/
│   │   ├── CityApiResponse.java        # Resposta da API de cidades
│   │   ├── EntrepreneurRequest.java    # DTO de requisição
│   │   ├── EntrepreneurResponse.java   # DTO de resposta
│   │   └── ErrorResponse.java         # DTO de erro
│   ├── entity/
│   │   └── Entrepreneur.java           # Entidade JPA
│   ├── enums/
│   │   ├── OperatingSegment.java        # Segmentos de atuação
│   │   └── Status.java                 # Status ativo/inativo
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java # Tratamento de exceções
│   │   └── ValidationException.java    # Exceção de validação
│   ├── repository/
│   │   └── EntrepreneurRepository.java # Repositório JPA
│   └── service/
│       ├── CityValidationService.java  # Integração BrasilAPI
│       └── EntrepreneurService.java     # Lógica de negócio
```

## Instruções de Execução

### Pré-requisitos

- Java 25
- Maven 3.9+
- PostgreSQL 14+ (ou Docker)

### Executando com Docker Compose

O projeto inclui um arquivo `compose.yaml` para facilitar a execução dos serviços:

```bash
docker-compose up -d
```

Isso iniciará:
- PostgreSQL na porta 5432
- A aplicação Spring Boot na porta 8080

### Executando Localmente

1. Clone o repositório
2. Configure o banco de dados PostgreSQL
3. Execute o comando:

```bash
./mvnw spring-boot:run
```

### Executando os Testes

```bash
./mvnw test
```

### Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | /entrepreneurs | Criar empreendedor |
| GET | /entrepreneurs/{id} | Buscar por ID |
| GET | /entrepreneurs | Listar todos (paginado) |
| PUT | /entrepreneurs/{id} | Atualizar empreendedor |
| DELETE | /entrepreneurs/{id} | Excluir empreendedor |

#### Exemplo de body para POST e PUT

```json
{
	"enterpriseName": "New Company",
	"entrepreneurName": "Jane Doe",
	"city": "Blumenau",
	"operatingSegment": "COMMERCE",
	"contact": "jane@example.com",
	"status": "ACTIVE"
}
```

## Vídeo Pitch

[Acesse o vídeo pitch aqui](https://youtu.be/yqTTNo8TL7M)

---
