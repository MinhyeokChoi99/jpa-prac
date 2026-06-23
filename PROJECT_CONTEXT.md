# PROJECT_CONTEXT.md

# Project Context: jpa-prac

_Last updated: 2026-06-24 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `order_item`, and basic `login`
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
- Login request validation with `@NotBlank`, `@Email`, and `@Valid`
- Reusable session lookup helper: `SessionUtil.getLoginMemberId(HttpServletRequest)`
- Current logged-in member lookup endpoint: `GET /members/me`
- Current logged-in member order lookup endpoint: `GET /members/me/orders`
- Session-based order creation through `POST /orders`
- `OrderCreateRequest` no longer accepts `memberId`; the order member is derived from the session
- Default newly created member role changed from `ADMIN` to `USER`
- Login-required exception flow using `ErrorCode.LOGIN_REQUIRED` and `LoginRequiredException`

---

## 4. Current Session Login Implementation

Current authentication is a learning-stage custom `HttpSession` implementation, not yet Spring Security-based.

Current authentication/user endpoints:

```text
POST /members/login
POST /members/logout
GET  /members/me
GET  /members/me/orders
```

Current session-protected business endpoint:

```text
POST /orders
```

Current login flow:

```text
1. Client sends email/password to POST /members/login.
2. LoginRequest is validated with Bean Validation.
3. LoginService finds Member by email.
4. LoginService compares request password with DB password.
5. If valid, LoginResponse is created from Member.
6. Controller creates/retrieves HttpSession.
7. Controller stores member id in session using SessionConst.LOGIN_MEMBER_ID.
8. Browser receives JSESSIONID cookie from the server.
9. Later requests can be associated with the same session through JSESSIONID.
```

Current logout flow:

```text
1. Client sends POST /members/logout with JSESSIONID cookie.
2. LoginService calls request.getSession(false).
3. If an existing session exists, it is invalidated.
4. The login state for that browser session is removed.
```

Current `/members/me` flow:

```text
1. Client sends GET /members/me.
2. MemberController calls SessionUtil.getLoginMemberId(request).
3. SessionUtil calls request.getSession(false).
4. If no session exists, LoginRequiredException is thrown.
5. SessionUtil reads SessionConst.LOGIN_MEMBER_ID from session.
6. If no login member id exists, LoginRequiredException is thrown.
7. MemberService finds the current member by id.
8. Controller returns MemberResponse.
```

Current `/members/me/orders` flow:

```text
1. Client sends GET /members/me/orders.
2. MemberController obtains the login member id through SessionUtil.
3. OrderService finds orders belonging to the login member id.
4. Controller returns List<OrdersResponse>.
```

Current session-based order creation flow:

```text
1. Client sends POST /orders with JSESSIONID.
2. Request body contains productNumber and count only.
3. OrdersController obtains login member id through SessionUtil.
4. If the client is not logged in, LoginRequiredException is thrown.
5. OrderService.createOrder(memberId, requestList) creates the order for the session member.
6. OrderCreateRequest no longer accepts memberId from the client.
```

Current session key:

```java
SessionConst.LOGIN_MEMBER_ID = "login_member_id";
```

Current design decisions:

- `SessionConst` is placed in `kr.co.prac.global.session`.
- `LoginResponse.memberId` exists for server-side session storage but is hidden from JSON response with `@JsonIgnore`.
- `ErrorCode.INVALID_PASSWORD` uses `HttpStatus.UNAUTHORIZED`.
- `ErrorCode.LOGIN_REQUIRED` uses `HttpStatus.UNAUTHORIZED`.
- `InvalidPasswordException` delegates to `ErrorCode.INVALID_PASSWORD`.
- `LoginRequiredException` delegates to `ErrorCode.LOGIN_REQUIRED`.
- Login currently uses plain password comparison as a learning-stage implementation.
- `GET /members/me` is used instead of `GET /me` for now to keep beginner-stage API naming consistent with existing member endpoints.
- `GET /members/me/orders` is used for current-user order lookup.
- `SessionUtil` is currently used as the reusable helper for extracting the login member id from the session.
- `POST /orders` now requires login and derives the order member from the session.

Confirmed remaining cleanup:

- Password comparison should later be replaced with `PasswordEncoder.matches()`.
- `data.sql` role values are now quoted; password seed values should also preferably be single-quoted strings.
- Order cancel logic should next verify that the order belongs to the logged-in member before cancellation.
- `GET /members/{memberId}/orders` still exists and should later be reviewed or restricted because logged-in user APIs should prefer session identity.
- `SessionUtil` is acceptable for the current learning stage; later compare it with argument resolver, interceptor, Spring Security principal, or a dedicated authentication abstraction.

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
- New members should default to `Role.USER`; admin privileges should be handled separately later.

### Product

Product stock changes through domain methods. Future work includes product detail, admin CRUD, search, pagination, and authorization.

### Orders

