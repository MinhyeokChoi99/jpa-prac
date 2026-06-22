# Project Log: jpa-prac

This document tracks the current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

---

## 1. Project Snapshot

- Project name: `jpa-prac`
- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term goal: Build a portfolio-level, production-minded Spring web application
- User language: Korean
- Assistant response style: Korean explanation + Java/Spring examples

Main domain:

- Member
- Product
- Orders
- OrderItem

Current confirmed tech stack:

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Data JDBC
- Bean Validation
- MySQL
- H2 for tests
- Lombok
- Springdoc OpenAPI / Swagger
- Maven

---

## 2. Current Working Direction

The project should be developed step by step, with emphasis on:

- Java/Spring learning
- JPA relationship understanding
- Clean layered architecture
- Feature-based package organization
- API design for future frontend separation
- Production-minded refactoring
- Portfolio-readiness
- Incremental feature development

The assistant role should be closer to a Spring/JPA teacher and code reviewer, not only a code generator.

When important decisions or progress updates are made, the assistant should generate a complete updated `PROJECT_LOG.md` file rather than only providing small snippets for manual copy/paste.

---

## 3. Current GitHub-Verified Package Structure

Verified from the latest pushed GitHub repository on 2026-06-22.

Current main application class:

```text
src/main/java/kr/co/prac/PracApplication.java
```

Current feature-based package structure:

```text
src/main/java/kr/co/prac
├── PracApplication.java
├── global
│   ├── config
│   │   └── JpaAuditingConfiguration.java
│   └── entity
│       └── BaseTimeEntity.java
├── member
│   ├── controller
│   │   └── MemberController.java
│   ├── dto
│   ├── entity
│   │   └── Member.java
│   ├── repository
│   │   └── MemberRepository.java
│   └── service
│       ├── MemberService.java
│       └── MemberServiceImpl.java
├── product
│   ├── controller
│   │   └── ProductController.java
│   ├── dto
│   ├── entity
│   │   └── Product.java
│   ├── repository
│   │   └── ProductRepository.java
│   └── service
│       └── ProductService.java
└── orders
    ├── controller
    │   └── OrdersController.java
    ├── dto
    ├── entity
    │   ├── Orders.java
    │   ├── OrderItem.java
    │   └── OrderStatus.java
    ├── repository
    │   ├── OrdersRepository.java
    │   └── OrderItemRepository.java
    └── service
        └── OrderService.java
```

Assessment:

- The main package refactor from layer-based packages to feature-based packages has been completed enough to continue Phase 0 stabilization.
- `member`, `product`, and `orders` are separated by domain.
- The current package name is `orders`, not `order`. This is acceptable if it stays consistent.
- `OrderStatus` has been moved to `orders.entity`; this is now correct because order status is domain state, not a DTO.
- `global.config` and `global.entity` now exist for shared infrastructure/configuration.
- `auth` should be added later when signup/login work begins.

---

## 4. Current Priority

Current recommended priority:

### Phase 0: Project Stabilization Before Authentication

The package-structure refactor and basic auditing timestamp support have been pushed, but stabilization is not fully complete yet.

1. Add or improve global exception handling.
2. Define a consistent error response format.
3. Separate application configuration by profile, such as local, test, and prod.
4. Review DTO usage and avoid exposing entities directly through APIs.
5. Improve order cancellation API from deletion-style behavior to a domain command.
6. Review transaction boundaries in service methods.
7. Review current JPA mappings for Member, Product, Orders, and OrderItem.
8. Review current tests and decide which tests should be added before authentication.

### Phase 1: Member and Authentication

Authentication should begin only after the basic project structure is stable.

1. Add password and role to `Member`.
2. Add `PasswordEncoder`.
3. Implement signup API.
4. Implement `CustomUserDetails`.
5. Implement `CustomUserDetailsService`.
6. Implement session-based login.
7. Add `GET /api/me`.
8. Use `@AuthenticationPrincipal` for current-user APIs.
9. Add role-based authorization for USER and ADMIN.
10. Prepare APIs for future React/Vue frontend separation.
11. Consider JWT after the basic authentication and authorization flow is stable.

---

## 5. Current Decisions

### 2026-06-22

