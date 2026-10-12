# Cidades ESG Inteligentes

API REST para cadastrar e consultar indicadores ambientais, sociais e de governança (ESG) de cidades. Projeto acadêmico demonstrativo com Java 21, Spring Boot, MySQL, Docker Compose e GitHub Actions.

> **Estado das evidências:** os arquivos e scripts estão preparados, mas os comandos de build, testes, execução dos containers e deploy precisam ser executados no ambiente do aluno. Não inclua prints como se já tivessem sido executados. Registre as evidências reais após seguir este guia.

## Funcionalidades

- CRUD de cidades.
- Indicadores de ambiente, social e governança de 0 a 100.
- Percentual de energia renovável de 0 a 100.
- Filtro por estado.
- Endpoint de saúde para verificação após deploy.
- Bancos separados para staging e produção no Docker Compose.

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.5.6
- Spring Web, Spring Data JPA, Bean Validation e Actuator
- Maven
- MySQL 8.4
- Docker e Docker Compose
- GitHub Actions
- JUnit 5

## Estrutura

```text
cidades-esg-inteligentes/
├── .github/workflows/ci-cd.yml
├── src/
│   ├── main/java/br/com/cidadesesg/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── main/resources/
│   └── test/java/
├── .env.example
├── .dockerignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Como executar localmente com Docker

### Pré-requisitos

- Docker Desktop instalado e em execução.
- Git (opcional, para clonar o repositório).

### 1. Configurar variáveis

Windows PowerShell:

```powershell
Copy-Item .env.example .env
notepad .env
```

Altere `MYSQL_ROOT_PASSWORD` para uma senha forte. Não publique o arquivo `.env` no Git.

### 2. Subir staging

Na raiz do projeto:

```bash
docker compose --profile staging up -d --build
```

A API ficará disponível em `http://localhost:8081`. Verifique:

```bash
curl http://localhost:8081/api/health
```

### 3. Subir produção local

```bash
docker compose --profile production up -d --build
```

A API ficará disponível em `http://localhost:8082`. Verifique:

```bash
curl http://localhost:8082/api/health
```

Os ambientes têm containers MySQL, redes e volumes separados. Para subir ambos:

```bash
docker compose --profile staging --profile production up -d --build
```

Para ver serviços e logs:

```bash
docker compose ps
docker compose --profile staging logs -f app-staging
docker compose --profile production logs -f app-prod
```

Para parar sem apagar os dados:

```bash
docker compose --profile staging --profile production down
```

Para remover também os dados persistidos, somente se tiver certeza:

```bash
docker compose --profile staging --profile production down -v
```

## API REST

Base URL de staging: `http://localhost:8081`

Base URL de produção local: `http://localhost:8082`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/health` | Retorna status e horário da aplicação |
| GET | `/api/cidades` | Lista todas as cidades |
| GET | `/api/cidades?estado=SP` | Filtra cidades pelo estado |
| GET | `/api/cidades/{id}` | Consulta uma cidade |
| POST | `/api/cidades` | Cadastra uma cidade |
| PUT | `/api/cidades/{id}` | Atualiza uma cidade |
| DELETE | `/api/cidades/{id}` | Exclui uma cidade |

### Exemplo de criação

```bash
curl -X POST http://localhost:8081/api/cidades \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Campinas",
    "estado": "SP",
    "indiceAmbiental": 78,
    "indiceSocial": 82,
    "indiceGovernanca": 75,
    "percentualEnergiaRenovavel": 40
  }'
