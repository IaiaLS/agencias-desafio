# Agência API

Cadastro de agências e cálculo de distâncias até um ponto informado. Projeto em **Java 21** com **Spring Boot 3.5.x**, arquitetura **Hexagonal**, **H2** no `dev/test`, e migrações com **Flyway**.

## Sumário
- [Arquitetura](#arquitetura)
- [Stack](#stack)
- [Endpoints](#endpoints)
- [Como rodar](#como-rodar)
- [Perfis / Config](#perfis--config)
- [Banco de dados e migrações](#banco-de-dados-e-migrações)
- [Swagger / H2 Console](#swagger--h2-console)
- [Logs & Exceções](#logs--exceções)
- [Testes](#testes)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Roadmap](#roadmap)

---

## Arquitetura

Arquitetura Hexagonal (Ports & Adapters):

```
domain/
  model/        -> Coordinate, Agency, AgencyDistance
  port/
    in/         -> RegisterAgencyUseCase, ComputeDistancesUseCase
    out/        -> AgencyRepositoryPort
  service/      -> DistanceService (euclidiana)
application/
  usecase/      -> RegisterAgencyService, ComputeDistancesService
adapters/
  in/web/       -> AgencyController + DTOs + ApiExceptionHandler
  out/persistence/
                 -> AgencyJpaEntity, SpringDataAgencyRepository, AgencyRepositoryAdapter
```

- Distância: **Euclidiana** (`Math.hypot`).
- Ordenação: crescente por distância, com **desempate por `agencyId`**.
- Transações: `@Transactional` no caso de uso de escrita; `readOnly = true` no de leitura.
- `name` da agência é **opcional**.

---

## Stack

- Java 21, Spring Boot 3.5.x
- Spring Web, Spring Data JPA
- H2 (dev/test), **Flyway** (migrações)
- springdoc-openapi (Swagger)
- JUnit 5, Mockito, Spring Boot Test

---

## Endpoints

### 1) Cadastrar agência

```
POST /desafio/cadastrar
Content-Type: application/json
```

**Request**
```json
{
  "posX": 10.0,
  "posY": -5.0,
  "name": "Agência Centro" // opcional
}
```

**Response 201**
```json
{
  "id": 1,
  "posX": 10.0,
  "posY": -5.0,
  "name": "Agência Centro"
}
```

### 2) Distâncias a partir de um ponto

```
GET /desafio/distancia?posX={x}&posY={y}
```

**Response 200**
```json
[
  { "agencyId": 2, "distance": "1.41" },
  { "agencyId": 3, "distance": "5.00" },
  { "agencyId": 1, "distance": "10.00" }
]
```

> **Formato legado (opcional, se exposto):**  
> `GET /desafio/distancia/v1` →  
> `{"AGENCIA 2":"distancia = 1.41","AGENCIA 3":"distancia = 5.00",...}`

---

## Como rodar

Pré-requisitos: **Java 21**.

### 1) Dev (H2 em memória)

```bash
./mvnw clean package
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
# ou
java -jar target/agencia-api-*.jar --spring.profiles.active=dev
```

- App: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`  
  JDBC URL: `jdbc:h2:mem:agencia_db` | user: `sa` | pass: `sa`

### 2) Testes (unitários e integração)

```bash
./mvnw test
```

---

## Perfis / Config

Arquivos:
```
src/main/resources/
  application.yml         # base comum
  application-dev.yml     # dev (H2, Swagger ligado)
  application-test.yml    # test (H2 create-drop)
  application-prod.yml    # prod (Postgres, Swagger off)
```

Variáveis podem ser sobrepostas via ambiente ou `.env` (se usando spring-dotenv) / `.env.properties` (via `spring.config.import`). Ex.:  
`SPRING_PROFILES_ACTIVE=dev`, `CORS_ALLOWED_ORIGINS=*`, etc.

---

## Banco de dados e migrações

- **Dev/Test:** H2 em memória (`jdbc:h2:mem:agencia_db`).
- **Migrações:** Flyway em `src/main/resources/db/migration`.

Scripts iniciais:
- `V1__init.sql` – cria a tabela `agencies (id, pos_x, pos_y)`.
- `V2__add_agency_name_nullable.sql` – adiciona `name` **nullable**.

> Em `prod`, use Postgres e `ddl-auto: validate`, deixando **apenas** o Flyway gerir o schema.

---

## Swagger / H2 Console

- **Swagger/OpenAPI** (dev):
    - JSON: `/v3/api-docs`
    - UI: `/swagger-ui.html`
- **H2 Console** (dev): `/h2-console`
    - URL: `jdbc:h2:mem:agencia_db` | user `sa`, senha `sa`

---

## Logs & Exceções

- Logs principais nos **casos de uso** e **controller** (níveis `INFO`/`DEBUG`).
- **Correlation ID** opcional via header `X-Request-Id` (se configurado um `Filter` de MDC).
- Exceções padronizadas via `@RestControllerAdvice` retornando **Problem Details (RFC 7807)**:
    - 400: validação de body/params;
    - 409: `DataIntegrityViolationException`;
    - 500: fallback.

Exemplo de 400:
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Corpo da requisição inválido",
  "path": "/desafio/cadastrar",
  "errors": [
    {"field":"posX","message":"must not be null"}
  ]
}
```

---

## Testes

- **Unitários** (JVM pura, sem Spring):
    - `DistanceServiceTest`
    - `RegisterAgencyServiceTest` (mock do `AgencyRepositoryPort`)
    - `ComputeDistancesServiceTest` (repo mock + `DistanceService` real, empate de distância)

- **Integração** (`@SpringBootTest + @AutoConfigureMockMvc`, H2):
    - fluxo cadastro → distância (ordenado e formatado)
    - 400 para body inválido e params ausentes
    - vazio quando não há agências

Rodar:
```bash
./mvnw test
```

---

## Estrutura de pastas

```
src/
  main/java/com/santander/desafio/agencia_api/
    AgenciaApiApplication.java
    domain/
      model/ (Coordinate, Agency, AgencyDistance)
      port/in/ (RegisterAgencyUseCase, ComputeDistancesUseCase)
      port/out/ (AgencyRepositoryPort)
      service/ (DistanceService)
    application/usecase/
      RegisterAgencyService.java
      ComputeDistancesService.java
    adapters/
      in/web/
        AgencyController.java
        dto/ (RegisterAgencyRequest, RegisterAgencyResponse, AgencyDistanceView)
        exception/ (ApiExceptionHandler)
      out/persistence/
        AgencyJpaEntity.java
        SpringDataAgencyRepository.java
        AgencyRepositoryAdapter.java
  main/resources/
    application.yml
    application-dev.yml
    application-test.yml
    application-prod.yml
    db/migration/V1__init.sql
    db/migration/V2__add_agency_name_nullable.sql
  test/java/com/santander/desafio/agencia_api/
    domain/service/DistanceServiceTest.java
    application/usecase/...
    integration/ApiIntegrationTest.java
  test/resources/
    application-test.yml
```

---

