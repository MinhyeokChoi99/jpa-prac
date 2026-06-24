# PROJECT_CONTEXT.md

# Project Context: jpa-prac

_Last updated: 2026-06-24 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `order_item`, and custom session-based `login`
- User language: Korean
- Preferred assistant style: Korean explanation, Java/Spring examples, direct code review, incremental production-minded guidance

The project is being developed step by step to study Spring MVC, JPA relationships, DTO design, business rules, exception handling, and authentication/authorization.

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
- Basic custom `HttpSession` login/logout
- Login request validation with `@NotBlank`, `@Email`, and `@Valid`
- Reusable session lookup helper: `SessionUtil.getLoginMemberId(HttpServletRequest)`
- Current logged-in member lookup endpoint: `GET /members/me`
- Current logged-in member order lookup endpoint: `GET /members/me/orders`
- Session-based order creation through `POST /orders`
- `OrderCreateRequest` no longer accepts `memberId`; the order member is derived from the session
- Order cancellation is restricted to the logged-in order owner
- Cancel endpoint is semantically named: `POST /orders/{orderId}/cancel`
- Order detail lookup is restricted to the logged-in order owner
- Order detail response includes order items and total order price
- `OrderItem` stores `unitPrice` and `count`; item/order totals are calculated from those values
- Default newly created member role changed from `ADMIN` to `USER`
- Login-required exception flow using `ErrorCode.LOGIN_REQUIRED` and `LoginRequiredException`
- Order authorization exception flow using forbidden error codes/exceptions

---

## 4. Current Session Login Implementation

Current authentication is a learning-stage custom `HttpSession` implementation, not yet Spring Security-based.

Current authentication/current-user endpoints:

```text
POST /members/login
POST /members/logout
GET  /members/me
GET  /members/me/orders
```

Current session-protected order endpoints:

```text
POST /orders
GET  /orders/{orderId}
POST /orders/{orderId}/cancel
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

Current session key:

```java
SessionConst.LOGIN_MEMBER_ID = "login_member_id";
```

Current `SessionUtil` role:

```text
1. Controller receives HttpServletRequest.
2. Controller calls SessionUtil.getLoginMemberId(request).
3. SessionUtil calls request.getSession(false), so it does not create a new session for unauthenticated users.
4. If no session exists, LoginRequiredException is thrown.
5. If SessionConst.LOGIN_MEMBER_ID does not exist, LoginRequiredException is thrown.
6. Otherwise, the login member id is returned to the controller.
```

Current design decisions:

- `SessionConst` is placed in `kr.co.prac.global.session`.
- `SessionUtil` is the current learning-stage helper for extracting the login member id from session.
- Controllers should extract session identity and pass `loginMemberId` into services.
- Services should receive primitive/domain identifiers such as `orderId`, `loginMemberId`, not `HttpServletRequest`.
- Login currently uses plain password comparison as a learning-stage implementation.
- `GET /members/me` is used instead of `GET /me` for now to keep beginner-stage API naming consistent with existing member endpoints.
- `GET /members/me/orders` is used for current-user order lookup.
- `POST /orders` requires login and derives the order member from session.
- `GET /orders/{orderId}` requires login and only allows the owner to view the detail.
- `POST /orders/{orderId}/cancel` requires login and only allows the owner to cancel.

Confirmed remaining cleanup:

- Password comparison should later be replaced with `PasswordEncoder.matches()`.
- `data.sql` role values are now quoted; password seed values should also preferably be single-quoted strings.
- `GET /members/{memberId}/orders` still exists and should later be reviewed, restricted, or treated as admin-only because logged-in user APIs should prefer session identity.
- `GET /orders` currently exposes all orders; later decide whether this should be admin-only or removed from normal user API.
- `SessionUtil` is acceptable for the current learning stage; later compare it with argument resolver, interceptor, Spring Security principal, or a dedicated authentication abstraction.
- Some Korean comments/messages may display as mojibake in Git Bash/diff output; later normalize file encoding/messages if needed.

---

## 5. Current Domain Understanding

### Member

A member represents a user/customer-like entity and is now also the authentication subject.

Known fields include or are expected to include:

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

Product stock changes through domain methods.

Current order behavior:

- Order creation decreases product stock.
- Order cancellation restores product stock.

Future work:

- Product detail
- Admin product CRUD
- Search/pagination/category
- Authorization for product management

### Orders

Orders currently support:

- List
- Owner-restricted detail
- Session-member-based creation
- Owner-restricted cancellation
- Current-user order lookup through `GET /members/me/orders`

Important current behavior:

- `POST /orders` does not accept `memberId` in request body.
- `POST /orders` uses `SessionUtil` to obtain `loginMemberId`.
- `GET /orders/{orderId}` uses `loginMemberId` to verify the order owner.
- `POST /orders/{orderId}/cancel` uses `loginMemberId` to verify the order owner.
- Unauthorized order access/cancel throws domain/business exceptions using `HttpStatus.FORBIDDEN`.

Future work:

- Decide whether `GET /orders` should be admin-only.
- Decide whether `GET /members/{memberId}/orders` should remain, become admin-only, or be removed.
- Consider returning an updated response after cancel instead of `void`.

### OrderItem

OrderItem connects Orders and Product.

Current price model:

```text
OrderItem.unitPrice = product price at order time
OrderItem.count = ordered quantity
OrderItemResponse.totalPrice = unitPrice * count
OrderDetailResponse.totalPrice = sum of item total prices
```

Important reasoning:

- Store order-time unit price in `OrderItem`, not only current `Product.price`.
- Product price may change later, but past order detail must preserve the price at the time of order.
- Storing `unitPrice` and `count` is cleaner than storing only line total because line total can be calculated.

Future work:

- Consider fetch join or DTO projection later to avoid lazy loading/N+1 issues.
- Consider naming API response field as `unitPrice` instead of `productPrice` for clarity if not already done locally.

---

## 6. Current API Direction

Current API shape:

```text
GET    /members
POST   /members
GET    /members/{memberId}
PUT    /members/{memberId}
DELETE /members/{memberId}
GET    /members/{memberId}/orders       # legacy/path-variable based; review later
GET    /members/me
GET    /members/me/orders               # preferred current-user order list

