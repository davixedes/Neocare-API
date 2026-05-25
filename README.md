# Integrantes

- Kaue Vinicius Samartino da Silva - 559317
- João dos Santos Cardoso de Jesus - 560400
- Davi Praxedes Santos - 560719

## Link vídeo 

https://youtu.be/BdNGmoQ4H9U

# Neocare API — Sistema de Monitoramento de Estresse

Neocare é uma plataforma completa de monitoramento de estresse e sinais vitais, desenvolvida com **Java 17**, **Spring Boot 3.5.6**, **Thymeleaf**, **Spring Security** (JWT + sessão) e **PostgreSQL**. O projeto segue os princípios da **Clean Architecture** com separação rigorosa de camadas.

## Para SP De DevOps e Cloud Computing
Acessar o arquivo [passo-a-passo-cloud.md](passo-a-passo-cloud.md) com as instruções

## 📋 Funcionalidades

### Gestão de Usuários
- Cadastro completo com dados pessoais, endereço e credenciais
- Edição de dados cadastrais
- Consulta por CPF ou username
- Listagem de usuários ativos
- Desativação de usuários (soft delete)ㅤ

### Medições Psicofisiológicas
- Registro de medições com **HRV** (variação de frequência cardíaca) e **GSR** (condutividade da pele) — sinais correlacionados com estresse
- Geração automática de alertas com severidade (Alta, Moderada) baseada nos valores detectados
- Cálculo automático de **Métrica de Estresse** (índice 0-100 + categoria: EUSTRESS, AMBIENTAL, PSICOLOGICO, FISIOLOGICO, EPISODICO_AGUDO, AGUDO, CRONICO)

### Medições de Sinais Vitais
- Registro de **BPM** (batimentos por minuto), **SpO2** (oxigenação), pressão arterial sistólica e diastólica

### Análise Preditiva via Oracle APEX
- Toda medição registrada (psicofisiológica ou vital) é enviada ao **Oracle APEX** para análise preditiva
- Retorna `score` (0.0 a 1.0), `predicao` (ex.: `ALTO_ESTRESSE`, `NORMAL`) e `analisadoEm`
- **Fail-safe:** se o APEX cair, a medição é persistida normalmente sem o resultado
- Resultado consultável via `GET /api/resultados-predicao/medicao/{id}` ou exibido após o registro na UI

### Alertas
- Criação automática de alertas a partir de **dois caminhos** independentes:
  - Regras crisp sobre os valores brutos (limiares de HRV/GSR/BPM/SpO2/PA)
  - Análise preditiva do APEX (score ≥ 0.5 → MODERADA, ≥ 0.8 → ALTA)
- Classificação por tipo e severidade
- **Status de leitura** (Novo / Lido) com ação "marcar como lido" na tela
- Visualização por usuário ou global (admin)

### Dashboards (Thymeleaf)
- **Dashboard do Usuário**: medições pessoais de estresse, sinais vitais e alertas
- **Dashboard do Admin**: visão global de usuários cadastrados e alertas recentes

### Autenticação & Autorização
- Login via formulário (Thymeleaf, sessão) e via API REST (JWT Bearer token)
- Dois perfis: `ROLE_USER` (acesso ao próprio dashboard e dados) e `ROLE_ADMIN` (acesso total + gerenciamento de usuários)
- Senhas criptografadas com BCrypt
- Token JWT com expiração de 24 horas

### Validações de Domínio
- CPF (formato e dígitos verificadores)
- E-mail, telefone, CEP, UF
- Idade mínima, peso e altura

## 🏗️ Arquitetura (Clean Architecture)

```
interfaces/       ← Controllers MVC/REST, DTOs, Mappers, GlobalExceptionHandler
application/      ← Use Cases (orquestração e regras de aplicação)
domain/           ← Modelos de domínio puros, interfaces de repositório, exceções, enums
infrastructure/   ← JPA Entities, Repository Adapters, Security (JWT), Config, Services
```

**Regra de dependência:** `interfaces → application → domain ← infrastructure`
O domínio nunca importa de infraestrutura ou interfaces.

### Modelos de Domínio

