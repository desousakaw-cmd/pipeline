# Cidades ESG Inteligentes

API REST para cadastro e consulta de indicadores ambientais, sociais e de governança (ESG) de cidades. Projeto acadêmico desenvolvido com Java 21, Spring Boot, MySQL, Docker Compose e GitHub Actions, com foco em automação de build, testes e implantação.

## 1. Objetivo do projeto

O projeto tem como objetivo demonstrar uma solução de software para organizar indicadores ESG de municípios, permitindo registrar informações, consultar cidades cadastradas e acompanhar indicadores relacionados à sustentabilidade, ao desenvolvimento social e à governança.

A aplicação utiliza uma API REST para disponibilizar as operações de gerenciamento de cidades e integra tecnologias de desenvolvimento backend, banco de dados, containerização e integração contínua.

## 2. Funcionalidades

* Cadastro de cidades.
* Consulta de todas as cidades cadastradas.
* Consulta individual por identificador.
* Atualização e exclusão de registros.
* Filtro de cidades por estado.
* Registro de indicadores ambientais, sociais e de governança.
* Registro do percentual de energia renovável.
* Endpoint de saúde para verificar a disponibilidade da aplicação.
* Execução da aplicação e do banco de dados por meio do Docker Compose.
* Pipeline de integração e entrega contínuas com GitHub Actions.

## 3. Tecnologias utilizadas

| Tecnologia           | Finalidade                                       |
| -------------------- | ------------------------------------------------ |
| Java 21              | Linguagem de programação                         |
| Spring Boot          | Desenvolvimento da API REST                      |
| Spring Web           | Criação dos endpoints HTTP                       |
| Spring Data JPA      | Persistência e acesso aos dados                  |
| Bean Validation      | Validação dos dados de entrada                   |
| Spring Boot Actuator | Recursos de monitoramento, conforme configurados |
| Maven                | Gerenciamento de dependências e build            |
| MySQL 8.4            | Banco de dados relacional                        |
| Docker               | Containerização da aplicação                     |
| Docker Compose       | Orquestração dos serviços                        |
| GitHub Actions       | Automação do pipeline CI/CD                      |
| JUnit 5              | Testes automatizados                             |

## 4. Estrutura do projeto

```text
cidades-esg-inteligentes/
├── .github/
│   └── workflows/
│       └── ci-cd.yml
├── src/
│   ├── main/
│   │   ├── java/br/com/cidadesesg/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   └── test/
├── .env.example
├── .dockerignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

A estrutura separa as responsabilidades da aplicação em camadas de controle, modelo, acesso aos dados e serviços. O diretório de testes contém os testes automatizados do projeto.

## 5. Configuração e execução local

### Pré-requisitos

* Java 21, caso a execução seja realizada diretamente na máquina.
* Maven, caso não seja utilizado o Maven Wrapper.
* Docker com suporte ao Docker Compose.
* Git, para clonar o repositório.

### 5.1. Compilar e executar os testes

Na raiz do projeto:

```bash
mvn clean verify
```

Esse comando solicita ao Maven a limpeza dos artefatos anteriores, a compilação da aplicação e a execução das verificações e dos testes configurados no projeto.

### 5.2. Iniciar o ambiente de staging

```bash
docker compose --profile staging up -d --build
```

Verifique os containers:

```bash
docker compose --profile staging ps
```

Se a configuração do Compose publicar a porta 8081, a API poderá ser acessada em:

`http://localhost:8081`

Para verificar o endpoint de saúde:

```bash
curl http://localhost:8081/api/health
```

A resposta esperada depende do formato implementado no endpoint.

## 6. API REST

A API disponibiliza operações de gerenciamento de cidades. Considerando a configuração local de staging na porta 8081, a URL-base é:

`http://localhost:8081`

| Método HTTP | Endpoint                 | Descrição                              |
| ----------- | ------------------------ | -------------------------------------- |
| GET         | `/api/health`            | Verifica a saúde da aplicação          |
| GET         | `/api/cidades`           | Lista as cidades cadastradas           |
| GET         | `/api/cidades?estado=SP` | Filtra cidades pelo estado             |
| GET         | `/api/cidades/{id}`      | Consulta uma cidade pelo identificador |
| POST        | `/api/cidades`           | Cadastra uma cidade                    |
| PUT         | `/api/cidades/{id}`      | Atualiza os dados de uma cidade        |
| DELETE      | `/api/cidades/{id}`      | Exclui uma cidade                      |

### 6.1. Exemplo de cadastro

A requisição abaixo ilustra o formato esperado para cadastrar uma cidade, conforme os campos descritos no projeto:

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

