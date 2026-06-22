# Project Log: jpa-prac

This document tracks the current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

---

## 1. Project Snapshot

- Project name: `jpa-prac`
- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term goal: Build a portfolio-level, production-minded Spring web application
- Main domain:
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
    │   └── OrderStatus.java   // should move to orders/entity
    ├── entity
    │   ├── Orders.java
    │   └── OrderItem.java
    ├── repository
    │   ├── OrdersRepository.java
    │   └── OrderItemRepository.java
    └── service
        └── OrderService.java
```

Assessment:

- The main package refactor from layer-based packages to feature-based packages has been mostly completed.
- `member`, `product`, and `orders` are now separated by domain.
- The main Spring Boot application class remains in the correct parent package, `kr.co.prac`, so component/entity/repository scanning should still work for subpackages.
- The current package name is `orders`, not `order`. This is acceptable if it stays consistent.
- `global` has not yet been confirmed as implemented.
- `auth` should be added later when signup/login work begins.

---

## 4. Current Priority

Current recommended priority:

### Phase 0: Project Stabilization Before Authentication

The package-structure refactor has been pushed, but stabilization is not fully complete yet.

1. Move `OrderStatus` from `orders.dto` to `orders.entity`.
2. Add or improve global exception handling.
3. Define a consistent error response format.
4. Separate application configuration by profile, such as local, test, and prod.
5. Review DTO usage and avoid exposing entities directly through APIs.
6. Improve order cancellation API from deletion-style behavior to a domain command.
7. Review transaction boundaries in service methods.
8. Review current JPA mappings for Member, Product, Orders, and OrderItem.
9. Review current tests and decide which tests should be added before authentication.

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
- Current implemented domain packages are `member`, `product`, and `orders`.
- Do not mix `order` and `orders` packages.
- Add `global` next for exception/config/response concerns.
- Add `auth` later when signup/login implementation starts.

Reason:
- Feature-based packages are easier to maintain as the project grows.
- The current structure already separates member, product, and orders concerns.
- Additional renaming from `orders` to `order` is optional and not urgent.

Status:
- Mostly completed.
- One cleanup remains: move `OrderStatus` from `orders.dto` to `orders.entity`.
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

Notes:
- This file will be used to preserve project continuity across deleted or newly created chat sessions.
- `PROJECT_CONTEXT.md` remains the stable project reference document.
- `PROJECT_LOG.md` tracks current decisions, progress, and next tasks.
- Current immediate focus is not login implementation yet.
- Current immediate focus is the remaining stabilization work after package refactoring.

---

## 7. In Progress

### Current Task

```text
Task: Project stabilization before authentication
Status: Package structure refactor pushed; remaining stabilization in progress
Related files:
- PROJECT_CONTEXT.md
- PROJECT_LOG.md
- src/main/java/kr/co/prac/member/**
- src/main/java/kr/co/prac/product/**
- src/main/java/kr/co/prac/orders/**
- future global/exception classes
- application configuration files

Notes:
- Login/authentication is not the immediate implementation step yet.
- The main package refactor has been pushed.
- Before implementing login, the project should still stabilize exception handling, configuration profiles, DTO boundaries, API conventions, and order cancellation behavior.
- Signup should be implemented before login after stabilization is complete.
- Session-based login is the first authentication target.
- JWT should be considered after Spring Security basics are understood.
```

---

## 8. Next Tasks

Keep this list short and ordered.

```text
1. Move OrderStatus from orders.dto to orders.entity.
2. Add global.exception.GlobalExceptionHandler.
3. Add global.exception.ErrorResponse.
4. Decide whether to add global.response.ApiResponse now or later.
5. Separate application configuration into local/test/prod profiles.
6. Improve order cancellation API from DELETE /orders/{orderId} to POST /orders/{orderId}/cancel.
7. Review service transaction boundaries.
8. Review JPA mappings for Member, Product, Orders, and OrderItem.
9. Review current tests and add missing tests for stock/order behavior.
10. After stabilization, add password and role to Member.
11. Add PasswordEncoder.
12. Implement signup API.
13. Implement CustomUserDetails and CustomUserDetailsService.
14. Implement session-based login.
15. Add GET /api/me.
16. Add role-based authorization for USER and ADMIN.
17. Consider JWT after the basic authentication and authorization flow is stable.
```

---

## 9. Problems / Cleanups Found During Structure Check

### Important

```text
- OrderStatus is currently in orders.dto, but it represents order domain state. Move it to orders.entity.
- OrdersController still uses DELETE /orders/{orderId} for cancellation-like behavior. This should become POST /orders/{orderId}/cancel later.
- ProductController imports org.springframework.stereotype.Controller even though it uses @RestController. Remove the unused import during cleanup.
- A global exception package has not yet been confirmed. Add global.exception next.
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
- Focus first on package structure, exception handling, configuration profiles, DTO/API conventions, order cancellation semantics, transaction boundaries, and JPA mappings.
- Authentication should be built after the backend foundation becomes cleaner and more consistent.

Reason:
- Authentication touches many parts of the project: Member, API design, security configuration, exception handling, current-user APIs, and authorization.
- If login is implemented before the basic structure is stable, later refactoring may become more difficult.
- A cleaner foundation makes session login and future JWT migration easier.

Status:
- Decided.
- Package structure is mostly completed.
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

Reason:
- Profile separation is necessary for local development, tests, and future deployment.
- Hardcoded credentials and destructive schema settings are not production-safe.

Status:
- Recommended.
- Implementation should happen during project stabilization before authentication.
```

---

## 11. Current Domain Notes

### Member

```text
Current state:
- Member is in member.entity.
- MemberController, MemberService, and MemberRepository are in the member package.
- Known fields include number, name, and email.

Future direction:
- Add password.
- Add role.
- Make email unique.
- Use Member as the authentication subject.
- Add signup before login.
```

### Product

```text
Current state:
- Product is in product.entity.
- ProductController, ProductService, and ProductRepository are in the product package.
- Known fields include number, name, price, and stock.
- Product has stock increase/decrease domain methods.

Future direction:
- Add admin-only product create/update/delete APIs.
- Add product detail API.
- Add product search/filtering and pagination.
```

### Orders

```text
Current state:
- Orders is in orders.entity.
- OrdersController, OrderService, and OrdersRepository are in the orders package.
- Current behavior includes order creation, order detail retrieval, order list retrieval, member-specific order lookup, and deletion-style cancellation.

Future direction:
- Replace deletion-style cancellation with POST /orders/{orderId}/cancel.
- Move OrderStatus to orders.entity.
- Add order total price.
- Return order items in order detail response.
- Prevent duplicate cancellation.
- After login, use current-user APIs such as GET /api/me/orders.
```

### OrderItem

```text
Current state:
- OrderItem is in orders.entity.
- OrderItemRepository is in orders.repository.
- OrderItem connects Orders and Product.
- It preserves order price and count at the time of ordering.

Future direction:
- Use DTOs for order item responses.
- Be careful with lazy loading and N+1 queries.
- Add order detail response that includes order items.
```

---

## 12. Testing Notes

### Existing Tests

```text
- Some member-related tests exist according to project context.
- Exact current test package structure still needs review after the package refactor.
```

### Recommended Tests

```text
Before authentication:
- Product stock decrease success.
- Product stock shortage failure.
- Order creation success.
- Order cancellation success.
- Duplicate cancellation failure.
- Global exception response format.
- Basic controller validation errors.

After authentication:
- Member signup success.
- Duplicate email failure.
- Password encoding verification at a behavior level.
- Current-user order lookup after authentication is added.
- Admin-only product management access control after authorization is added.
```

---

## 13. Blockers / Questions

```text
- Need to confirm whether all test imports were updated after the package refactor.
- Need to decide whether to keep package name orders permanently or later rename it to order.
- Need to add global exception handling before authentication.
- Need to separate application profiles before production-minded deployment work.
```

---

## 14. New Chat Handoff Summary

When starting a new chat session, paste or upload `PROJECT_CONTEXT.md` and this file.

Use this summary:

```text
This project is `jpa-prac`, a Java 21 + Spring Boot + MySQL practice project.
The goal is to grow it into a portfolio-level Spring web application.
The main domains are Member, Product, Orders, and OrderItem.

Use PROJECT_CONTEXT.md as the stable project reference.
Use PROJECT_LOG.md as the current progress and handoff record.

The assistant should act as a Spring/JPA teacher and code reviewer.
Explain concepts in Korean, provide Java/Spring examples when useful, and review code with production-readiness and portfolio-readiness in mind.

Current GitHub-verified structure:
- kr.co.prac.member
- kr.co.prac.product
- kr.co.prac.orders
- kr.co.prac.PracApplication

Current immediate priority:
1. Move OrderStatus from orders.dto to orders.entity.
2. Add or improve global exception handling.
3. Add consistent error response format.
4. Separate application profiles.
5. Improve order cancellation API.
6. Review transaction boundaries and JPA mappings.
7. Then implement signup.
8. Then implement session-based login.
9. Add authorization.
10. Prepare for future React/Vue frontend separation.
11. Consider JWT later after Spring Security basics are understood.

Important authentication decision:
- Implement session-based login first.
- Consider JWT later.
- Keep service logic independent from session/request/JWT details.
- Controllers may use @AuthenticationPrincipal, but services should receive values such as memberId.

Project log workflow:
- Keep one PROJECT_LOG.md file.
- When updates are needed, the assistant should generate a complete updated file for download.
```

---

## 15. Update Template

Use this template whenever the project state changes.

```md
## YYYY-MM-DD

### Completed
- 

### Decisions
- 

### Problems Found
- 

### Next
- 

### Notes for Future Chat
- 
```
