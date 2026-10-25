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
| Backend | Java 21, Spring Boot 3, Spring Data JPA, Spring Security (JWT), Flyway, Spring Data Elasticsearch |
| Database | MySQL 8 (source of truth) |
| Search | Elasticsearch 8 |
| Analytics | Kibana 8 |
| Frontend | React 18, Vite, TypeScript, React Router, TanStack Query, Axios |
| Infra | Docker Compose (MySQL, Elasticsearch, Kibana) |

## Repository layout
```
Restaurant-Review/
├── README.md            # this file
├── CLAUDE.md            # rules for Claude / AI assistants
├── AGENT.md             # skills catalog for agents
├── docker-compose.yml   # mysql, elasticsearch, kibana        (planned)
├── docs/
│   ├── PLAN.md          # roadmap, API outline, search design
│   └── ER-diagram.md    # database ER diagram (Mermaid)
├── backend/             # Spring Boot API                      (planned)
└── frontend/            # React + Vite app                     (planned)
```

## Prerequisites
- JDK 21
- Node.js 20+
- Docker Desktop

## Quick start
> These steps apply once the base template is in place.

```bash
# 1. Start infrastructure
docker compose up -d

# 2. Run the backend (http://localhost:8080)
cd backend
./mvnw spring-boot:run

# 3. Run the frontend (http://localhost:5173)
cd frontend
npm install
npm run dev
```

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