Os índices e o percentual de energia renovável são representados por valores entre 0 e 100, de acordo com as regras de validação previstas para o projeto. Os campos obrigatórios e os nomes definitivos devem corresponder à implementação da API.

### 6.2. Exemplo de consulta

## 7. Pipeline CI/CD com GitHub Actions

O projeto utiliza GitHub Actions para automatizar etapas do processo de integração e entrega contínuas.

O workflow está localizado em:

`.github/workflows/ci-cd.yml`

### 8.1. Build e testes

A etapa de build utiliza um runner hospedado pelo GitHub para obter o código, configurar o Java 21 e executar as verificações do Maven:

```bash
mvn -B clean verify
```

Essa etapa permite identificar erros de compilação e falhas nos testes antes de prosseguir para o deploy.

### 8.2. Deploy local com self-hosted runner

Para executar containers no computador local, foi configurado um runner self-hosted vinculado ao repositório. Esse runner permite que o GitHub Actions envie tarefas de implantação para uma máquina administrada pelo responsável pelo projeto.

O deploy de staging utiliza o Docker Compose para construir e iniciar os serviços:

```bash
docker compose --profile staging up -d --build
docker compose --profile staging ps
```

A execução depende de o computador estar ligado, conectado à internet, com o runner ativo e com permissões para controlar o Docker.

### 8.3. Gerenciamento de segredos

O pipeline utiliza o segredo `MYSQL_ROOT_PASSWORD`, cadastrado nas configurações do repositório em:

**Settings → Secrets and variables → Actions**

O segredo disponibiliza a senha necessária ao processo de deploy sem exigir que ela seja escrita diretamente no arquivo do workflow.

### 8.4. Fluxo de execução

O fluxo geral de CI/CD é:

1. O código é enviado ao repositório.
2. O GitHub Actions inicia o workflow configurado.
3. O runner hospedado pelo GitHub executa o build e os testes.
4. Se o build for aprovado, o job de deploy é liberado.
5. O self-hosted runner executa o deploy de staging com Docker Compose.
6. O estado dos serviços é consultado para auxiliar na verificação da implantação.

A execução efetiva de cada etapa deve ser demonstrada pelo histórico do workflow no GitHub Actions.

**Escopo do deploy:** a configuração de workflow descrita nesta documentação contempla o deploy de staging. O deploy automatizado de produção só deve ser considerado implementado após sua inclusão no workflow e a validação de sua execução.

## 9. Segurança e boas práticas

* Manter credenciais fora do controle de versão.
* Utilizar segredos do GitHub Actions para informações confidenciais.
* Não expor o Docker daemon diretamente à internet.
* Utilizar volumes persistentes quando os dados precisarem ser preservados.
* Consultar os logs dos containers para diagnosticar falhas.
* Verificar os resultados dos testes antes do deploy.
* Manter o runner self-hosted atualizado e ativo durante as execuções.

As evidências devem ser inseridas no documento final após a verificação de cada etapa. Não utilizar imagens ilustrativas como comprovação de uma execução real.

## 10. Desafios técnicos e soluções

Durante a configuração do ambiente, foi necessário ajustar o funcionamento do deploy automatizado e a comunicação entre o GitHub Actions e o runner local.

Um dos pontos identificados foi a necessidade de configurar corretamente a variável `MYSQL_ROOT_PASSWORD`. O workflow verificava se essa variável estava disponível antes de continuar. A ausência do segredo provocava a interrupção da etapa de deploy.

A solução foi cadastrar o segredo nas configurações do repositório, em **Settings → Secrets and variables → Actions**, utilizando o nome esperado pelo workflow.

Outro ponto importante foi a seleção do runner. O job de deploy precisava utilizar as labels disponíveis no runner self-hosted cadastrado no repositório. A configuração das labels do workflow e do runner deve ser compatível para que o job possa ser executado.

Esses ajustes demonstram a importância de configurar corretamente as variáveis de ambiente, as permissões e os requisitos do ambiente de execução em um pipeline de entrega contínua.

## 11. Conclusão

O projeto Cidades ESG Inteligentes reúne conceitos de desenvolvimento de APIs REST, persistência de dados, containerização e automação de processos de integração e entrega contínuas.

A solução permite organizar informações relacionadas a indicadores ESG de cidades e demonstra como tecnologias utilizadas no desenvolvimento backend podem ser integradas a um fluxo de build, testes e implantação.

A utilização do Docker Compose facilita a configuração do ambiente, enquanto o GitHub Actions automatiza etapas do processo de entrega. O self-hosted runner permite executar tarefas de deploy em uma máquina local, ampliando as possibilidades de integração entre o repositório e o ambiente de execução.