# Task Manager API 📋

Microserviço RESTful desenvolvido com **Spring Boot** para gerenciamento de tarefas, como projeto final da disciplina de Programação Orientada a Objetos.

---

## 🌐 API em Produção

| | |
|---|---|
| **API Base URL** | https://projeto-final-poo-3jx3.onrender.com |
| **Documentação Swagger** | https://projeto-final-poo-3jx3.onrender.com/swagger-ui.html |

---

## 🎯 Sobre o Projeto

Sistema de gerenciamento de tarefas com suporte a categorias, desenvolvido como microserviço REST. A API permite criar, listar, atualizar e remover tarefas organizadas por categorias, com validações, tratamento de erros padronizado e documentação interativa.

**Conceitos de POO aplicados:**
- Encapsulamento via DTOs e separação em camadas
- Herança e polimorfismo nas exceções customizadas
- Abstração via interfaces de repositório (Spring Data JPA)
- Injeção de dependência via construtor em todas as classes

---

## 🛠️ Tecnologias

| Tecnologia | Finalidade |
|---|---|
| Java 17 | Linguagem de programação |
| Spring Boot 3.3 | Framework principal |
| Spring Data JPA | Persistência de dados |
| H2 Database | Banco em memória (desenvolvimento) |
| PostgreSQL + Supabase | Banco relacional (produção) |
| Springdoc OpenAPI | Documentação Swagger |
| JUnit 5 + Mockito | Testes unitários |
| JaCoCo | Cobertura de testes (mínimo 90%) |
| Maven | Gerenciamento de dependências |
| Render | Plataforma de deploy |

---

## 🏗️ Arquitetura em Camadas

```
src/main/java/com/faculdade/taskmanager/
├── config/          → Configuração do Swagger (OpenApiConfig)
├── controller/      → TaskController, CategoryController
├── service/         → TaskService, CategoryService
├── repository/      → TaskRepository, CategoryRepository
├── model/           → Task, Category, TaskStatus
├── dto/             → TaskRequestDTO, TaskResponseDTO, CategoryRequestDTO, CategoryResponseDTO
└── exception/       → ResourceNotFoundException, BusinessException, GlobalExceptionHandler
```

---

## 📚 Rotas da API

### Tarefas — `/tasks`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/tasks` | Criar nova tarefa |
| `GET` | `/tasks` | Listar todas as tarefas |
| `GET` | `/tasks/{id}` | Buscar tarefa por ID |
| `PUT` | `/tasks/{id}` | Atualizar tarefa completa |
| `PATCH` | `/tasks/{id}/status` | Atualizar apenas o status |
| `DELETE` | `/tasks/{id}` | Remover tarefa |
| `GET` | `/tasks/filter?status=` | Filtrar tarefas por status |

### Categorias — `/categories`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/categories` | Criar categoria |
| `GET` | `/categories` | Listar categorias |
| `GET` | `/categories/{id}` | Buscar categoria por ID |
| `PUT` | `/categories/{id}` | Atualizar categoria |
| `DELETE` | `/categories/{id}` | Remover categoria |

### Status disponíveis
`PENDING` | `IN_PROGRESS` | `DONE` | `CANCELLED`

---

## 💡 Exemplos de Uso

### Criar uma categoria
```bash
curl -X POST https://projeto-final-poo-3jx3.onrender.com/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Estudos", "description": "Tarefas acadêmicas"}'
```

### Criar uma tarefa
```bash
curl -X POST https://projeto-final-poo-3jx3.onrender.com/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Revisar capítulo de JPA",
    "status": "PENDING",
    "dueDate": "2026-12-31",
    "categoryId": 1
  }'
```

### Listar todas as tarefas
```bash
curl https://projeto-final-poo-3jx3.onrender.com/tasks
```

### Atualizar status
```bash
curl -X PATCH "https://projeto-final-poo-3jx3.onrender.com/tasks/1/status?status=IN_PROGRESS"
```

### Filtrar tarefas por status
```bash
curl "https://projeto-final-poo-3jx3.onrender.com/tasks/filter?status=PENDING"
```

### Deletar uma tarefa
```bash
curl -X DELETE https://projeto-final-poo-3jx3.onrender.com/tasks/1
```

---

## 🔒 Tratamento de Erros

Todos os erros seguem um formato padronizado:

```json
{
  "timestamp": "2026-06-09T03:46:49.763012126",
  "status": 404,
  "error": "Recurso Não Encontrado",
  "message": "Tarefa com ID 99 não encontrada.",
  "details": null
}
```

| Código | Situação |
|---|---|
| `400` | Dados de entrada inválidos (validação) |
| `404` | Recurso não encontrado |
| `422` | Violação de regra de negócio |
| `500` | Erro interno inesperado |

---

## 🚀 Como Rodar Localmente

### Pré-requisitos
- Java 17 (JDK)
- Maven 3.8+

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/Murilo0123/Projeto_Final_POO.git
cd Projeto_Final_POO

# 2. Configure o JAVA_HOME (Windows)
$env:JAVA_HOME = "C:\Users\seu-usuario\.jdks\temurin-17.0.19"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# 3. Compile
mvn clean package -DskipTests

# 4. Execute
java -jar target/taskmanager-0.0.1-SNAPSHOT.jar

# 5. Acesse
# Swagger: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:taskdb | User: sa | Password: (vazio)
```

### Rodar os testes

```bash
mvn test
# Relatório JaCoCo: target/site/jacoco/index.html
```

---

## ☁️ Deploy (Render + Supabase)

1. Banco PostgreSQL criado no [Supabase](https://supabase.com)
2. Tabelas criadas manualmente via SQL Editor do Supabase
3. Aplicação deployada no [Render](https://render.com) com buildpack Java
4. Variáveis de ambiente configuradas no painel do Render:

| Variável | Descrição |
|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC do Supabase (Connection Pooler) |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco |
| `SPRING_PROFILES_ACTIVE` | `prod` |

> Nenhuma credencial está no código-fonte. Todas as informações sensíveis são gerenciadas via variáveis de ambiente.

---

## 📊 Cobertura de Testes

Cobertura mínima configurada: **90%** via JaCoCo.

Testes implementados:
- `TaskServiceTest` — 12 casos de teste (criação, listagem, busca, atualização, regras de negócio, deleção)
- `CategoryServiceTest` — 9 casos de teste
- `TaskControllerTest` — 9 casos de teste com MockMvc

---

## 🌿 Branches e Commits

```
main        → produção estável
develop     → integração
feature/*   → desenvolvimento de funcionalidades
```
