# TEM NA FESTA - Microsserviço de Notificações

Microsserviço responsável por receber eventos de pedidos do backend principal e enviar notificações aos clientes pelo WhatsApp. Ele consome mensagens do RabbitMQ, monta o texto a partir de templates armazenados no MySQL e envia as mensagens pela API da Whapi.

---

# Visão Geral

Este projeto é uma aplicação Java com Spring Boot que complementa o [backend principal do Tem Na Festa](https://github.com/pedrorico79/TEM-NA-FESTA-backend). O processamento é assíncrono: o backend publica eventos de pedidos no RabbitMQ e este serviço os consome para processar e enviar as notificações.

O serviço registra o conteúdo, o status e as tentativas de envio no banco de dados. Eventos já notificados para o mesmo pedido e tipo de evento não são enviados novamente.

---

# Tecnologias

- Java 21
- Spring Boot 3.3.5
- Spring AMQP / RabbitMQ
- Spring Data JPA
- MySQL 8+
- Whapi (envio de mensagens pelo WhatsApp)
- Maven
- Docker
- JUnit e Mockito

---

# Funcionalidades

- Consumo de eventos de pedidos por uma fila RabbitMQ
- Envio de notificações pelo WhatsApp usando a API da Whapi
- Templates de mensagens configuráveis e armazenados no MySQL
- Registro do conteúdo, status e tentativas de envio
- Prevenção de envio duplicado para o mesmo pedido e tipo de evento
- Formatação de valores monetários e itens do pedido em português do Brasil

Os tipos de evento previstos são `PEDIDO_CRIADO`, `PEDIDO_CONFIRMADO`, `EM_PRODUCAO`, `PRONTO_PARA_ENTREGA`, `ENTREGUE` e `CANCELADO`.

---

# Estrutura do Projeto

```text
TEM-NA-FESTA-microservice/
├── .github/
│   └── workflows/
│       └── deploy.yml
├── Dockerfile
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/temnafesta/notificacao/
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   └── infrastructure/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   └── test/
│       └── java/com/temnafesta/notificacao/
└── README.md
```

---

# Pré-requisitos

- JDK 21+
- Maven 3.9+
- MySQL 8+
- RabbitMQ
- Token da API da Whapi
- Docker (opcional)

---

# Configuração de Ambiente

Configure as variáveis de ambiente antes de iniciar o serviço:

| Variável | Descrição | Valor padrão |
|---|---|---|
| `DB_URL` | URL JDBC do banco de notificações | `jdbc:mysql://localhost:3306/temnafesta_notificacao` |
| `DB_USERNAME` | Usuário do MySQL | `root` |
| `DB_PASSWORD` | Senha do MySQL | Não definido |
| `RABBITMQ_HOST` | Host do RabbitMQ | `localhost` |
| `RABBITMQ_PORT` | Porta do RabbitMQ | `5672` |
| `RABBITMQ_USERNAME` | Usuário do RabbitMQ | `admin` |
| `RABBITMQ_PASSWORD` | Senha do RabbitMQ | Não definido |
| `WHAPI_TOKEN` | Token de autenticação da API Whapi | Obrigatório |

O serviço espera encontrar um MySQL acessível e uma instância do RabbitMQ com as credenciais configuradas. O token da Whapi é obrigatório para inicializar o adaptador de envio.


# Executando Localmente

## 1. Clone o repositório

```bash
git clone https://github.com/pedrorico79/TEM-NA-FESTA-microservice.git
cd TEM-NA-FESTA-microservice
```

## 2. Configure as variáveis de ambiente

### Linux/macOS

```bash
export DB_URL="jdbc:mysql://localhost:3306/temnafesta_notificacao"
export DB_USERNAME="root"
export DB_PASSWORD="sua_senha"
export RABBITMQ_HOST="localhost"
export RABBITMQ_PORT="5672"
export RABBITMQ_USERNAME="admin"
export RABBITMQ_PASSWORD="sua_senha_rabbitmq"
export WHAPI_TOKEN="seu_token_whapi"
```

### Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/temnafesta_notificacao"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="sua_senha"
$env:RABBITMQ_HOST="localhost"
$env:RABBITMQ_PORT="5672"
$env:RABBITMQ_USERNAME="admin"
$env:RABBITMQ_PASSWORD="sua_senha_rabbitmq"
$env:WHAPI_TOKEN="seu_token_whapi"
```

## 3. Inicie o serviço

```bash
mvn spring-boot:run
```

A aplicação fica configurada na porta `8081`. O serviço opera como consumidor de mensagens; não há endpoints REST de negócio documentados neste projeto.

---

# Executando com Docker

Construa a imagem e inicie o container:

```bash
docker build -t tem-na-festa-notificacao .
docker run --rm -p 8081:8081 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/temnafesta_notificacao" \
  -e DB_USERNAME="root" \
  -e DB_PASSWORD="sua_senha" \
  -e RABBITMQ_HOST="host.docker.internal" \
  -e RABBITMQ_PORT="5672" \
  -e RABBITMQ_USERNAME="admin" \
  -e RABBITMQ_PASSWORD="sua_senha_rabbitmq" \
  -e WHAPI_TOKEN="seu_token_whapi" \
  tem-na-festa-notificacao
```

O MySQL e o RabbitMQ precisam estar acessíveis pelo container. Em outros ambientes, substitua `host.docker.internal` pelos hosts correspondentes. O repositório não inclui um arquivo `docker-compose.yml`.

---

# Integração com RabbitMQ

O serviço declara e consome a seguinte topologia:

| Recurso | Nome |
|---|---|
| Exchange (direct) | `exchange_pedido` |
| Fila durável | `queue_pedido` |
| Routing key | `routing_key_pedido` |

As mensagens são desserializadas como JSON e devem conter os dados do evento do pedido, incluindo identificador, tipo de evento, dados do cliente, valores e itens. O contrato correspondente está definido em `ReceberEventoNotificacaoCommand`.

---

# Testes

Para executar os testes automatizados:

```bash
mvn test
```

---

# Arquitetura

O microsserviço organiza o código em camadas inspiradas em arquitetura hexagonal:

- `application`: comando recebido e caso de uso de processamento
- `domain`: modelos, portas e tipos de evento
- `infrastructure`: integração com RabbitMQ e Whapi, persistência JPA e configuração

Essa separação mantém as regras de processamento desacopladas dos mecanismos externos de mensageria, envio e armazenamento.

---

# Licença

Este projeto é destinado ao uso acadêmico e de extensão. Consulte o responsável pelo repositório para informações específicas sobre uso e distribuição.