| Modelo | Descrição |
|--------|-----------|
| `Usuario` | Dados pessoais, endereço embarcado, credenciais |
| `Credenciais` | Username, password (BCrypt), roles — implementa `UserDetails` |
| `Role` | `ROLE_ADMIN`, `ROLE_USER` (many-to-many com Credenciais) |
| `Endereco` | Value Object embarcado no Usuário |
| `Dispositivo` | Tipo de dispositivo vinculado a um usuário |
| `Medicao` | Base para medições (psicofisiológica e vital) |
| `MedicaoPsicofisiologica` | HRV (ms) e GSR (μS) — sinais correlatos com estresse |
| `MedicaoVital` | BPM, SpO2 (%), pressão sistólica/diastólica |
| `MetricaEstresse` | Índice de estresse (0-100) + categoria, calculado a partir da medição psicofisiológica |
| `ResultadoPredicao` | `score`, `predicao` e `analisadoEm` retornados pelo Oracle APEX |
| `Alerta` | Tipo, severidade, valor detectado, mensagem, status de leitura (`lido`) |

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Finalidade |
|------------|-----------|
| **Java 17** | Linguagem |
| **Spring Boot 3.5.6** | Framework principal |
| **Spring Security** | Autenticação (formulário + JWT) e autorização |
| **Thymeleaf** | Templates HTML para frontend web |
| **Spring Data JPA / Hibernate** | Persistência de dados |
| **PostgreSQL 17** | Banco de dados relacional |
| **Oracle APEX (ORDS)** | Endpoint externo para análise preditiva (via `RestTemplate`) |
| **Flyway** | Migração e versionamento do schema (12 migrations) |
| **JJWT 0.12.5** | Geração e validação de tokens JWT |
| **SpringDoc OpenAPI 2.7.0** | Documentação automática da API (Swagger) |
| **Maven** | Gerenciamento de dependências e build |
| **Docker** | Containerização (multi-stage build) |
| **JUnit + Spring Security Test** | Testes automatizados |

## 🚀 Executando a Aplicação

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.8.1 ou superior
- Docker (recomendado) ou PostgreSQL 17 instalado localmente

### Opção 1 — Docker Compose (recomendado)

```shell
cd src/main/docker
docker-compose up -d
```

Isso sobe automaticamente o PostgreSQL e a API na porta `8080`.

### Opção 2 — Execução local

