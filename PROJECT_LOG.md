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

1. Add or improve global exception handling
2. Separate application configuration by profile
3. Improve order cancellation API
4. Add member signup
5. Add session-based login
6. Add authorization with roles
7. Prepare APIs for future React/Vue frontend separation
8. Consider JWT after the basic authentication and authorization flow is stable

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
- Clarified the authentication learning path: session-based login first, JWT later.
- Clarified that future `PROJECT_LOG.md` updates should be delivered as a complete updated file.

Notes:
- This file will be used to preserve project continuity across deleted or newly created chat sessions.
- `PROJECT_CONTEXT.md` remains the stable project reference document.
- `PROJECT_LOG.md` tracks current decisions, progress, and next tasks.

---

## 6. In Progress

Record the feature or refactoring currently being worked on.

### Current Task

```text
Task: Authentication preparation and project stabilization
Status: Planning
Related files:
- PROJECT_CONTEXT.md
- PROJECT_LOG.md
- Member entity
- Auth-related classes to be added later
- SecurityConfig to be added later
Notes:
- Before implementing login, the project should stabilize exception handling, configuration profiles, and API conventions.
- Signup should be implemented before login.
- Session-based login is the first authentication target.
- JWT should be considered after Spring Security basics are understood.
```

---

## 7. Next Tasks

Keep this list short and ordered.

```text
1. Add or improve Global Exception Handler.
2. Separate application configuration into profiles such as local, test, and prod.
3. Improve order cancellation API from deletion-style behavior to a domain command.
4. Add password and role to `Member`.
5. Add `PasswordEncoder`.
6. Implement signup API.
7. Implement `CustomUserDetails`.
8. Implement `CustomUserDetailsService`.
9. Implement session-based login.
10. Add `GET /api/me`.
11. Use `@AuthenticationPrincipal` for current-user APIs.
12. Add role-based authorization for USER and ADMIN.
13. Prepare APIs for future React/Vue frontend separation.
14. Consider JWT after the basic authentication and authorization flow is stable.
```

---

## 8. Important Design Decisions

Record decisions that should not be forgotten.

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
- Exact refactoring status is pending code review.
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
- Implementation pending.
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
```

### Important

Issues that affect maintainability, scalability, or backend conventions.

```text
- Add global exception handling before authentication becomes complex.
- Use DTOs for API requests and responses.
- Keep controllers thin and services focused on business logic.
- Use `@AuthenticationPrincipal` at the controller boundary for current-user APIs.
- Separate auth/security concerns from member/order/product domain logic.
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
- Member signup success.
- Duplicate email failure.
- Password encoding verification at a behavior level.
- Product stock decrease success.
- Product stock shortage failure.
- Order creation success.
- Order cancellation success.
- Duplicate cancellation failure.
- Current-user order lookup after authentication is added.
- Admin-only product management access control after authorization is added.
```

---

## 12. Blockers / Questions

Record anything that is currently unclear or blocking progress.

```text
- Exact current source code has not been reviewed in this session.
- Need to confirm current package names and entity mappings before detailed refactoring.
- Need to decide whether global exception handling should be implemented before or together with signup.
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

Current priority:
1. Add or improve global exception handling
2. Separate application profiles
3. Improve order cancellation API
4. Add signup
5. Add session-based login
6. Add authorization
7. Prepare for future React/Vue frontend separation
8. Consider JWT later after Spring Security basics are understood

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

---

## 2026-06-23 - Authentication Roadmap Decision Update

### Completed
- Updated `PROJECT_CONTEXT.md` to reflect the final authentication learning and implementation direction.
- Clarified that the next authentication path should be `Session -> JWT`, not direct JWT-first implementation.
- Reframed the authentication roadmap around cookie-based server sessions before token-based authentication.

### Decisions
- Learn and implement authentication in this order:
  1. Basic `HttpSession` login flow.
  2. Session timeout, `JSESSIONID` cookie behavior, and logout.
  3. Multi-server session limitations.
  4. Compare Redis Session and JWT.
  5. Implement JWT only after session fundamentals are understood.
- Do not jump directly into JWT before understanding how server-side session login works.
- Spring Security may be introduced during the session stage, but explanations should still connect Security internals to `HttpSession`, `JSESSIONID`, `SecurityContext`, and `Authentication`.

### Reasoning
- Session login helps clarify the basic question: where is login state stored?
- Cookie-based sessions show how the browser and server cooperate through `JSESSIONID`.
- Multi-server session limitations explain why in-memory Tomcat sessions are insufficient for scaled deployments.
- Redis Session and JWT should be compared only after the tradeoff between server-side state and stateless token verification is clear.

### Next
1. Run local build/test commands:
   - `./mvnw clean test`
   - `./mvnw clean package`
2. Improve `GlobalExceptionHandler` fallback logging to include the exception object:
   - `log.error("Unexpected exception occurred", e);`
3. Start authentication with session-based login:
   - Add `password` to `Member`.
   - Add signup request/response flow.
   - Store encoded password.
   - Implement login flow using `HttpSession` and `JSESSIONID`.
   - Implement `/members/me`.
   - Implement logout with `session.invalidate()`.
4. After session basics, study multi-server session limitations and compare Redis Session vs JWT.

### Notes for Future Chat
- The current authentication roadmap is now: `Session first -> JWT later`.
- The user specifically chose this sequence after discussing cookie timeout, Tomcat multi-server session limitations, Redis Session, RDBMS session storage, and JWT.
- Future answers should not recommend direct JWT-first implementation unless the user explicitly changes direction.


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
