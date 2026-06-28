# Project Log: jpa-prac

This document tracks the current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

_Last updated: 2026-06-29 (Asia/Seoul)_

---

## 1. Project Snapshot

- Project name: `jpa-prac`
- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term goal: Build a portfolio-level, production-minded Spring web application
- Main domains:
  - Member
  - Product
  - Orders
  - OrderItem
  - Spring Security session login
  - Admin
  - Static frontend
  - Planned Cart

---

## 2. Current Working Direction

The project should be developed step by step, with emphasis on:

- Java/Spring learning
- JPA relationship understanding
- Clean layered architecture
- API design for frontend usage
- Production-minded refactoring
- Portfolio-readiness
- Incremental feature development
- Authentication and authorization correctness
- Session management structure
- Test learning and test coverage
- Backend-centered business logic

The assistant role should be closer to a Spring/JPA teacher and code reviewer, not only a code generator.

Important frontend/backend boundary:

- The user is responsible for backend logic.
- The assistant may help generate frontend UI.
- Frontend code must not hide, patch, or compensate for backend defects.
- Backend remains the source of truth for authentication, authorization, validation, stock changes, order ownership, product status transitions, and persistence.
- Frontend role checks are display/routing only.

---

## 3. Current Priority

Current recommended priority:

1. Learn and add backend tests.
   - Start with JUnit 5 and AssertJ basics.
   - Write pure domain unit tests first.
   - Write service unit tests with Mockito.
   - Write repository tests with `@DataJpaTest`.
   - Write controller/security integration tests with MockMvc.
2. Add cart feature after initial test coverage is started.
   - Add `Cart` and `CartItem`.
   - Add cart item add/update/remove APIs.
   - Add order-from-cart API.
   - Reuse backend order validation.
3. Add product image support starting with `imageUrl`.
   - Add `imageUrl` to `Product`.
   - Add `imageUrl` to product create/update request DTOs.
   - Add `imageUrl` to product response DTO.
   - Display product images in the static frontend.
   - Consider file upload only later.
4. Keep frontend as an API-calling UI only.
5. Add README or documentation updates after the next backend phase is stable.

---

## 4. Current Decisions

### 2026-06-22

- Use `PROJECT_CONTEXT.md` as the stable project reference document.
- Use `PROJECT_LOG.md` as the progress and handoff document.
- The project may continue across multiple chat sessions.
- Important decisions and progress should be recorded here to preserve continuity.
- The assistant should explain Spring/JPA concepts in Korean with Java/Spring examples.
- Login knowledge will be learned from the basics before choosing a final implementation style.
- Session-based login is recommended before JWT.
- React/Vue frontend separation is a future direction, but backend API design should already consider frontend usage.

### 2026-06-24

- Keep the roadmap as `Session first -> JWT later`.
- Separate normal-user APIs from admin APIs.
- Use `/members/me` for current-user behavior.
- Move arbitrary member lookup/deletion to admin flow.
- Move all-order lookup to admin flow.
- Treat order cancellation as a domain command, not simple deletion.

### 2026-06-26

- `PROJECT_CONTEXT.md` and `PROJECT_LOG.md` should be kept as stable handoff documents.
- Exact filenames must be preserved:
  - `PROJECT_CONTEXT.md`
  - `PROJECT_LOG.md`
- Do not generate files with suffixes like `_updated`.
- Documentation format should remain consistent with the existing numbered section structure.

### 2026-06-29

- Spring Security session login is now treated as the current authentication direction.
- Admin APIs are protected as admin-only backend APIs.
- Admin product CRUD has been added.
- Product status management has been added:
  - `ACTIVE`
  - `HIDDEN`
  - `DELETED`
- Product delete means soft delete through `ProductStatus.DELETED`.
- Public product list should expose only `ACTIVE` products.
- Admin product list should expose all product statuses.
- Static frontend files were created under:
  - `src/main/resources/static/index.html`
  - `src/main/resources/static/js/app.js`
  - `src/main/resources/static/css/style.css`
- Login page originally included an intro/hero section.
- The login intro/hero section was removed from the code, not merely hidden by CSS.
- Current login page should show only:
  - login form
  - signup form
- Browser flow was mostly verified by the user.
- The next backend phase should focus on tests, not more frontend patching.

### 2026-06-29 Testing Decision

Testing is now the selected next learning and implementation phase.

Testing should include:

- domain unit tests
- service unit tests with Mockito
- repository tests with `@DataJpaTest`
- controller/security integration tests with MockMvc
- broader integration tests later if needed

The user has limited prior experience writing tests, so test work must include explanation of:

- what behavior is being tested
- what data is prepared
- what action is executed
- what result is asserted
- what type of test is being written

Recommended test implementation order:

1. Learn JUnit 5 and AssertJ syntax.
2. Write pure domain unit tests for `Product`.
3. Write order stock/cancel-related domain tests.
4. Write service unit tests with Mockito.
5. Write repository tests with `@DataJpaTest`.
6. Write controller/security integration tests with MockMvc.
7. Add broader API flow integration tests if needed.

Important testing distinction:

- Domain unit tests usually do not require Mockito.
- Service unit tests often use Mockito to mock repositories, encoders, and other dependencies.
- Repository tests should not mock repositories; use `@DataJpaTest`.
- Controller/security integration tests should verify real API/security behavior with MockMvc.

### 2026-06-29 Cart Decision

Cart is selected as the next commerce feature after the testing phase starts.

Expected future domain:

- `Cart`
- `CartItem`

Expected future APIs:

```text
GET    /cart
POST   /cart/items
PUT    /cart/items/{cartItemId}
DELETE /cart/items/{cartItemId}
POST   /cart/order
```

Important rule:

- Cart must not replace backend order validation.
- Product status and stock must be checked again when creating an order from cart.
- The authenticated user must own the cart.

### 2026-06-29 Product Image Decision

Product image support should start with a simple `imageUrl` field.

Stage 1:

- Add `imageUrl` to `Product`.
- Add `imageUrl` to product create/update request DTOs.
- Add `imageUrl` to product response DTO.
- Display product images in the static frontend.
- Use a default placeholder image when `imageUrl` is empty.

Decision:

- Do not start with file upload.
- Consider `MultipartFile`, local storage, or object storage only later.

