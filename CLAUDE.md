# CLAUDE.md — Rules for Claude

These rules apply to Claude and any AI assistant working in this repository. Reusable task recipes live in [AGENT.md](AGENT.md).

## Project summary
A restaurant review platform: restaurants, reviews and ratings, photos, menus, search, and moderation.
- **Backend:** Spring Boot 3 (Java 21, Maven) in `backend/`
- **Frontend:** React 18 + Vite + TypeScript in `frontend/`
- **Data:** MySQL 8 (source of truth), Elasticsearch 8 (search index), Kibana 8 (analytics)
- **Docs:** `docs/PLAN.md` (roadmap) and `docs/ER-diagram.md` (schema)

## Key commands
```bash
docker compose up -d                 # start MySQL, Elasticsearch, Kibana
cd backend  && ./mvnw spring-boot:run  # run API on :8080
cd backend  && ./mvnw verify           # build + tests
cd frontend && npm run dev             # run UI on :5173
cd frontend && npm run build           # type-check + build
cd frontend && npm test                # Vitest
```

## Architecture rules
- Use feature-based packages under `com.restaurantreview.<feature>` (`user`, `restaurant`, `menu`, `review`, `photo`, `moderation`, `search`). Shared code goes in `common`, `config` and `security`.
- Keep the layering **controller → service → repository**. Controllers hold no business logic; repositories hold no business rules.
- Use **DTOs at the API boundary**. Never return JPA entities from controllers.
- **MySQL is the source of truth.** Elasticsearch is a derived index: update it only from `@TransactionalEventListener(phase = AFTER_COMMIT)` listeners in the `search` package, and keep a full reindex path working.
- Denormalized counters (`avg_rating`, `review_count`, `helpful_count`) are updated in the service layer, in the same transaction as the change that affects them.
- Photo storage goes through the `StorageService` interface. Don't write to disk directly from other code.

## Database rules
- Change the schema **only through a new Flyway migration** (`V<n>__<description>.sql`). **Never edit a migration that has been applied.**
- Every schema change must update `docs/ER-diagram.md` in the same change.
- Keep `spring.jpa.hibernate.ddl-auto=validate`. Entities must match the migrations.
- Name tables and columns in `snake_case`, use plural table names, and give every table a `bigint` surrogate key. Every foreign key gets an index.

## Coding conventions
**Java**
- Use constructor injection (no field `@Autowired`).
- Validate request DTOs with Bean Validation (`@Valid`, `@NotBlank`, `@Min/@Max`, …).
- Throw domain exceptions and map them to an `ApiError` response in `GlobalExceptionHandler`.
- Paginate list endpoints with `Pageable`. Never return unbounded lists.
- Prefer Java records for DTOs.

**TypeScript / React**
- Keep `strict` mode on. Avoid `any`.
- Make every HTTP call through `src/api/client.ts`. Fetch server state with TanStack Query.
- Use function components and hooks. Name files in PascalCase for components and camelCase for utilities.

## Security rules
- No secrets in code or committed files. Use environment variables, and commit only `.env.example`.
- Hash passwords with BCrypt. Never log passwords, tokens or personal data.
- Check authentication **and** ownership/role on every mutating endpoint. Admin endpoints live under `/api/admin/**` and require `ADMIN`.
- Validate uploads by MIME type, extension and size, and store them under generated file names.
- Use parameterized queries only (JPA/Spring Data). No string-concatenated SQL or ES queries.

## Workflow rules
- Read the relevant code and docs before changing anything. For large or cross-cutting changes, propose a plan first.
- Keep diffs focused on the task. Don't refactor unrelated code.
- Add or update tests with every feature or bug fix.
- Update `README.md` / `docs/` when behavior, setup, or the API changes.
- Run the build and tests before reporting a task as done, and report failures honestly.
- **Commit or push only when the user asks.** Branch off `main` for feature work.
- When requirements are ambiguous, ask instead of guessing.
