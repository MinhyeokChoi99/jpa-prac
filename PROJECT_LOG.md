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

---

## 2. Current Working Direction

The project should be developed step by step, with emphasis on:

- Java/Spring learning
- JPA relationship understanding
- Clean layered architecture
- API design for future frontend separation
- Production-minded refactoring
- Portfolio-readiness
- Incremental feature development

The assistant role should be closer to a Spring/JPA teacher and code reviewer, not only a code generator.

When important decisions or progress updates are made, the assistant should generate a complete updated `PROJECT_LOG.md` file rather than only providing small snippets for manual copy/paste.

---

## 3. Current Priority

Current recommended priority:

### Phase 0: Project Stabilization Before Authentication

The project should be cleaned and stabilized before implementing signup/login.

1. Review current project structure and package naming.
2. Move gradually toward feature-based packages such as `member`, `product`, `order`, `auth`, and `global`.
3. Add or improve global exception handling.
4. Define a consistent error response format.
5. Separate application configuration by profile, such as local, test, and prod.
6. Review DTO usage and avoid exposing entities directly through APIs.
7. Improve order cancellation API from deletion-style behavior to a domain command.
8. Review transaction boundaries in service methods.
9. Review current JPA mappings for Member, Product, Orders, and OrderItem.
10. Review current tests and decide which tests should be added before authentication.

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

## 4. Current Decisions

### 2026-06-22

- Use `PROJECT_CONTEXT.md` as the stable project reference document.
- Use `PROJECT_LOG.md` as the progress and handoff document.
- The project may continue across multiple chat sessions.
- Important decisions and progress should be recorded here to preserve continuity.
- When this file needs to be updated, the assistant should generate a complete updated Markdown file instead of asking the user to manually paste fragmented snippets.
- The assistant should explain Spring/JPA concepts in Korean with Java/Spring examples.
- The assistant should act as a Spring/JPA teacher and code reviewer, not only a code generator.
- Before implementing login, the project should first go through project stabilization and structural cleanup.
- Login should not be built on top of fragile package structure, inconsistent exception handling, or unclear API conventions.
- Login knowledge will be learned from the basics before choosing a final implementation style.
- Authentication will be implemented with session-based login first.
- JWT will be considered later after the basic Spring Security authentication and authorization flow is understood.
- The authentication structure should be designed so that the service layer does not depend on session, request, or JWT details.
- Controllers may use `@AuthenticationPrincipal`, but services should receive domain-level values such as `memberId`.
- Future React/Vue frontend separation should be considered when designing authentication APIs.
- React/Vue frontend separation is a future direction, but backend API design should already consider it.

---

## 5. Completed Work

Record completed work here.

### Example Format

```text
Date: YYYY-MM-DD
Task:
- What was completed

Notes:
- Important implementation details
- Important design decisions
- Any issues found
```

### Entries

#### 2026-06-22

Task:
- Created initial `PROJECT_LOG.md` skeleton.
- Decided how to preserve project continuity across deleted or newly created chat sessions.
- Clarified that future `PROJECT_LOG.md` updates should be delivered as a complete updated file.
- Clarified the authentication learning path: session-based login first, JWT later.
- Clarified that project stabilization should happen before authentication implementation.

Notes:
- This file will be used to preserve project continuity across deleted or newly created chat sessions.
- `PROJECT_CONTEXT.md` remains the stable project reference document.
- `PROJECT_LOG.md` tracks current decisions, progress, and next tasks.
- Current immediate focus is not login implementation yet.
- Current immediate focus is project structure cleanup and stabilization.

---

## 6. In Progress

Record the feature or refactoring currently being worked on.

### Current Task

```text
Task: Project stabilization before authentication
Status: Planning
Related files:
- PROJECT_CONTEXT.md
- PROJECT_LOG.md
- package structure
- application configuration files
- controller classes
- service classes
- repository classes
- entity classes
- DTO classes
- exception handling classes to be added or improved

Notes:
- Login/authentication is not the immediate implementation step yet.
- Before implementing login, the project should stabilize package structure, exception handling, configuration profiles, DTO boundaries, API conventions, and core order behavior.
- Signup should be implemented before login after stabilization is complete.
- Session-based login is the first authentication target.
- JWT should be considered after Spring Security basics are understood.
```

