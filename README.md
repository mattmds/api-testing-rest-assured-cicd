## 🚀 Framework de testes Automatizados de API para Sistema de reserva de hotel (Restful-Booker)

![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Rest Assured](https://img.shields.io/badge/Rest_Assured-5.4.0-blue?style=for-the-badge)
![JUnit 5](https://img.shields.io/badge/JUnit-5-green?style=for-the-badge&logo=junit5)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apachemaven)
![GitHub Actions](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions)

Este repositório contém o framework de automação de testes de API para a [Restful-Booker API](https://restful-booker.herokuapp.com/), uma API que simula um sistema de reservas de hotel, desenvolvido com foco em boas práticas de **Engenharia de Qualidade**. O projeto utiliza **Java 21**, **Rest Assured**, **JUnit 5** e segue o padrão arquitetural **Service Object Pattern**.

---

### 📋 Cenários Cobertos

- [x] **Criar uma Reserva (POST):** Validação de criação com sucesso (Status 200), estrutura do payload de resposta e validação rigorosa de contrato com JSON Schema.
- [x] **Consulta de Reserva (GET):** Validação de busca por ID e filtro de dados.
- [x] **Consulta de Reserva Inexistente (GET):** Validação de busca por ID inexistente (404).
- [x] **Delete de Reserva com Autenticação (POST):** Apaga a reserva criada com uso de token de acesso para operações administrativas.

---

### 🏗️ Arquitetura e Padrões de Projeto

O projeto foi desenhado para garantir sustentabilidade, desacoplamento e facilidade de manutenção:

* **Service Object Pattern:** Abstração das chamadas HTTP da API em classes de serviço (`AuthAPI`, `BookingAPI`), mantendo os testes focados apenas nas asserções e regras de negócio.
* **Java 21 Text Blocks:** Utilização de `"""` para gerenciamento limpo e legível de payloads JSON estáticos e dinâmicos dentro das classes de massa (`data/`).
* **Validação de JSON Schema:** Garantia de contrato das respostas da API utilizando o `json-schema-validator` com schemas estáticos armazenados na pasta `resources`.
* **Gerenciamento de Dependências com Maven:** Controle centralizado das bibliotecas no `pom.xml`.

---

### 📁 Estrutura do Projeto

```
api-testing-rest-assured-cicd/
├── .github/
│   └── workflows/
│       └── api-tests.yml          # Esteira de CI para execução automática via GitHub Actions
├── src/
│   └── test/
│       ├── java/                  
│       │   ├── api/               
│       │   │   ├── AuthAPI.java           # Encapsula requisição de autenticação e geração de Token
│       │   │   ├── BaseAPI.java           # Especificações globais (BaseURI, Headers Content-Type e Accept)
│       │   │   └── BookingAPI.java        # Métodos para a entidade /booking (POST, GET, DELETE)
│       │   ├── data/              
│       │   │   └── BookingData.java       # Provedor dos payloads em JSON
│       │   └── tests/                 
│       │       └── BookingTest.java       # Execução dos cenários, @Order e logs no console
│       └── resources/             
│           └── schemas/
│               └── booking-schema.json    # Schema em JSON puro para validação de contrato
├── .gitignore                     
├── pom.xml                        # Gerenciador de dependências Maven 
└── README.md                     
```

---

### 🛠️ Pré-requisitos

Para rodar este projeto localmente, certifique-se de ter instalado:

1. **JDK 21** ou superior (Recomendado: [Eclipse Temurin 21](https://adoptium.net/)).
2. **Apache Maven 3.8+** (opcional se executado via IDE).
3. **Git** para clonar o repositório.

#### Verificando as versões no terminal:
```bash
java -version
mvn -version
```

---

### 🧪 Como Executar os Testes

#### Via Linha de Comando 

* **Executar todos os testes da aplicação:**
  ```bash
  mvn test
  ```

* **Executar apenas a suíte de reservas (`BookingTest`):**
  ```bash
  mvn test -Dtest=BookingTest
  ```

#### Via IDE (IntelliJ IDEA / Eclipse / VS Code)

1. Abra a pasta raiz do projeto na sua IDE.
2. Aguarde a sincronização automática do Maven.
3. Navegue até `src/test/java/tests/BookingTest.java`.
4. Clique com o botão direito na classe ou método de teste e selecione **Run 'BookingTest'**.

---

### 🔄 Integração Contínua (CI) e Quality Gate

A nível de validação e testes do Quality Gate, o repositório do próprio Projeto de Automação foi utilizado no workflow e integrado com o **GitHub Actions** para execução automatizada dos testes a cada `push` ou `pull_request` enviado, garantindo validação contínua da qualidade do código.
No repositório há um branch chamada test-quality-gate, que ao receber o push de alterações, dispara o

#### Destaques da Pipeline (`.github/workflows/api-tests.yml`):
* **⚙️Ambiente Isolado:** Execução em container Ubuntu rodando **Java 21 (Eclipse Temurin)** e Maven..
* **🚀Performance com Cache:** Cache automático das dependências `.m2` do Maven para acelerar o tempo de build.
* **🧪 Execução Automatizada:** A suíte `BookingTest` é executada automaticamente pelo Maven através do comando `mvn test`.
* **🛡️ Quality Gate:** O resultado dos testes do workflow (disparados pelo push) é utilizado como critério obrigatório para integração na `main`. Pull Requests com falhas nos testes são bloqueadas até que o pipeline seja aprovado.
* **📊 Relatórios de Teste:** Os relatórios gerados pelo **Maven Surefire** são armazenados como artefatos do workflow, permitindo análise dos resultados diretamente na aba **Actions** do GitHub, mesmo quando os testes falham.
* **🌿 Proteção da Branch Principal:** A branch `main` possui uma **Ruleset** que exige a aprovação do Quality Gate antes da integração das alterações.

#### Fluxo da Pipeline

`Push` → `GitHub Actions` → `Maven + Rest Assured` → `Testes Automatizados` → `Quality Gate` → `Pull Request` → `Merge na main`

**Resultado:** alterações que introduzem falhas nos testes são automaticamente identificadas e impedidas de serem integradas à branch principal.
* **Relatório de Artefatos:** Em caso de falha ou sucesso, o relatório Surefire é empacotado e disponibilizado para download na aba **Actions** do GitHub.

---

