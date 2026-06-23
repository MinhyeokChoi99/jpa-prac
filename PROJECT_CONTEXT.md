# PROJECT_CONTEXT.md

# Project Context: jpa-prac

_Last updated: 2026-06-23 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `order_item`, and now basic `login`
- User language: Korean
- Preferred assistant response style: Korean explanation + Java/Spring code examples + direct code review

This project is being developed step by step to study real backend structure, JPA relationships, business rules, exception handling, and authentication.

---

## 2. Current Confirmed Tech Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Data JDBC dependency/module is present, but actual repositories are JPA repositories
- Bean Validation
- MySQL
- H2 for tests
- Lombok
- Springdoc OpenAPI / Swagger
- Maven
- Jakarta Servlet API through Spring Web/Tomcat

Potential future stack:

- Spring Security
- BCrypt password encoding
- Redis Session
- JWT
- Spring profiles
- Flyway or Liquibase
- QueryDSL
- Docker
- GitHub Actions
- Testcontainers
- Cloud deployment

---

## 3. Current Confirmed Repository State

Current packages/domains include:

- `member`
- `product`
- `orders`
- `login`
- `global.exception`
- `global.entity`
- `global.session`

Current implemented features include:

- Member CRUD
- Product list API
- Order list/detail/create/cancel flows
- Member-specific order lookup
- DTO-based request/response separation
- Basic validation for order creation
- Product stock decrease and restore logic
- Global exception handling with `BusinessException`, `ErrorCode`, and `ErrorResponse`
- JPA auditing with `BaseTimeEntity`
- Basic custom session-based login and logout

---

## 4. Current Session Login Implementation

Current authentication is a learning-stage custom `HttpSession` implementation, not yet Spring Security-based.

Current endpoints:

```text
POST /members/login
POST /members/logout
```

Current login flow:

```text
1. Client sends email/password to POST /members/login.
2. LoginService finds Member by email.
3. LoginService compares request password with DB password.
4. If valid, LoginResponse is created from Member.
5. Controller creates/retrieves HttpSession.
6. Controller stores member id in session using SessionConst.LOGIN_MEMBER_ID.
7. Browser receives JSESSIONID cookie from the server.
8. Later requests can be associated with the same session through JSESSIONID.
```

Current logout flow:

```text
1. Client sends POST /members/logout with JSESSIONID cookie.
2. LoginService calls request.getSession(false).
3. If an existing session exists, it is invalidated.
4. The login state for that browser session is removed.
```

Current session key:

```java
SessionConst.LOGIN_MEMBER_ID = "login_member_id";
```

Current design decisions:

- `SessionConst` is placed in `kr.co.prac.global.session`.
- `LoginResponse.memberId` exists for server-side session storage but is hidden from JSON response with `@JsonIgnore`.
- `ErrorCode.INVALID_PASSWORD` now uses `HttpStatus.UNAUTHORIZED`.
- `InvalidPasswordException` delegates to `ErrorCode.INVALID_PASSWORD`.
- Login currently uses plain password comparison as a learning-stage implementation.

Confirmed remaining cleanup:

- `LoginServiceImpl` still has an unused `MemberService` import. Remove it.
- `LoginRequest` still needs validation annotations such as `@NotBlank` and `@Email`.
- Password comparison should later be replaced with `PasswordEncoder.matches()`.
- `data.sql` role values are now quoted; password seed values should also preferably be single-quoted strings.

---

## 5. Current Domain Understanding

### Member

A member represents a user/customer-like entity and is now becoming the authentication subject.

Known fields now include or are expected to include:

- `number`
- `name`
- `email`
- `password`
- `role`
- `createdAt`
- `updatedAt`

Important future direction:

- Add a unique constraint for email.
- Replace raw password storage/comparison with BCrypt.
- Use session member id as the source of identity after login.
- Do not trust client-provided member id for user-specific operations after login.

### Product

Product stock changes through domain methods. Future work includes product detail, admin CRUD, search, pagination, and authorization.

### Orders

Orders currently support list/detail/create/cancel. Future work should connect order creation/listing to the logged-in session member and prefer `POST /orders/{orderId}/cancel` for cancellation semantics.

### OrderItem

OrderItem connects Orders and Product. Future work should return order items in detail responses and watch for lazy loading/N+1 problems.