---

## 7. Next Tasks

Keep this list short and ordered.

```text
1. Review current package structure and package naming.
2. Decide whether to refactor toward feature-based packages.
3. Add or improve Global Exception Handler.
4. Add a consistent ErrorResponse DTO.
5. Separate application configuration into profiles such as local, test, and prod.
6. Review DTO usage and remove direct entity exposure from API responses if present.
7. Improve order cancellation API from deletion-style behavior to a domain command.
8. Review service transaction boundaries.
9. Review JPA mappings for Member, Product, Orders, and OrderItem.
10. Review current tests and identify missing tests for core business behavior.
11. After stabilization, add password and role to Member.
12. Add PasswordEncoder.
13. Implement signup API.
14. Implement CustomUserDetails and CustomUserDetailsService.
15. Implement session-based login.
16. Add GET /api/me.
17. Add role-based authorization for USER and ADMIN.
18. Consider JWT after the basic authentication and authorization flow is stable.
```

---

## 8. Important Design Decisions

Record decisions that should not be forgotten.

### Project Stabilization Before Authentication

```text
Decision:
- Stabilize the project before implementing signup/login.
- Focus first on package structure, exception handling, configuration profiles, DTO/API conventions, order cancellation semantics, transaction boundaries, and JPA mappings.
- Authentication should be built after the backend foundation becomes cleaner and more consistent.

Reason:
- Authentication tends to touch many parts of the project: Member, API design, security configuration, exception handling, current-user APIs, and authorization.
- If login is implemented before the basic structure is stable, later refactoring may become more difficult.
- A cleaner foundation makes session login and future JWT migration easier.

Status:
- Decided.
- This is the current immediate project direction.
```

### Authentication

```text
Decision:
- Start with session-based login before JWT.
- Keep authentication details isolated from the service layer.
- Use `@AuthenticationPrincipal` in controllers to access the current authenticated user.
- Pass domain-level values such as `memberId` into services.
- Consider JWT later when React/Vue frontend separation becomes more concrete.

Reason:
- Session-based login is easier for learning the basic Spring Security flow.
- JWT adds extra concerns such as token issuance, token validation, token storage, refresh tokens, CORS, and logout strategy.
- If authentication concerns are isolated in the security layer, switching from session to JWT later should not require rewriting the core domain or service logic.

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
- Prefer current-user endpoints after login, such as `GET /api/me` and `GET /api/me/orders`.
- Avoid relying on client-provided `memberId` for user-specific operations after authentication is introduced.
- Prefer domain-command endpoints for business actions such as order cancellation.

Reason:
- A separated frontend needs stable, predictable, JSON-based APIs.
- User-specific APIs should derive the current user from authentication, not from user-controlled request values.
- Cancelling an order is not the same as deleting order history.

Status:
- Direction decided.
- Exact API implementation is pending.
```

### Package Structure

```text
Decision:
- Gradually move toward feature-based packages such as `member`, `product`, `order`, `auth`, and `global`.

Reason:
- Feature-based packages are easier to maintain as the project grows.
- Authentication and global exception handling should not be scattered across unrelated packages.

Status:
- Recommended.
- This should be reviewed before authentication implementation.
```

### Database / Configuration

```text
Decision:
- Separate application configuration by profile.
- Do not rely on local database settings as production settings.
- Avoid hardcoded database credentials.
- Reduce long-term reliance on `ddl-auto=create`.

Reason:
- Profile separation is necessary for local development, tests, and future deployment.
- Hardcoded credentials and destructive schema settings are not production-safe.

Status:
- Recommended.
- Implementation should happen during project stabilization before authentication.
```

### Project Log Management

```text
Decision:
- Keep a single `PROJECT_LOG.md` file in the project root.
- Update the same file over time rather than creating many separate log files.
- When updates are needed, the assistant should generate a complete updated `PROJECT_LOG.md` file for download.

Reason:
- This reduces manual copy/paste work.
- A single current log file is easier to upload in new chat sessions.
- Git history can track how the log evolves over time.

Status:
- Decided.
```

---

