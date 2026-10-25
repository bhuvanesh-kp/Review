# Restaurant Review Platform

A full-stack platform for discovering restaurants, rating and reviewing them, sharing photos, and browsing menus. Admins moderate reported content. Elasticsearch powers search and Kibana provides analytics.

> **Status:** M0 (planning and bootstrap). The plan and ER diagram are up for review; the backend/frontend base template comes next.

## Features (MVP)
- **Accounts:** register and log in with JWT; roles `USER` and `ADMIN`.
- **Restaurants:** listings with address, geo location, cuisines, price level, opening hours.
- **Reviews:** 1–5 star ratings with written reviews (one per user per restaurant) and an aggregated average rating.
- **Photos & menus:** restaurant and review photos; menus organized by category.
- **Search:** full-text search with filters (city, cuisine, rating, price), geo distance and sorting, via Elasticsearch.
- **Moderation:** report reviews, an admin report queue, hide/remove reviews, suspend users, "helpful" votes.

## Tech stack
| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 4.1, Gradle (Groovy DSL), Spring Data JPA, Spring Security (JWT), Liquibase, Spring Data Elasticsearch, springdoc-openapi |
| Database | MySQL 8.4 (source of truth) |
| Search | Elasticsearch 9 |
| Analytics | Kibana 9 |
| Frontend | React 18, Vite, TypeScript, React Router, TanStack Query, Axios |
| Infra | Docker Compose (MySQL, Elasticsearch, Kibana) |

## Repository layout
```
Restaurant-Review/
├── README.md            # this file
├── CLAUDE.md            # rules for Claude / AI assistants
├── AGENT.md             # skills catalog for agents
├── docker-compose.yml   # mysql, elasticsearch, kibana
├── .env.example         # environment variables template
├── docs/
│   ├── PLAN.md          # roadmap, API outline, search design
│   └── ER-diagram.md    # database ER diagram (Mermaid)
├── backend/             # Spring Boot API (Gradle)
└── frontend/            # React + Vite app                     (planned)
```

## Prerequisites
- JDK 17+ to launch Gradle (the build compiles with a JDK 21 toolchain, downloaded automatically if missing)
- Node.js 20+
- Docker Desktop

## Quick start
> Steps 1–2 work today. Step 3 applies once the frontend exists.

```bash
# 1. Configure and start infrastructure
cp .env.example .env
docker compose up -d

# 2. Run the backend (http://localhost:8080)
cd backend
./gradlew bootRun        # Windows: gradlew.bat bootRun

# 3. Run the frontend (http://localhost:5173)
cd frontend
npm install
npm run dev
```

## Infrastructure
`docker-compose.yml` runs MySQL 8.4, Elasticsearch 9.3 and Kibana 9.3. Data persists in the named volumes `mysql_data` and `es_data`.

```bash
docker compose ps            # status and health of each service
docker compose logs -f kibana
docker compose down          # stop (keeps data)
docker compose down -v       # stop AND delete all data volumes
```

### Database initialization
Database setup happens in three layers:

| When | What | Where |
|---|---|---|
| First start of an empty `mysql_data` volume | Creates the database (utf8mb4) if missing and grants the app user access | `docker/mysql/init/01-create-database.sh` |
| Every `docker compose up` | One-shot `liquibase` service waits for MySQL to be healthy, applies pending changesets, then exits | `backend/src/main/resources/db/changelog/` |
| Every app start | `createDatabaseIfNotExist=true` recreates a missing database; Spring Boot's Liquibase applies pending changesets | `application.yml` |

The compose service and the app use the same changelog, so whichever runs second reports `Run: 0`.

```bash
docker compose up liquibase  # re-apply / check the schema on demand
docker compose logs liquibase
```

MySQL init scripts only run on a fresh volume. To re-run them, use `docker compose down -v`, which **deletes all data**.

- Elasticsearch security is **disabled for local development only**.
- Kibana starts only after Elasticsearch reports healthy, so its first start can take a minute or two.
- **Windows/WSL2:** give Docker at least 4 GB of memory. If Elasticsearch exits with a `vm.max_map_count` error, run `wsl -d docker-desktop sysctl -w vm.max_map_count=262144`.

## Service URLs
| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Elasticsearch | http://localhost:9200 |
| Kibana | http://localhost:5601 |
| MySQL | localhost:3306 |

## Documentation
- [Project plan](docs/PLAN.md)
- [ER diagram](docs/ER-diagram.md)
- [Claude rules](CLAUDE.md)
- [Agent skills](AGENT.md)

## Milestones
| # | Scope |
|---|---|
| M0 | Plan, ER diagram, base template |
| M1 | Auth, restaurant CRUD, reviews and rating aggregation |
| M2 | Photos (upload, validation, gallery) and menus |
| M3 | Elasticsearch search UI and Kibana dashboards |
| M4 | Moderation (reports, hide/remove, suspend) and helpful votes |
| M5 | Hardening: tests, pagination, rate limiting, CI |
| Later | Owner claim/reply, S3 storage, notifications |
