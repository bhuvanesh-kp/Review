#!/bin/bash
# Runs once, on the first start of an empty mysql_data volume (docker-entrypoint-initdb.d).
# Creates the application database if it does not exist and grants the app user access to it.
# Tables are NOT created here: Liquibase owns the schema (backend/src/main/resources/db/changelog).
set -euo pipefail

: "${MYSQL_DATABASE:?MYSQL_DATABASE must be set}"
: "${MYSQL_USER:?MYSQL_USER must be set}"

echo "[init] ensuring database '${MYSQL_DATABASE}' exists and '${MYSQL_USER}' has access"

mysql --protocol=socket -uroot -p"${MYSQL_ROOT_PASSWORD}" <<SQL
CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'%';
FLUSH PRIVILEGES;
SQL