- Use `PROJECT_CONTEXT.md` as the stable project reference document.
- Use `PROJECT_LOG.md` as the progress and handoff document.
- The project may continue across multiple chat sessions.
- Important decisions and progress should be recorded here to preserve continuity.
- When this file needs to be updated, the assistant should generate a complete updated Markdown file instead of asking the user to manually paste fragmented snippets.
- Before project-related recommendations, the assistant should check the latest GitHub project Markdown files and current repository structure when available.
- The assistant should explain Spring/JPA concepts in Korean with Java/Spring examples.
- The assistant should act as a Spring/JPA teacher and code reviewer, not only a code generator.
- Before implementing login, the project should first go through project stabilization and structural cleanup.
- Authentication will be implemented with session-based login first.
- JWT will be considered later after the basic Spring Security authentication and authorization flow is understood.
- The authentication structure should be designed so that the service layer does not depend on session, request, or JWT details.
- Controllers may use `@AuthenticationPrincipal`, but services should receive domain-level values such as `memberId`.
- Future React/Vue frontend separation should be considered when designing authentication APIs.

### Package Structure Decision

```text
Decision:
- Keep the current pushed feature-based structure as the base.
- Current implemented domain packages are member, product, and orders.
- Do not mix order and orders packages.
- Add global packages for exception/config/response/common entity concerns.
- Add auth later when signup/login implementation starts.

Reason:
- Feature-based packages are easier to maintain as the project grows.
- The current structure already separates member, product, and orders concerns.
- Additional renaming from orders to order is optional and not urgent.

Status:
- Completed enough for now.
- OrderStatus has been moved to orders.entity.
```

### Auditing Decision

```text
Decision:
- Add entity timestamp auditing during Phase 0 stabilization.
- Add createdAt and updatedAt through BaseTimeEntity.
- Use global.config.JpaAuditingConfiguration to enable JPA auditing.
- Apply BaseTimeEntity to Member, Product, Orders, and OrderItem.
- Do not add createdBy or updatedBy yet.

Reason:
- createdAt and updatedAt are common operational fields for main entities.
- Auditing timestamps are infrastructure/stabilization work, not a large business feature.
- createdBy and updatedBy require a meaningful authenticated principal, so they should wait until Spring Security/session login is introduced.

Status:
- Implemented and pushed.
- Minor cleanup recommended: make BaseTimeEntity abstract, add getters, and explicitly name created_at/updated_at columns if desired.
```

---

## 6. Completed Work

### Entries

#### 2026-06-22

Task:
- Created initial `PROJECT_LOG.md` skeleton.
- Decided how to preserve project continuity across deleted or newly created chat sessions.
- Clarified that future `PROJECT_LOG.md` updates should be delivered as a complete updated file.
- Clarified the authentication learning path: session-based login first, JWT later.
- Clarified that project stabilization should happen before authentication implementation.
- Refactored and pushed the main package structure toward feature-based packages.
- Confirmed from GitHub that `member`, `product`, and `orders` packages now exist under `kr.co.prac`.
- Moved `OrderStatus` into `orders.entity`.
- Added auditing timestamp support:
  - `global.config.JpaAuditingConfiguration`
  - `global.entity.BaseTimeEntity`
  - `createdAt`
  - `updatedAt`
  - entity inheritance for `Member`, `Product`, `Orders`, and `OrderItem`
- Updated `data.sql` seed data to include `created_at` and `updated_at` values.

Notes:
- This file will be used to preserve project continuity across deleted or newly created chat sessions.
- `PROJECT_CONTEXT.md` remains the stable project reference document.
- `PROJECT_LOG.md` tracks current decisions, progress, and next tasks.
- Current immediate focus is not login implementation yet.
- Current immediate focus is the remaining stabilization work after package refactoring and auditing.

---

## 7. In Progress

### Current Task

```text
Task: Project stabilization before authentication
Status: Package structure refactor and auditing timestamps pushed; remaining stabilization in progress
Related files:
- PROJECT_CONTEXT.md
- PROJECT_LOG.md
- src/main/java/kr/co/prac/global/config/JpaAuditingConfiguration.java
- src/main/java/kr/co/prac/global/entity/BaseTimeEntity.java
- src/main/java/kr/co/prac/member/**
- src/main/java/kr/co/prac/product/**
- src/main/java/kr/co/prac/orders/**
- future global/exception classes
- application configuration files

Notes:
- Login/authentication is not the immediate implementation step yet.
- The main package refactor has been pushed.
- Auditing timestamps have been added.
- Before implementing login, the project should still stabilize exception handling, configuration profiles, DTO boundaries, API conventions, and order cancellation behavior.
- Signup should be implemented before login after stabilization is complete.
- Session-based login is the first authentication target.
- JWT should be considered after Spring Security basics are understood.
```

