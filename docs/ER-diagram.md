# ER Diagram

Database: **MySQL 8**. This diagram is the reference for the Liquibase changesets in `backend/src/main/resources/db/changelog/`. Any schema change must update this file (see [CLAUDE.md](../CLAUDE.md)).

```mermaid
erDiagram
    USERS ||--o{ REVIEWS : writes
    USERS ||--o{ REVIEW_VOTES : casts
    USERS ||--o{ REVIEW_REPORTS : files
    USERS ||--o{ RESTAURANT_PHOTOS : uploads
    USERS ||--o{ RESTAURANTS : creates
    RESTAURANTS ||--o{ REVIEWS : receives
    RESTAURANTS ||--o{ RESTAURANT_PHOTOS : has
    RESTAURANTS ||--o{ OPENING_HOURS : has
    RESTAURANTS ||--o{ MENU_CATEGORIES : has
    RESTAURANTS ||--o{ RESTAURANT_CUISINES : tagged
    CUISINES ||--o{ RESTAURANT_CUISINES : tags
    MENU_CATEGORIES ||--o{ MENU_ITEMS : contains
    REVIEWS ||--o{ REVIEW_PHOTOS : has
    REVIEWS ||--o{ REVIEW_VOTES : receives
    REVIEWS ||--o{ REVIEW_REPORTS : receives

    USERS {
        bigint id PK
        varchar email UK
        varchar password_hash
        varchar display_name
        varchar avatar_url
        enum role "USER, ADMIN"
        enum status "ACTIVE, SUSPENDED"
        datetime created_at
        datetime updated_at
    }
    RESTAURANTS {
        bigint id PK
        varchar name
        varchar slug UK
        text description
        varchar phone
        varchar website
        varchar address_line
        varchar city
        varchar state
        varchar postal_code
        varchar country
        decimal latitude
        decimal longitude
        tinyint price_level "1-4"
        decimal avg_rating "denormalized"
        int review_count "denormalized"
        bigint created_by FK
        datetime created_at
        datetime updated_at
    }
    CUISINES {
        bigint id PK
        varchar name UK
    }
    RESTAURANT_CUISINES {
        bigint restaurant_id PK,FK
        bigint cuisine_id PK,FK
    }
    OPENING_HOURS {
        bigint id PK
        bigint restaurant_id FK
        tinyint day_of_week "0-6"
        time open_time
        time close_time
        boolean closed
    }
    MENU_CATEGORIES {
        bigint id PK
        bigint restaurant_id FK
        varchar name
        int sort_order
    }
    MENU_ITEMS {
        bigint id PK
        bigint category_id FK
        varchar name
        text description
        decimal price
        boolean vegetarian
        boolean available
        varchar image_url
    }
    REVIEWS {
        bigint id PK
        bigint restaurant_id FK
        bigint user_id FK
        tinyint rating "1-5"
        varchar title
        text body
        date visit_date
        enum status "PUBLISHED, HIDDEN, REMOVED"
        int helpful_count "denormalized"
        datetime created_at
        datetime updated_at
    }
    REVIEW_PHOTOS {
        bigint id PK
        bigint review_id FK
        varchar url
        varchar caption
        datetime created_at
    }
    RESTAURANT_PHOTOS {
        bigint id PK
        bigint restaurant_id FK
        bigint uploaded_by FK
        varchar url
        varchar caption
        boolean is_cover
        datetime created_at
    }
    REVIEW_VOTES {
        bigint review_id PK,FK
        bigint user_id PK,FK
        datetime created_at
    }
    REVIEW_REPORTS {
        bigint id PK
        bigint review_id FK
        bigint reporter_id FK
        enum reason "SPAM, OFFENSIVE, FAKE, OTHER"
        text details
        enum status "OPEN, RESOLVED, DISMISSED"
        bigint resolved_by FK
        datetime resolved_at
        datetime created_at
    }
```

## Table notes
| Table | Purpose | Notes |
|---|---|---|
| `users` | Accounts | `email` unique; password stored as a BCrypt hash; `status=SUSPENDED` blocks login |
| `restaurants` | Listings | `slug` unique; `avg_rating` / `review_count` updated whenever a published review changes |
| `cuisines` / `restaurant_cuisines` | Many-to-many cuisine tags | Composite PK on the join table |
| `opening_hours` | Weekly schedule | `day_of_week` runs 0=Mon … 6=Sun; `closed=true` ignores the times |
| `menu_categories` / `menu_items` | Menus | Ordered by `sort_order` |
| `reviews` | User reviews | `UNIQUE(restaurant_id, user_id)`; only `PUBLISHED` reviews count toward ratings |
| `review_photos` / `restaurant_photos` | Images | `url` points at a `StorageService` location; one `is_cover` per restaurant |
| `review_votes` | "Helpful" votes | Composite PK, so a user votes once per review; updates `reviews.helpful_count` |
| `review_reports` | Moderation queue | `UNIQUE(review_id, reporter_id)`; `resolved_by` references `users` (an admin) |

## Indexes
- Every FK column is indexed.
- `restaurants(city)`, `restaurants(avg_rating)`
- `reviews(restaurant_id, created_at)`, `reviews(user_id)`
- `review_reports(status, created_at)`

## Future extensions (not in MVP)
- `restaurant_owners` (user ↔ restaurant claim) and `review_replies` for owner responses.