POST   /members/login
POST   /members/logout

GET    /products

GET    /orders                          # all orders; review/admin-only later
GET    /orders/{orderId}                # login required; owner only
POST   /orders                          # login required; member id from session
POST   /orders/{orderId}/cancel         # login required; owner only
```

Recommended future API direction:

```text
POST   /members/signup or POST /auth/signup
POST   /members/login
POST   /members/logout
GET    /members/me
GET    /members/me/orders or GET /me/orders
GET    /orders/{orderId}                # owner-only or admin
POST   /orders/{orderId}/cancel         # owner-only or admin
```

Important API direction after login:

- For user-specific APIs, avoid trusting `memberId` from URL or request body.
- Prefer deriving the current user from session.
- `GET /members/me/orders` should be preferred over `GET /members/{memberId}/orders` for normal logged-in users.
- `POST /orders` now derives the order owner from session instead of request body.
- Order detail and order cancellation are now protected by owner checks.

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
- Keep web/session handling in the controller layer while learning session mechanics.
- Do not pass `HttpServletRequest` into service methods.
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
1. Basic HttpSession login flow. [DONE]
2. JSESSIONID cookie behavior. [DONE/learning]
3. Logout with session.invalidate(). [DONE]
4. Current logged-in user endpoint. [DONE: GET /members/me]
5. Current-user order lookup. [DONE: GET /members/me/orders]
6. Use session identity in order creation. [DONE: POST /orders]
7. Restrict order cancellation by owner. [DONE]
8. Restrict order detail by owner. [DONE]
9. Review legacy memberId/path-variable APIs.
10. Multi-server session limitation.
11. Redis Session comparison.
12. JWT comparison.
13. JWT implementation after session fundamentals are understood.
```

Do not recommend direct JWT-first implementation unless the user explicitly changes direction.

---

## 9. Recommended Next Feature

Current best next feature:

```text
Review and restrict legacy memberId/order list APIs
```

The highest-priority security/business flow has been improved:

```text
POST /orders                     # login required; session member creates order
GET  /orders/{orderId}           # login required; owner-only detail
POST /orders/{orderId}/cancel    # login required; owner-only cancel
```

Recommended next order:

```text
1. Manually verify login -> POST /orders -> GET /orders/{orderId} -> POST /orders/{orderId}/cancel.
2. Verify that another logged-in member cannot view or cancel the order.
3. Review GET /members/{memberId}/orders and decide whether to remove, restrict, or keep as admin-only later.
4. Review GET /orders and decide whether it should become admin-only later.
5. Rename/clarify response field productPrice -> unitPrice if not already completed locally.
6. Add BCrypt/password encoder.
7. Add tests later as a separate learning block.
8. Then consider Spring Security session login.
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
- `GET /members/me` returns current member when session exists.
- `GET /members/me` returns unauthorized error when session does not exist.
- `GET /members/me/orders` returns only orders for the session member.
- `POST /orders` returns 401 when no session exists.
- `POST /orders` creates the order for the session member.
- `GET /orders/{orderId}` returns detail for owner.
- `GET /orders/{orderId}` returns 403 for non-owner.
- `POST /orders/{orderId}/cancel` cancels owner order.
- `POST /orders/{orderId}/cancel` returns 403 for non-owner.
- Cancelled order restores product stock.
- Already cancelled order cannot be cancelled again.

---

## 11. Important Current Warnings

- Plain password comparison is still used.
- Passwords should later be encoded with BCrypt.
- Some list endpoints still expose broad data and need an admin/owner design decision later.
- Current custom session implementation is appropriate for learning but should later be compared with Spring Security.
- Do not jump to JWT yet.
- Do not add tests now unless the user explicitly starts the testing block.

---

## 12. Handoff Summary

Use this when continuing the project in a new chat:

```text
This project is jpa-prac, a Java 21 + Spring Boot + MySQL practice project.
The goal is to grow it into a portfolio-level Spring web application.
The main domains are Member, Product, Orders, OrderItem, and custom Login/session.

Current auth state:
- Custom HttpSession login/logout is implemented.
- SessionConst.LOGIN_MEMBER_ID stores the authenticated member id.
- SessionUtil.getLoginMemberId(request) extracts the login member id or throws LoginRequiredException.
- GET /members/me is implemented.
- GET /members/me/orders is implemented.
- POST /orders uses session member id; OrderCreateRequest no longer accepts memberId.
- GET /orders/{orderId} is owner-restricted.
- POST /orders/{orderId}/cancel is owner-restricted.
- OrderItem stores unitPrice and count; order item total and order total are calculated in response DTOs.
- Tests are postponed for later.

Current next priority:
1. Manually verify owner restrictions for order detail/cancel.
2. Review legacy GET /members/{memberId}/orders and broad GET /orders APIs.
3. Add BCrypt/password encoder later.
4. Learn tests later in one focused block.
5. Compare Spring Security session login, Redis Session, and JWT later.
```