1. Crie um banco PostgreSQL chamado `neocare`
2. Configure as credenciais em `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/neocare
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

3. Execute a aplicação:

```shell
./mvnw spring-boot:run
```

> A documentação da API estará disponível em http://localhost:8080/swagger-ui.html

### Empacotando a Aplicação

```shell
./mvnw package
java -jar target/Neocare-API-0.0.1-SNAPSHOT.jar
```

## 📡 API REST

### Autenticação

| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| `POST` | `/api/auth/login` | Público | Login — retorna JWT token |

### Usuários

| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| `POST` | `/usuarios` | Público | Cadastrar novo usuário |
| `GET` | `/usuarios` | USER / ADMIN | Listar todos os usuários ativos |
| `GET` | `/usuarios/{cpf}` | USER / ADMIN | Buscar usuário por CPF |
| `GET` | `/usuarios/username/{username}` | USER / ADMIN | Buscar usuário por username |
| `PUT` | `/usuarios` | USER / ADMIN | Atualizar dados do usuário |
| `DELETE` | `/usuarios/{cpf}` | ADMIN | Desativar usuário |

### Medições

| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| `POST` | `/medicoes/medicao_psicofisiologica` | Público | Registrar medição psicofisiológica (HRV + GSR) — resposta inclui `ResultadoPredicao` |
| `POST` | `/medicoes/medicao_vital` | Público | Registrar medição vital (BPM, SpO2, PA) — resposta inclui `ResultadoPredicao` |
| `GET` | `/medicoes/psicofisiologicas/usuario/{usuarioId}` | Autenticado | Listar medições psicofisiológicas de um usuário |
| `GET` | `/medicoes/vitais/usuario/{usuarioId}` | Autenticado | Listar medições vitais de um usuário |

### Predição (Oracle APEX)

| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| `GET` | `/api/resultados-predicao/medicao/{medicaoId}` | Autenticado | Consultar resultado da análise preditiva de uma medição |

### Alertas

| Método | Endpoint | Acesso | Descrição |
|--------|----------|--------|-----------|
| `GET` | `/api/alertas` | ADMIN | Listar todos os alertas do sistema |
| `GET` | `/api/alertas/usuario/{usuarioId}` | USER / ADMIN | Listar alertas de um usuário |

### Formato de Erro Padrão

```json
{
  "statusCode": 400,
  "message": "Mensagem de erro",
  "timestamp": "2026-04-12T10:30:00",
  "path": "/usuarios"
}
```

## 🌐 Frontend Web (Thymeleaf)

| Rota | Acesso | Descrição |
|------|--------|-----------|
| `/auth/login` | Público | Página de login |
| `/auth/registro` | Público | Formulário de cadastro completo (dados pessoais, endereço, credenciais) |
| `/dashboard` | Autenticado | Dashboard do usuário (medições e alertas pessoais) ou admin (visão global) |
| `/medicoes-web` | Autenticado | Lista de medições do usuário com colunas de **Predição** e **Score** |
| `/medicoes-web/nova-psicofisiologica` | Autenticado | Formulário de nova medição psicofisiológica |
| `/medicoes-web/nova-vital` | Autenticado | Formulário de nova medição vital |
| `/medicoes-web/resultado` | Autenticado | Tela de confirmação após registro, exibindo o resultado da predição |
| `/alertas-web` | Autenticado | Lista de alertas com status (Novo / Lido) e botão "marcar como lido" |
| `POST /alertas-web/{id}/marcar-lido` | Autenticado | Marca um alerta como lido (form action) |
| `/` | Autenticado | Redireciona para `/dashboard` |

### Páginas de Erro
- `403` — Acesso negado
- `404` — Página não encontrada

## 🔒 Segurança

O sistema possui duas cadeias de filtro de segurança:

1. **Web (sessão)**: Form login para páginas Thymeleaf — cria sessão no servidor
2. **API (JWT stateless)**: Token Bearer para endpoints REST — sem estado no servidor

### CORS

Origens permitidas: `localhost`, `*.vercel.app`, `*.render.com`

## 🤖 Configuração do Oracle APEX (Análise Preditiva)

A integração com o Oracle APEX consome um endpoint ORDS REST que recebe a medição e retorna a predição. As variáveis abaixo controlam a integração:

| Propriedade | Variável de ambiente | Default |
|---|---|---|
| `apex.predicao.url` | `APEX_PREDICAO_URL` | URL ORDS do workspace |
| `apex.predicao.api-key` | `APEX_PREDICAO_API_KEY` | (vazio) — sem header `Authorization` |
| `apex.predicao.timeout-ms` | — | `5000` |

Quando `api-key` está preenchido, o cliente envia `Authorization: Bearer <api-key>`. Se a chamada ao APEX falhar (timeout, 5xx, payload inválido), a medição é persistida normalmente e o resultado fica como `null` na resposta — o usuário pode tentar novamente depois consultando `GET /api/resultados-predicao/medicao/{id}`.

## 🗄️ Migrações Flyway

| Versão | Descrição |
|--------|-----------|
| V1 | Criação das tabelas `credenciais`, `role` e junction `credenciais_role` |
| V2 | Criação da tabela `usuario` com endereço embarcado |
| V3 | Criação da tabela `dispositivo` |
| V4 | Criação da tabela `medicoes` (base) |
| V5 | Criação da tabela `medicoes_estresse` (HRV, GSR) — renomeada na V12 |
| V6 | Criação da tabela `medicoes_vitais` (BPM, SpO2, PA) |
| V7 | Criação da tabela `alertas` |
| V8 | Seed de dispositivos padrão |
| V9 | Criação da tabela `metricas_estresse` (índice de estresse + categoria) |
| V10 | Criação da tabela `resultados_predicao` (score, predicao, analisadoEm — vindo do APEX) |
| V11 | Adição da coluna `lido BOOLEAN NOT NULL DEFAULT FALSE` em `alertas` |
| V12 | Renomeação de `medicoes_estresse` para `medicoes_psicofisiologicas` |

## 📁 Estrutura de Pastas

```
src/main/java/com/neocare/api/
├── domain/                    # Modelos puros, enums, exceções, contratos de repositório
│   ├── enums/
│   ├── exception/
│   ├── logging/
│   ├── model/
│   └── repository/
├── application/               # Use Cases
│   ├── exception/
│   ├── port/                  # Portas de saída (ex.: PredicaoApexPort)
│   └── usecase/
│       ├── alerta/            # ListarAlertas, MarcarAlertaComoLido, GerarAlertaPorPredicao
│       ├── dispositivo/
│       ├── medicao/
│       │   ├── psicofisiologica/
│       │   ├── vital/
│       │   └── metrica/
│       ├── predicao/          # AnalisarMedicao, BuscarPredicoesPorMedicaoIds
│       └── usuario/
├── infrastructure/            # Implementações técnicas
│   ├── api/rest/              # REST Adapters (Spring-specific)
│   ├── client/                # ApexPredicaoClientAdapter (HTTP cliente do APEX)
│   ├── config/                # SecurityConfig, SpringDocConfig, ApexPredicaoConfig, Beans
│   ├── entity/                # JPA Entities
│   ├── logging/
│   ├── persistance/           # Repository Adapters (JPA → Domain)
│   ├── repository/            # Spring Data JPA Repositories
│   ├── security/              # JWT (JwtUtil, JwtAuthenticationFilter)
│   └── services/              # CustomUserDetailsService
└── interfaces/                # Contratos e DTOs
    ├── assembler/             # MedicaoOutputAssembler
    ├── controller/            # Controllers puros (framework-agnostic)
    ├── dto/
    │   ├── form/              # Forms Thymeleaf
    │   ├── input/
    │   └── output/
    ├── handler/               # GlobalExceptionHandler
    ├── mapper/
    └── web/                   # Thymeleaf Web Controllers
```

## 📄 Licença

Este projeto está licenciado sob a licença MIT — veja o arquivo LICENSE para detalhes.

---

