# Task Manager API 📋

Microserviço RESTful desenvolvido com **Spring Boot** para gerenciamento de tarefas, como projeto da disciplina de Programação Orientada a Objetos.

---

## 🎯 Propósito

API para criação, listagem, atualização e remoção de **tarefas** organizadas por **categorias**, aplicando conceitos de POO, arquitetura em camadas, persistência com JPA, documentação com Swagger e testes unitários com JUnit 5 + Mockito.

---

## 🛠️ Tecnologias

| Tecnologia | Finalidade |
|---|---|
| Java 17 | Linguagem de programação |
| Spring Boot 3.3 | Framework principal |
| Spring Data JPA | Persistência de dados |
| H2 Database | Banco em memória (desenvolvimento) |
| PostgreSQL | Banco relacional (produção) |
| Springdoc OpenAPI | Documentação Swagger |
| JUnit 5 + Mockito | Testes unitários |
| JaCoCo | Cobertura de testes (mínimo 90%) |
| Maven | Gerenciamento de dependências |

---

## 🏗️ Arquitetura

```
src/main/java/com/faculdade/taskmanager/
├── config/          # Configuração do Swagger (OpenApiConfig)
├── controller/      # Camada de controle (TaskController, CategoryController)
├── service/         # Regras de negócio (TaskService, CategoryService)
├── repository/      # Acesso ao banco via JPA (TaskRepository, CategoryRepository)
├── model/           # Entidades JPA (Task, Category, TaskStatus)
├── dto/             # Data Transfer Objects (request/response)
└── exception/       # Exceções customizadas e GlobalExceptionHandler
```

---

## 🚀 Como Rodar Localmente

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Git

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/seu-grupo/taskmanager.git
cd taskmanager

# 2. Execute a aplicação (perfil dev com H2)
./mvnw spring-boot:run

# 3. Acesse o Swagger UI
http://localhost:8080/swagger-ui.html

# 4. Acesse o console H2 (banco em memória)
http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:taskdb | User: sa | Password: (vazio)
```

### Executar os Testes

```bash
# Roda os testes e gera relatório de cobertura JaCoCo
./mvnw test

# Relatório de cobertura gerado em:
# target/site/jacoco/index.html
```

---

## 📚 Rotas da API

### Tarefas (`/tasks`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/tasks` | Criar nova tarefa |
| `GET` | `/tasks` | Listar todas as tarefas |
| `GET` | `/tasks/{id}` | Buscar tarefa por ID |
| `PUT` | `/tasks/{id}` | Atualizar tarefa completa |
| `PATCH` | `/tasks/{id}/status` | Atualizar apenas o status |
| `DELETE` | `/tasks/{id}` | Remover tarefa |
| `GET` | `/tasks/filter?status=PENDING` | Filtrar tarefas por status |

### Categorias (`/categories`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/categories` | Criar categoria |
| `GET` | `/categories` | Listar categorias |
| `GET` | `/categories/{id}` | Buscar categoria por ID |
| `PUT` | `/categories/{id}` | Atualizar categoria |
| `DELETE` | `/categories/{id}` | Remover categoria |

### Status disponíveis: `PENDING` | `IN_PROGRESS` | `DONE` | `CANCELLED`

---

## 💡 Exemplos de Uso (cURL)

### Criar uma categoria
```bash
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Estudos", "description": "Tarefas acadêmicas"}'
```

### Criar uma tarefa
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Revisar capítulo de JPA",
    "status": "PENDING",
    "dueDate": "2025-12-31",
    "categoryId": 1
  }'
```

### Listar todas as tarefas
```bash
curl http://localhost:8080/tasks
```

### Atualizar status para IN_PROGRESS
```bash
curl -X PATCH "http://localhost:8080/tasks/1/status?status=IN_PROGRESS"
```

### Filtrar tarefas por status
```bash
curl "http://localhost:8080/tasks/filter?status=PENDING"
```

### Deletar uma tarefa
```bash
curl -X DELETE http://localhost:8080/tasks/1
```

---

## 🌐 Deploy em Produção

### Link público da API

> **https://taskmanager-api.onrender.com** *(substitua pelo link gerado após o deploy)*

### Como foi realizado o deploy (Render + Supabase)

1. **Banco de dados:** Criado um banco PostgreSQL gratuito no [Supabase](https://supabase.com).
2. **Aplicação:** Deploy realizado no [Render](https://render.com) usando o buildpack de Java.
3. **Variáveis de ambiente** configuradas no painel do Render:
   - `DATABASE_URL` → URL de conexão do Supabase
   - `DATABASE_USERNAME` → usuário do banco
   - `DATABASE_PASSWORD` → senha do banco
   - `SPRING_PROFILES_ACTIVE` → `prod`
4. O Render detecta automaticamente o `pom.xml` e executa `./mvnw package` para gerar o `.jar`.

> **Importante:** nenhuma senha ou credencial está no código-fonte ou nos arquivos `.properties`. Todas as informações sensíveis são gerenciadas via variáveis de ambiente na plataforma.

---

## 👥 Divisão de Tarefas

| Integrante | Responsabilidade |
|---|---|
| Integrante 1 | Model (`Task`, `Category`, `TaskStatus`) e repositórios JPA |
| Integrante 2 | `TaskService` e regras de negócio |
| Integrante 3 | `CategoryService` e tratamento de exceções |
| Integrante 4 | `TaskController` e `CategoryController` |
| Integrante 5 | Testes unitários (`TaskServiceTest`, `CategoryServiceTest`) |
| Integrante 6 | Testes do controlador, configuração Swagger, deploy e README |

---

## 📊 Cobertura de Testes

Cobertura mínima exigida: **90%** (verificada via JaCoCo).

Para visualizar o relatório após rodar os testes:
```
target/site/jacoco/index.html
```

---

## 🌿 Padrão de Branches e Commits

- `main` — produção estável
- `develop` — integração
- `feature/nome-da-feature` — desenvolvimento de funcionalidades

**Padrão de commits:**
```
feat: adiciona endpoint de criação de tarefas
fix: corrige validação de data futura no DTO
test: adiciona testes para TaskService
docs: atualiza README com instruções de deploy
```
