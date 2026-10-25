-- V1: initial schema. Mirrors docs/ER-diagram.md. Never edit once applied; add V2+ instead.

CREATE TABLE users (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name  VARCHAR(100) NOT NULL,
    avatar_url    VARCHAR(512),
    role          ENUM('USER', 'ADMIN')         NOT NULL DEFAULT 'USER',
    status        ENUM('ACTIVE', 'SUSPENDED')   NOT NULL DEFAULT 'ACTIVE',
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE restaurants (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(200) NOT NULL,
    slug         VARCHAR(220) NOT NULL,
    description  TEXT,
    phone        VARCHAR(30),
    website      VARCHAR(255),
    address_line VARCHAR(255),
    city         VARCHAR(100) NOT NULL,
    state        VARCHAR(100),
    postal_code  VARCHAR(20),
    country      VARCHAR(100) NOT NULL,
    latitude     DECIMAL(9, 6),
    longitude    DECIMAL(9, 6),
    price_level  TINYINT,
    avg_rating   DECIMAL(3, 2) NOT NULL DEFAULT 0.00,
    review_count INT           NOT NULL DEFAULT 0,
    created_by   BIGINT,
    created_at   DATETIME(6)  NOT NULL,
    updated_at   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_restaurants_slug UNIQUE (slug),
    CONSTRAINT fk_restaurants_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT ck_restaurants_price_level CHECK (price_level BETWEEN 1 AND 4),
    INDEX idx_restaurants_city (city),
    INDEX idx_restaurants_avg_rating (avg_rating),
    INDEX idx_restaurants_created_by (created_by)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE cuisines (
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_cuisines_name UNIQUE (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE restaurant_cuisines (
    restaurant_id BIGINT NOT NULL,
    cuisine_id    BIGINT NOT NULL,
    PRIMARY KEY (restaurant_id, cuisine_id),
    CONSTRAINT fk_restaurant_cuisines_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT fk_restaurant_cuisines_cuisine FOREIGN KEY (cuisine_id) REFERENCES cuisines (id) ON DELETE CASCADE,
    INDEX idx_restaurant_cuisines_cuisine (cuisine_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE opening_hours (
    id            BIGINT  NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT  NOT NULL,
    day_of_week   TINYINT NOT NULL,
    open_time     TIME,
    close_time    TIME,
    closed        BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_opening_hours_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT ck_opening_hours_day CHECK (day_of_week BETWEEN 0 AND 6),
    INDEX idx_opening_hours_restaurant (restaurant_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE menu_categories (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT       NOT NULL,
    name          VARCHAR(100) NOT NULL,
    sort_order    INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_menu_categories_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    INDEX idx_menu_categories_restaurant (restaurant_id, sort_order)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE menu_items (
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    category_id BIGINT         NOT NULL,
    name        VARCHAR(150)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    vegetarian  BOOLEAN        NOT NULL DEFAULT FALSE,
    available   BOOLEAN        NOT NULL DEFAULT TRUE,
    image_url   VARCHAR(512),
    PRIMARY KEY (id),
    CONSTRAINT fk_menu_items_category FOREIGN KEY (category_id) REFERENCES menu_categories (id) ON DELETE CASCADE,
    INDEX idx_menu_items_category (category_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE reviews (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT       NOT NULL,
    user_id       BIGINT       NOT NULL,
    rating        TINYINT      NOT NULL,
    title         VARCHAR(200),
    body          TEXT         NOT NULL,
    visit_date    DATE,
    status        ENUM('PUBLISHED', 'HIDDEN', 'REMOVED') NOT NULL DEFAULT 'PUBLISHED',
    helpful_count INT          NOT NULL DEFAULT 0,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_reviews_restaurant_user UNIQUE (restaurant_id, user_id),
    CONSTRAINT fk_reviews_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),
    INDEX idx_reviews_restaurant_created (restaurant_id, created_at),
    INDEX idx_reviews_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE review_photos (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    review_id  BIGINT       NOT NULL,
    url        VARCHAR(512) NOT NULL,
    caption    VARCHAR(255),
    created_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_review_photos_review FOREIGN KEY (review_id) REFERENCES reviews (id) ON DELETE CASCADE,
    INDEX idx_review_photos_review (review_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE restaurant_photos (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT       NOT NULL,
    uploaded_by   BIGINT,
    url           VARCHAR(512) NOT NULL,
    caption       VARCHAR(255),
    is_cover      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_restaurant_photos_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT fk_restaurant_photos_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_restaurant_photos_restaurant (restaurant_id),
    INDEX idx_restaurant_photos_uploaded_by (uploaded_by)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE review_votes (
    review_id  BIGINT      NOT NULL,
    user_id    BIGINT      NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (review_id, user_id),
    CONSTRAINT fk_review_votes_review FOREIGN KEY (review_id) REFERENCES reviews (id) ON DELETE CASCADE,
    CONSTRAINT fk_review_votes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_review_votes_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE review_reports (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    review_id   BIGINT      NOT NULL,
    reporter_id BIGINT      NOT NULL,
    reason      ENUM('SPAM', 'OFFENSIVE', 'FAKE', 'OTHER')  NOT NULL,
    details     TEXT,
    status      ENUM('OPEN', 'RESOLVED', 'DISMISSED')       NOT NULL DEFAULT 'OPEN',
    resolved_by BIGINT,
    resolved_at DATETIME(6),
    created_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_review_reports_review_reporter UNIQUE (review_id, reporter_id),
    CONSTRAINT fk_review_reports_review FOREIGN KEY (review_id) REFERENCES reviews (id) ON DELETE CASCADE,
    CONSTRAINT fk_review_reports_reporter FOREIGN KEY (reporter_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_review_reports_resolved_by FOREIGN KEY (resolved_by) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_review_reports_status_created (status, created_at),
    INDEX idx_review_reports_reporter (reporter_id),
    INDEX idx_review_reports_resolved_by (resolved_by)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