Orders currently support list/detail/create/cancel. Current-user order lookup and order creation now use session identity. Future work should restrict order cancellation to the logged-in order owner and prefer `POST /orders/{orderId}/cancel` for cancellation semantics.

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
GET    /members/me
GET    /members/me/orders

POST   /members/login
POST   /members/logout

GET    /products

GET    /orders
GET    /orders/{orderId}
POST   /orders        # login required; member id comes from session
POST   /orders/{orderId}  # currently used for cancel; should later become /orders/{orderId}/cancel
```

Recommended future API direction:

```text
POST   /members/signup or POST /auth/signup
POST   /members/login
POST   /members/logout
GET    /members/me
GET    /members/me/orders or GET /me/orders

POST   /orders/{orderId}/cancel
```

Important API direction after login:

- For user-specific APIs, avoid trusting `memberId` from the URL or request body.
- Prefer deriving the current user from the session.
- `GET /members/me/orders` now exists and should be preferred over `GET /members/{memberId}/orders` for logged-in user order lookup.
- `POST /orders` now derives the order owner from the session instead of the request body.
- Next high-priority change: order cancellation must verify that the target order belongs to the logged-in member.

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
- Use service interfaces in controllers where available instead of directly depending on implementation classes.
- Do not introduce large package restructuring in the same commit as a small feature.

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
5. Current logged-in user endpoint. [DONE: GET /members/me]
6. Current-user order lookup. [DONE: GET /members/me/orders]
7. Use session identity in business APIs. [IN PROGRESS: POST /orders uses session member id]
8. Restrict business commands by owner, starting with order cancellation.
9. Multi-server session limitation.
10. Redis Session comparison.
11. JWT comparison.
12. JWT implementation after session fundamentals are understood.
```

Do not recommend direct JWT-first implementation unless the user explicitly changes direction.

---

## 9. Recommended Next Feature

Current best next feature:

```text
Restrict order cancellation to the logged-in order owner
```

Recently completed session-identity features:

```text
GET  /members/me
GET  /members/me/orders
POST /orders
```

Purpose already achieved:

- Confirm that session login works across multiple requests.
- Retrieve `SessionConst.LOGIN_MEMBER_ID` through `SessionUtil`.
- Load the current `Member` from DB.
- Return the current logged-in member as `MemberResponse`.
- Return the current logged-in member's order list through `GET /members/me/orders`.
- Create orders only for the logged-in session member through `POST /orders`.
- Remove client-controlled `memberId` from `OrderCreateRequest`.

Current session identity flow:

```text
JSESSIONID -> HttpSession -> login_member_id -> SessionUtil -> Service layer method argument
```

Recommended next order:

```text
1. Manually verify login -> /members/me -> /members/me/orders -> POST /orders -> logout.
2. Change order cancellation to use session identity.
3. Verify that a user cannot cancel another member's order.
4. Rename the cancel endpoint from POST /orders/{orderId} to POST /orders/{orderId}/cancel.
5. Add password encoding with BCrypt.
6. Add tests later as a separate learning block.
7. Then consider Spring Security session login.
```

Testing is intentionally postponed for now because the user wants to learn tests later in one focused block.

---

## 10. Testing Roadmap

Testing is postponed for now.

When the user starts the testing block, recommended tests include:

- Login success with correct email/password.
- Login failure when member does not exist.
- Login failure when password is invalid.
- Successful login creates session and stores `SessionConst.LOGIN_MEMBER_ID`.
- Logout invalidates an existing session.
- Logout without session does not create a new session.
- `/members/me` returns current member when session exists.
- `/members/me` returns unauthorized error when session does not exist.
- `/members/me` returns unauthorized error after logout.
- `/members/me/orders` returns only the current member's orders.
- `POST /orders` fails with unauthorized error when no login session exists.
- `POST /orders` creates an order for the session member and ignores any client-supplied member identity because the DTO no longer accepts `memberId`.
- Order cancellation fails when the target order does not belong to the logged-in member, after that feature is implemented.

Do not force tests into the current small feature/refactoring steps unless the user asks.

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
- Add tests when fixing bugs or adding features, but respect the current learning decision to postpone testing.

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
8. `/members/me` is now implemented.
9. `/members/me/orders` is now implemented.
10. `POST /orders` now uses session member id instead of request-body `memberId`.
11. The immediate next authentication/business step is restricting order cancellation to the logged-in order owner.
12. Do not recommend tests immediately unless the user asks; the user wants to learn tests later in one focused block.

---

## 13. What To Check Later

When more precision is needed, check latest GitHub source code, especially:

- `LoginController`
- `LoginServiceImpl`
- `LoginRequest`
- `LoginResponse`
- `SessionConst`
- `SessionUtil`
- `MemberController`
- `OrdersController`
- `OrderCreateRequest`
- `OrderService`
- `OrderServiceImpl`
- `Member` entity
- `MemberService`
- `ErrorCode`
- `LoginRequiredException`
- `GlobalExceptionHandler`
- `data.sql`
- local run logs
- test output when testing starts