---

## 8. Next Tasks

Keep this list short and ordered.

```text
1. Add global.exception.GlobalExceptionHandler.
2. Add global.exception.ErrorCode.
3. Add global.exception.ErrorResponse.
4. Add global.exception.BusinessException.
5. Replace meaningful IllegalArgumentException / IllegalStateException usages with domain-specific exceptions.
6. Decide whether to add global.response.ApiResponse now or later.
7. Separate application configuration into local/test/prod profiles.
8. Improve order cancellation API from DELETE /orders/{orderId} to POST /orders/{orderId}/cancel.
9. Review service transaction boundaries.
10. Review JPA mappings for Member, Product, Orders, and OrderItem.
11. Review current tests and add missing tests for stock/order/auditing behavior if useful.
12. After stabilization, add password and role to Member.
13. Add PasswordEncoder.
14. Implement signup API.
15. Implement CustomUserDetails and CustomUserDetailsService.
16. Implement session-based login.
17. Add GET /api/me.
18. Add role-based authorization for USER and ADMIN.
19. Consider JWT after the basic authentication and authorization flow is stable.
```

---

## 9. Problems / Cleanups Found During Structure Check

### Important

```text
- OrdersController still uses DELETE /orders/{orderId} for cancellation-like behavior. This should become POST /orders/{orderId}/cancel later.
- ProductController imports org.springframework.stereotype.Controller even though it uses @RestController. Remove the unused import during cleanup if still present.
- Global exception handling has not yet been implemented. Add global.exception next.
- Current service/entity code still uses broad exceptions such as IllegalArgumentException and IllegalStateException for meaningful business failures. Replace gradually with domain-specific exceptions.
```

### Auditing Cleanup Recommendations

```text
- BaseTimeEntity currently works structurally, but it is better as an abstract class because it should not be instantiated directly.
- BaseTimeEntity should expose getters for createdAt and updatedAt if DTOs/tests need to read auditing timestamps later.
- Explicit @Column(name = "created_at") and @Column(name = "updated_at") are recommended for clarity, especially because data.sql uses snake_case column names.
- createdBy and updatedBy should not be added until authentication/Spring Security is introduced.
```

### Optional

```text
- The package name is orders instead of order. This is acceptable if consistent. Renaming to order is optional and not urgent.
- Consider making service field names less implementation-specific later, for example memberService instead of memberServiceImpl.
```

---

## 10. Important Design Decisions

### Project Stabilization Before Authentication

```text
Decision:
- Stabilize the project before implementing signup/login.
- Focus first on package structure, auditing timestamps, exception handling, configuration profiles, DTO/API conventions, order cancellation semantics, transaction boundaries, and JPA mappings.
- Authentication should be built after the backend foundation becomes cleaner and more consistent.

Reason:
- Authentication touches many parts of the project: Member, API design, security configuration, exception handling, current-user APIs, and authorization.
- If login is implemented before the basic structure is stable, later refactoring may become more difficult.
- A cleaner foundation makes session login and future JWT migration easier.

Status:
- Decided.
- Package structure is mostly completed.
- Auditing timestamps are implemented.
- Remaining stabilization tasks are still pending.
```

### Authentication

```text
Decision:
- Start with session-based login before JWT.
- Keep authentication details isolated from the service layer.
- Use @AuthenticationPrincipal in controllers to access the current authenticated user.
- Pass domain-level values such as memberId into services.
- Consider JWT later when React/Vue frontend separation becomes more concrete.

Reason:
- Session-based login is easier for learning the basic Spring Security flow.
- JWT adds extra concerns such as token issuance, token validation, token storage, refresh tokens, CORS, and logout strategy.
- If authentication concerns are isolated in the security layer, switching from session to JWT later should not require rewriting core domain or service logic.

Status:
- Decided.
- Signup should be implemented before login.
- Session-based login is the current recommended first implementation.
- Authentication implementation should begin after project stabilization.
```

### API Design

