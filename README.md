# 🎮 Jan-Ken-Po - Microsserviços

> Projeto de microsserviços para gerenciamento de partidas de Pedra, Papel e Tesoura (Jan-Ken-Po)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.14.3-green.svg)](https://gradle.org/)

## 📋 Sobre o Projeto

Este projeto implementa um sistema de gerenciamento de partidas do clássico jogo **Jan-Ken-Po** (Pedra, Papel e Tesoura) utilizando arquitetura de **microsserviços**. Foi desenvolvido para praticar conceitos fundamentais de:

- ✅ **Arquitetura de Microsserviços (MSA)**
- ✅ **Comunicação Síncrona com Feign Client**
- ✅ **Persistência de Dados com Spring Data JPA**
- ✅ **RESTful APIs**
- ✅ **Tratamento de Exceções**

## 🏗️ Arquitetura

O sistema é composto por **dois microsserviços independentes**:

```
┌─────────────────────────────────────────────────────────┐
│                                                         │
│  ┌──────────────────┐      ┌──────────────────┐        │
│  │   MS-USER        │      │   MS-MATCH       │        │
│  │   Port: 8081     │◄─────│   Port: 8082     │        │
│  │                  │ Feign│                  │        │
│  │ - Usuários       │      │ - Partidas       │        │
│  │ - Histórico      │      │ - Lógica do Jogo │        │
│  └────────┬─────────┘      └────────┬─────────┘        │
│           │                         │                  │
│  ┌────────▼─────────┐      ┌────────▼─────────┐        │
│  │  PostgreSQL      │      │  PostgreSQL      │        │
│  │  ms-user DB      │      │  ms-match DB     │        │
│  └──────────────────┘      └──────────────────┘        │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 📦 MS-USER (User Service) - Porta 8081

**Responsabilidades:**

- Gerenciamento de usuários (CRUD)
- Armazenamento do histórico de partidas
- Endpoint interno para receber resultados de partidas

### 📦 MS-MATCH (Match Service) - Porta 8082

**Responsabilidades:**

- Gerenciamento de partidas
- Validação de jogadas
- Determinação de vencedores
- Comunicação com MS-USER via Feign Client

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.2.x**
- **Spring Data JPA**
- **PostgreSQL**
- **OpenFeign** (Comunicação entre microsserviços)
- **Gradle**
- **Lombok**

## 📋 Pré-requisitos

- Java JDK 21
- PostgreSQL 16+
- Git

## 🚀 Como Executar

### 1️⃣ Clone o Repositório

```bash
git clone https://github.com/AleLorencato/Jan-Ken-Po.git
cd Jan-Ken-Po
```

### 2️⃣ Configure os Bancos de Dados

Crie dois bancos de dados no PostgreSQL:

```sql
CREATE DATABASE "ms-user";
CREATE DATABASE "ms-match";
```

### 3️⃣ Configure as Credenciais

**MS-USER** (`ms-user/src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ms-user
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

**MS-MATCH** (`ms-match/src/main/resources/application.yml`):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ms-match
    username: seu_usuario
    password: sua_senha
```

### 4️⃣ Execute os Microsserviços

**Terminal 1 - MS-USER:**

```bash
cd ms-user
./gradlew bootRun
```

**Terminal 2 - MS-MATCH:**

```bash
cd ms-match
./gradlew bootRun
```

## 📡 Endpoints da API

### MS-USER (Port 8081)

| Método   | Endpoint                     | Descrição                   |
| -------- | ---------------------------- | --------------------------- |
| `POST`   | `/users`                     | Criar novo usuário          |
| `GET`    | `/users`                     | Listar todos os usuários    |
| `GET`    | `/users/{id}`                | Buscar usuário por ID       |
| `GET`    | `/users/username/{username}` | Buscar usuário por username |
| `GET`    | `/users/{id}/history`        | Ver histórico de partidas   |
| `PUT`    | `/users/{id}`                | Atualizar usuário           |
| `DELETE` | `/users/{id}`                | Deletar usuário             |

### MS-MATCH (Port 8082)

| Método | Endpoint                  | Descrição            |
| ------ | ------------------------- | -------------------- |
| `POST` | `/matches/start`          | Iniciar nova partida |
| `POST` | `/matches/{matchId}/move` | Registrar jogada     |

## 🧪 Exemplo de Uso

### 1. Criar Usuários

```bash
POST http://localhost:8081/users
{
  "username": "player1"
}
```

### 2. Iniciar Partida

```bash
POST http://localhost:8082/matches/start
{
  "firstPlayerUsername": "player1",
  "secondPlayerUsername": "player2"
}
```

### 3. Fazer Jogadas

```bash
POST http://localhost:8082/matches/1/move
{
  "username": "player1",
  "move": "JAN"
}
```

**Movimentos válidos:** `JAN` (Pedra), `KEN` (Tesoura), `PO` (Papel)

## 🎮 Regras do Jogo

- 🪨 **JAN** (Pedra) vence **KEN** (Tesoura)
- ✂️ **KEN** (Tesoura) vence **PO** (Papel)
- 📄 **PO** (Papel) vence **JAN** (Pedra)

## 📊 Estrutura do Projeto

```
jan-ken-po/
├── ms-user/                 # Microsserviço de usuários
│   ├── src/main/java/
│   │   └── compass/uol/jankenpo/
│   │       ├── controllers/
│   │       ├── services/
│   │       ├── repositories/
│   │       ├── models/
│   │       └── ...
│   └── src/main/resources/
│       └── application.properties
│
├── ms-match/                # Microsserviço de partidas
│   ├── src/main/java/
│   │   └── compass/uol/msmatch/
│   │       ├── controllers/
│   │       ├── services/
│   │       ├── repositories/
│   │       ├── clients/      # Feign Clients
│   │       └── ...
│   └── src/main/resources/
│       └── application.yml
│
└── README.md
```

## 📝 Licença

Este projeto foi desenvolvido para fins de aprendizado pessoal.

---
