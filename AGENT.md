# AGENT.md — Skills Catalog

Reusable, step-by-step recipes for agents working on this repository. Always follow the rules in [CLAUDE.md](CLAUDE.md). Each skill lists a **trigger**, **steps**, the **files it touches**, and a **done checklist**.

---

## 1. `add-entity`
**Trigger:** a new table or domain object is needed.

**Steps**
1. Design the table: columns, keys, constraints, indexes.
2. Add a Flyway migration `backend/src/main/resources/db/migration/V<n>__<description>.sql`.
3. Create the JPA entity in `com.restaurantreview.<feature>` (extend `BaseEntity` where it applies).
4. Create a Spring Data repository.
5. Update `docs/ER-diagram.md`: the entity block, its relationships, and any constraint notes.

**Files:** `db/migration/*`, `<feature>/<Entity>.java`, `<feature>/<Entity>Repository.java`, `docs/ER-diagram.md`

**Done when**
- [ ] The app starts with `ddl-auto=validate`
- [ ] The ER diagram matches the migration
- [ ] Every FK is indexed and unique constraints are defined

---

## 2. `add-endpoint`
**Trigger:** a new REST operation is needed.

**Steps**
1. Define request/response DTOs (records) with Bean Validation.
2. Implement the logic in the feature's service, with `@Transactional` where needed.
3. Add the controller method. Use the `/api/...` path convention and `/api/admin/...` for admin operations.
4. Enforce authorization (role and ownership).
5. Map new error cases in `GlobalExceptionHandler`.
6. Write tests: a service unit test and a controller test (`@WebMvcTest` or an integration test).

**Files:** `<feature>/dto/*`, `<feature>/*Service.java`, `<feature>/*Controller.java`, tests

**Done when**
- [ ] Shows up in Swagger UI with correct schemas
- [ ] Invalid input returns 400 with an `ApiError`; unauthorized calls return 401/403
- [ ] List endpoints are paginated
- [ ] Tests pass (`./mvnw verify`)

---

## 3. `add-react-page`
**Trigger:** a new screen in the frontend.

**Steps**
1. Add typed API functions in `frontend/src/api/` that use `client.ts`.
2. Create query/mutation hooks with TanStack Query.
3. Build the page in `src/pages/` from components in `src/components/`.
4. Register the route in `App.tsx`. Guard it with auth/role if needed.
5. Handle loading, empty, and error states.

**Files:** `src/api/*`, `src/pages/*`, `src/components/*`, `src/App.tsx`

**Done when**
- [ ] `npm run build` passes (no type errors)
- [ ] Loading, empty, and error states render
- [ ] Usable at mobile width (~400px)

---

## 4. `search-index-sync`
**Trigger:** a change to searchable restaurant data or search behavior.

**Steps**
1. Update `RestaurantDocument` (the field mapping: text/keyword/geo_point/numeric).
2. Make sure every service that changes the data publishes a `RestaurantChangedEvent`.
3. `IndexSyncListener` re-indexes after commit (`AFTER_COMMIT`).
4. Update the query building in the search service (filters, sorting, geo distance).
5. If the mapping changed, run the full reindex (`POST /api/admin/search/reindex`).

**Files:** `search/*`, the services that publish events

**Done when**
- [ ] A create/update/review change appears in search results
- [ ] A full reindex produces the same results
- [ ] The document can be inspected in Kibana Discover

---

## 5. `photo-upload`
**Trigger:** adding or changing an image upload flow.

**Steps**
1. Accept `multipart/form-data` in the controller.
2. Validate the MIME type (jpeg/png/webp), extension, and maximum size.
3. Store the file through `StorageService` under a generated name, then save the URL in the photo table.
4. Check that the uploader may attach the photo to that restaurant/review.
5. On the frontend, show a preview and upload progress.

**Files:** `photo/*`, the related controller, frontend upload component

**Done when**
- [ ] Invalid type or size is rejected with a clear error
- [ ] No user-controlled file paths
- [ ] Deleting the parent review cleans up its photos

---

## 6. `moderation-action`
**Trigger:** a new report reason, admin action, or moderation rule.

**Steps**
1. Update the enums (`reason`, `status`) through a migration if they're stored as DB enums.
2. Implement the action in the moderation service. Record `resolved_by` and `resolved_at`.
3. Hiding or removing a review must recompute the restaurant's rating aggregates and trigger a search re-index.
4. Expose it under `/api/admin/**` (`ADMIN` only).
5. Update the admin reports page.

**Done when**
- [ ] Non-admins get 403
- [ ] Aggregates and the search index reflect hidden/removed reviews
- [ ] Each action is traceable (who did it and when)

---

## 7. `write-tests`
**Trigger:** any feature or bug fix.

**Steps**
- **Backend:** JUnit 5 + Mockito for services; `@WebMvcTest` for controllers; Testcontainers (MySQL, Elasticsearch) for integration tests.
- **Frontend:** Vitest + React Testing Library for components and hooks. Mock the API layer.
- For a bug fix, first write a test that reproduces the bug.

**Done when**
- [ ] `./mvnw verify` and `npm test` pass
- [ ] The new behavior and its edge cases (validation, authorization) are covered

---

## 8. `code-review-checklist`
**Trigger:** before handing off any change.

- [ ] Follows the layering and package rules in CLAUDE.md
- [ ] No entities leaked through the API; DTOs validated
- [ ] Authorization checked on every mutating path
- [ ] Schema changes go through a new migration, and the ER diagram is updated
- [ ] ES sync handled for searchable data changes
- [ ] No secrets, no logged personal data
- [ ] Tests added and passing; build green
- [ ] Docs updated where behavior changed