```text
Decision:
- Design APIs with future React/Vue frontend separation in mind.
- Prefer current-user endpoints after login, such as GET /api/me and GET /api/me/orders.
- Avoid relying on client-provided memberId for user-specific operations after authentication is introduced.
- Prefer domain-command endpoints for business actions such as order cancellation.

Reason:
- A separated frontend needs stable, predictable, JSON-based APIs.
- User-specific APIs should derive the current user from authentication, not from user-controlled request values.
- Cancelling an order is not the same as deleting order history.

Status:
- Direction decided.
- Exact API implementation is pending.
```

### Database / Configuration

```text
Decision:
- Separate application configuration by profile.
- Do not rely on local database settings as production settings.
- Avoid hardcoded database credentials.
- Reduce long-term reliance on ddl-auto=create.
- Keep auditing timestamps as application-level entity metadata.

Reason:
- Profile separation is necessary for local development, tests, and future deployment.
- Hardcoded credentials and destructive schema settings are not production-safe.
- Auditing timestamps are useful for debugging, administration, and future production-readiness.

Status:
- Recommended.
- Auditing timestamps implemented.
- Profile separation still pending.
```

---

## 11. Current Domain Notes

### Member

Current role:

- User/customer-like entity.
- Eventually becomes authentication subject.

Current/expected fields:

- number
- name
- email
- createdAt
- updatedAt

Near-future direction:

- Add password only when signup/security implementation begins.
- Add role such as USER/ADMIN when authorization is introduced.
- Add unique email constraint and duplicate email handling.

### Product

Current role:

- Item that can be ordered.
- Holds price and stock.

Current/expected fields:

- number
- name
- price
- stock
- createdAt
- updatedAt

Important current behavior:

- Stock can increase/decrease.
- Stock shortage should eventually use a domain-specific exception instead of broad IllegalArgumentException.

Near-future direction:

- Admin-only product CRUD after authentication/authorization.
- Product detail API.
- Product pagination/search.

### Orders

Current role:

- Order transaction connected to Member.

Current/expected fields:

- number
- member
- orderDate
- status
- createdAt
- updatedAt

Near-future direction:

- Replace deletion-style cancellation endpoint with command endpoint.
- Prevent duplicate cancellation.
- Return order detail with order items.
- Calculate total price.

### OrderItem

Current role:

- Order line item connecting Orders and Product.

Current/expected fields:

- number
- orders
- product
- orderPrice
- count
- createdAt
- updatedAt

Near-future direction:

- Keep order price snapshot.
- Avoid exposing entity graph directly through API responses.
- Watch lazy loading/N+1 query problems.

---

## 12. Exception Handling Plan

Next stabilization target:

```text
global.exception
├── BusinessException.java
├── ErrorCode.java
├── ErrorResponse.java
└── GlobalExceptionHandler.java
```

Recommended first domain exceptions:

```text
member.exception
├── MemberNotFoundException.java
└── DuplicateEmailException.java

product.exception
├── ProductNotFoundException.java
└── OutOfStockException.java

orders.exception
├── OrderNotFoundException.java
├── AlreadyCanceledOrderException.java
└── EmptyOrderRequestException.java
```

Initial ErrorCode candidates:

```text
MEMBER_NOT_FOUND
DUPLICATE_EMAIL
PRODUCT_NOT_FOUND
OUT_OF_STOCK
ORDER_NOT_FOUND
EMPTY_ORDER_REQUEST
ALREADY_CANCELED_ORDER
INVALID_INPUT_VALUE
INTERNAL_SERVER_ERROR
```

Principle:

- Define only meaningful business failures explicitly.
- Let validation failures be handled by a validation exception handler.
- Let unexpected system failures fall back to INTERNAL_SERVER_ERROR.

---

## 13. Testing Notes

Current recommended next tests:

```text
- Member creation success
- Duplicate email failure after exception handling is added
- Product stock decrease success
- Product stock shortage failure
- Order creation success
- Order cancellation success
- Duplicate cancellation failure
- Auditing timestamp non-null check if useful
```

For auditing specifically:

```text
- Repository save should populate createdAt and updatedAt.
- data.sql seed rows must include created_at and updated_at because SQL initialization bypasses JPA auditing listeners.
```

---

## 14. Next Best Step

The next best implementation step is:

```text
Global exception handling
```

Reason:

- Package structure is now mostly stable.
- Auditing timestamps are implemented.
- The current service/entity code still uses broad exceptions for domain failures.
- Authentication should not be implemented before error handling conventions are clear.

Recommended next commit message:

```bash
git commit -m "feat: add global exception handling"
```