## 9. Code Review Notes

Use this section when reviewing code.

### Critical

Issues that may cause runtime errors, wrong data, security problems, or production failure.

```text
- Do not store raw passwords when authentication is added.
- Do not let service methods depend directly on `HttpSession`, `HttpServletRequest`, or JWT token strings.
- Do not trust client-provided `memberId` for user-specific operations after login is introduced.
- Do not expose sensitive user data in API responses.
- Do not rely on destructive schema settings such as `ddl-auto=create` for long-term development or deployment.
```

### Important

Issues that affect maintainability, scalability, or backend conventions.

```text
- Stabilize package structure before authentication implementation.
- Add global exception handling before authentication becomes complex.
- Use DTOs for API requests and responses.
- Keep controllers thin and services focused on business logic.
- Use `@AuthenticationPrincipal` at the controller boundary for current-user APIs.
- Separate auth/security concerns from member/order/product domain logic.
- Review order cancellation semantics before adding more order-related features.
```

### Optional

Useful improvements that are not urgent.

```text
- Add more detailed Swagger documentation later.
- Add a consistent API response wrapper later if needed.
- Add Docker and CI/CD after core backend features are stable.
- Add JWT after session-based login and authorization are understood.
```

---

## 10. Current Domain Notes

### Member

```text
Current state:
- Member currently represents a user/customer-like entity.
- Known or expected fields include number, name, and email.

Future direction:
- Add password.
- Add role.
- Make email unique.
- Use Member as the authentication subject.
- Add signup before login.

Open questions:
- Current exact Member entity code needs review before implementation.
```

### Product

```text
Current state:
- Product represents an item that can be ordered.
- Known or expected fields include number, name, price, and stock.

Future direction:
- Add admin-only product create/update/delete APIs.
- Add product detail API.
- Add product search/filtering and pagination.

Open questions:
- Current product API and stock handling implementation need review before deeper refactoring.
```

### Orders

```text
Current state:
- Orders represents a purchase/order transaction.
- Current or expected behavior includes order creation, order detail retrieval, order list retrieval, member-specific order lookup, and cancellation.

Future direction:
- Replace deletion-style cancellation with a clearer domain command such as `POST /orders/{orderId}/cancel`.
- Add order total price.
- Return order items in order detail response.
- Prevent duplicate cancellation.
- After login, use current-user APIs such as `GET /api/me/orders`.

Open questions:
- Current order entity, order status enum, and cancellation logic need review.
```

### OrderItem

```text
Current state:
- OrderItem connects Orders and Product.
- It should preserve order price and count at the time of ordering.

Future direction:
- Use DTOs for order item responses.
- Be careful with lazy loading and N+1 queries.
- Add order detail response that includes order items.

Open questions:
- Current JPA relationship mapping needs review.
```

---

## 11. Testing Notes

Record test coverage and future test targets.

### Existing Tests

```text
- Some member-related tests appear to exist according to project context.
- Exact test files need review.
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

## 12. Blockers / Questions

Record anything that is currently unclear or blocking progress.

```text
- Exact current source code has not been reviewed in this session.
- Need to confirm current package names and entity mappings before detailed refactoring.
- Need to decide the exact first stabilization task: package structure, exception handling, or configuration profiles.
```

---

## 13. New Chat Handoff Summary

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

Current immediate priority:
1. Stabilize the project before implementing authentication.
2. Review package structure and package naming.
3. Add or improve global exception handling.
4. Add consistent error response format.
5. Separate application profiles.
6. Review DTO/API boundaries.
7. Improve order cancellation API.
8. Review transaction boundaries and JPA mappings.
9. Then implement signup.
10. Then implement session-based login.
11. Add authorization.
12. Prepare for future React/Vue frontend separation.
13. Consider JWT later after Spring Security basics are understood.

Important authentication decision:
- Implement session-based login first.
- Consider JWT later.
- Keep service logic independent from session/request/JWT details.
- Controllers may use `@AuthenticationPrincipal`, but services should receive values such as `memberId`.

Project log workflow:
- Keep one `PROJECT_LOG.md` file.
- When updates are needed, the assistant should generate a complete updated file for download.
```

---

## 14. Update Template

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