---

## 6. Current API Direction

Current/expected API shape:

```text
GET    /members
POST   /members
GET    /members/{memberId}
PUT    /members/{memberId}
DELETE /members/{memberId}
GET    /members/{memberId}/orders

POST   /members/login
POST   /members/logout

GET    /products

GET    /orders
GET    /orders/{orderId}
POST   /orders
DELETE /orders/{orderId}
```

Recommended future API direction:

```text
POST   /members/signup or POST /auth/signup
POST   /members/login
POST   /members/logout
GET    /members/me or GET /me
GET    /members/me/orders or GET /me/orders

POST   /orders/{orderId}/cancel
```

---

## 7. Current Architecture

Current architecture follows layered architecture:

```text
Controller -> Service -> Repository -> Entity/Database
```

Current preferred package structure is feature-based:

```text
kr.co.prac
├── global
│   ├── entity
│   ├── exception
│   └── session
├── member
├── product
├── orders
├── login
└── PracApplication.java
```

Architecture guidance:

- Keep controllers thin.
- Keep business rules in services/domain methods.
- Keep session/request handling near the web layer while learning session mechanics.
- Use DTOs instead of exposing entities.

---

## 8. Authentication Roadmap Decision

Final authentication learning direction:

```text
Session first -> JWT later
```

Detailed order:

```text
1. Basic HttpSession login flow.
2. JSESSIONID cookie behavior.
3. Logout with session.invalidate().
4. Session timeout.
5. Current logged-in user endpoint.
6. Use session identity in business APIs.
7. Multi-server session limitation.
8. Redis Session comparison.
9. JWT comparison.
10. JWT implementation after session fundamentals are understood.
```

Do not recommend direct JWT-first implementation unless the user explicitly changes direction.

---

## 9. Recommended Next Feature

Current best next feature:

```text
GET /members/me
```

Purpose:

- Confirm that session login works across multiple requests.
- Retrieve `SessionConst.LOGIN_MEMBER_ID` from session.
- Load the current `Member` from DB.
- Return the current logged-in member as a DTO.

Expected flow:

```text
JSESSIONID -> HttpSession -> login_member_id -> MemberRepository.findById(...) -> MemberResponse
```

Recommended next order:

```text
1. Remove tiny remaining cleanup issues.
2. Add LoginRequest validation.
3. Add /members/me.
4. Add tests for login/logout/me.
5. Add password encoding.
6. Then consider Spring Security session login.
```

---

## 10. Testing Roadmap

Recommended tests now:

- Login success with correct email/password.
- Login failure when member does not exist.
- Login failure when password is invalid.
- Successful login creates session and stores `SessionConst.LOGIN_MEMBER_ID`.
- Logout invalidates an existing session.
- Logout without session does not create a new session.
- `/members/me` returns current member when session exists.
- `/members/me` returns unauthorized error when session does not exist.

---

## 11. Production-Minded Standards

### Security

- Do not store raw passwords long term.
- Do not expose sensitive fields.
- Do not trust client-provided user id after login.
- Use session/authenticated principal for user-specific operations.
- Separate user/admin permissions.
- Prefer generic login failure messages in production.

### Database

- Do not use `ddl-auto=create` in production.
- Use migration tools later.
- Add unique constraints for email.
- Add indexes where needed.

### Code

- Keep controllers thin.
- Keep business rules in service/domain methods.
- Avoid entity exposure.
- Use DTOs.
- Add tests when fixing bugs or adding features.

---

## 12. Assistant Instructions for Future Conversations

When the user asks about this project:

1. Assume the project is `jpa-prac`.
2. Answer in Korean.
3. Provide Java/Spring code when useful.
4. Connect explanations to the current product/member/order/login domain.
5. Review code with production-readiness and portfolio-readiness in mind.
6. Recommend incremental changes instead of large rewrites.
7. For authentication, follow `Session first -> JWT later`.
8. The immediate authentication next step is likely `/members/me`.

---

## 13. What To Check Later

When more precision is needed, check latest GitHub source code, especially:

- `LoginController`
- `LoginServiceImpl`
- `LoginRequest`
- `LoginResponse`
- `SessionConst`
- `Member` entity
- `ErrorCode`
- `GlobalExceptionHandler`
- `data.sql`
- local run logs
- test output