```

Os indicadores e o percentual de energia renovável aceitam números inteiros entre 0 e 100. Nome e estado são obrigatórios.

## Pipeline CI/CD

Workflow: `.github/workflows/ci-cd.yml`.

1. **Build e testes:** em cada push ou pull request para `main`, GitHub Actions prepara Java 21, executa `mvn clean verify` e constrói uma imagem Docker.
2. **Deploy em staging:** após o sucesso da validação, o runner local inicia o perfil `staging` e verifica `/api/health` na porta 8081.
3. **Deploy em produção:** somente depois do staging responder com sucesso, o workflow inicia o perfil `production` e verifica `/api/health` na porta 8082.
4. **Falha:** se o endpoint de saúde não responder dentro do tempo limite, a etapa falha e os logs são impressos no job.

### Configurar o deploy local via GitHub Actions

Como os containers serão executados no computador local, os jobs de deploy precisam de um **self-hosted runner** nesse computador. Os jobs de build e teste continuam usando um runner hospedado pelo GitHub.

1. Publique o projeto em um repositório GitHub e use a branch `main`.
2. Instale e mantenha o Docker Desktop em execução no computador que hospedará os containers.
3. No repositório, abra **Settings → Secrets and variables → Actions → New repository secret** e crie `MYSQL_ROOT_PASSWORD`.
4. Em **Settings → Actions → Runners**, adicione um runner próprio. Configure as labels adicionais como `cidades-esg` e mantenha o serviço/processo do runner ativo.
5. O runner precisa ter Docker CLI, Docker Compose, `curl` e shell Bash disponíveis, além de permissão para controlar o Docker.
6. Faça push para `main` ou execute manualmente em **Actions → CI-CD Cidades ESG Inteligentes → Run workflow**.
7. Opcionalmente, configure proteção/aprovação para o environment `production` em **Settings → Environments**.

> O computador precisa estar ligado, conectado à internet e com o runner ativo para os deploys automáticos locais. Não exponha o Docker daemon à internet.

## Containerização

O `Dockerfile` usa build em múltiplas etapas: Maven/Java 21 compila o JAR; uma imagem Java 21 JRE executa somente o artefato. O processo é executado por usuário sem privilégios de root e a porta 8080 é exposta internamente.

O Compose cria quatro serviços por perfil: aplicação e banco para staging, aplicação e banco para produção. Staging usa a porta 8081 e produção a 8082. Cada banco possui volume persistente e cada ambiente tem sua própria rede bridge.

Comandos úteis:

```bash
docker build -t cidades-esg-inteligentes:local .
docker compose --profile staging up -d --build
docker compose --profile production up -d --build
docker compose ps
docker compose logs -f
```

## Prints do funcionamento e evidências

Insira capturas reais nestes espaços após executar a atividade:

1. **Build e testes:** GitHub → Actions → execução do workflow com `build-test` concluído.
2. **Deploy staging:** job `deploy-staging` concluído e resposta de `http://localhost:8081/api/health`.
3. **Deploy produção:** job `deploy-production` concluído e resposta de `http://localhost:8082/api/health`.
4. **Docker:** saída de `docker compose ps` mostrando aplicações e bancos em execução.
5. **CRUD:** resposta real de uma requisição POST e GET de `/api/cidades`.

Não inclua tokens, senhas ou outros segredos nas capturas.

## Desafios encontrados e como documentar

Como a implementação ainda precisa ser executada no ambiente final, registre os desafios reais encontrados durante a configuração, por exemplo:

- Configuração do Docker Desktop e dos volumes persistentes.
- Configuração do self-hosted runner para o deploy local.
- Configuração dos secrets do GitHub Actions.
- Verificação da conectividade entre aplicação e MySQL.

Para cada desafio, descreva o erro observado, a causa confirmada e a solução aplicada. Não declare como resolvido algo que ainda não foi testado.

## Checklist obrigatório de entrega

Marque cada item somente depois de verificar:

- [ ] Projeto compactado em ZIP com estrutura organizada.
- [ ] Dockerfile funcional, validado com build e execução.
- [ ] `docker-compose.yml` funcional, com volumes, variáveis e redes.
- [ ] Pipeline com build, testes e deploy em staging e produção.
- [ ] README com instruções e evidências reais.
- [ ] Documentação técnica PDF com evidências.
- [ ] Deploy executado e verificado nos ambientes staging e produção.

## Integrantes

- Nome(s): **preencher antes da entrega**
- Turma: **preencher, se solicitado**
- Disciplina: **preencher, se solicitado**
