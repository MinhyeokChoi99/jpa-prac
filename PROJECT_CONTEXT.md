# Project Context: jpa-prac

_Last updated: 2026-06-25 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `order_item`, custom session-based `login`, and basic `admin` APIs
- User language: Korean
- Preferred assistant style: Korean explanation, Java/Spring examples, direct code review, incremental production-minded guidance

The project is being developed step by step to study Spring MVC, JPA relationships, DTO design, business rules, exception handling, authentication, authorization, session management, and future production-minded backend architecture.

---

## 2. Current Confirmed Tech Stack

Current confirmed stack:

- Java 21
- Spring Boot 4.1.0
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
- Spring Security Crypto
- BCryptPasswordEncoder through `PasswordEncoder`
- QueryDSL dependencies are present, but custom QueryDSL repository/query usage has not been confirmed yet

Current authentication/security stack status:

- Custom `HttpSession` login is currently used.
- Passwords are encoded with BCrypt during signup.
- Login verifies passwords through `PasswordEncoder.matches(...)`.
- Full Spring Security authentication/authorization has not been introduced yet.
- Redis Session has not been introduced yet.
- JWT has not been introduced yet.

Potential future stack:

- Spring Security session login
- Spring Session
- Redis Session
- JWT
- Spring profiles
- Flyway or Liquibase
- QueryDSL custom repositories
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
- `admin`
- `global.exception`
- `global.entity`
- `global.session`
- `global.config`

Current implemented features include:

- Member signup
- BCrypt password encoding for signup
- BCrypt password verification for login
- Current-member lookup, update, and delete APIs
- Product list API
- Order detail/create/cancel flows
- Current-member order lookup
- Admin member lookup/delete APIs
- Admin all-orders lookup API
- Admin order detail lookup API
- DTO-based request/response separation
- Basic validation for order creation and login request
- Product stock decrease and restore logic
- Global exception handling with `BusinessException`, `ErrorCode`, and `ErrorResponse`
- JPA auditing with `BaseTimeEntity`
- Basic custom `HttpSession` login/logout
- Login request validation with `@NotBlank`, `@Email`, and `@Valid`
- Reusable session lookup helper: `SessionUtil.getLoginMemberId(HttpServletRequest)`
- Role-based admin authorization helper: `AdminAuthService.requireAdmin(HttpServletRequest)`
- Current logged-in member lookup endpoint: `GET /members/me`
- Current logged-in member order lookup endpoint: `GET /members/me/orders`
- Current logged-in member deletion endpoint: `DELETE /members/me`
- Session-based order creation through `POST /orders`
- Owner-restricted order detail through `GET /orders/{orderId}`
- Owner-restricted order cancellation through `POST /orders/{orderId}/cancel`
- Admin unrestricted order detail through `GET /admin/orders/{orderId}`

Current important design state:

- General member APIs are now `/members/me` centered.
- Admin member APIs were separated into `AdminMemberController` under `/admin/members`.
- Admin order APIs were separated into `AdminOrderController` under `/admin/orders`.
- Public `GET /orders` was removed from the normal `OrdersController` and moved to admin-only flow.
- Public `GET /members/{number}` and `DELETE /members/{number}` were removed from normal `MemberController` and moved to admin-only flow.
- Public `PUT /members/{number}` was replaced with current-user update `PUT /members/me`.
- Normal user account deletion is now exposed as `DELETE /members/me`.
- Admin order detail lookup is now exposed as `GET /admin/orders/{orderId}`.

---

## 4. Current Authentication and Authorization Model

The project currently uses a custom session-based login flow.

### Session key

```java
SessionConst.LOGIN_MEMBER_ID = "login_member_id";
```

### Current login model

- Login endpoint validates email/password through `LoginService`.
- Login uses `PasswordEncoder.matches(rawPassword, encodedPassword)`.
- Login endpoint stores the logged-in member id in `HttpSession`.
- Protected user APIs use `SessionUtil.getLoginMemberId(HttpServletRequest)`.
- If no session exists or no member id exists in session, `LoginRequiredException` is thrown.
- `LoginRequiredException` maps to `ErrorCode.LOGIN_REQUIRED` with HTTP `401 Unauthorized`.

### Current password model

- Signup encodes the request password through `PasswordEncoder.encode(...)`.
- BCrypt is used through `BCryptPasswordEncoder` configured in `PasswordConfig`.
- Login compares the raw request password with the encoded stored password through `PasswordEncoder.matches(...)`.
- Plain-text password comparison should no longer be considered the current implementation state.

### Current admin model

Admin authorization is now role-based at the helper-service level.

Current rule:

```text
Member.role == Role.ADMIN -> admin
Member.role != Role.ADMIN -> not admin
```

Implementation direction:

- Admin controllers call `AdminAuthService.requireAdmin(HttpServletRequest)`.
- `AdminAuthService` internally calls `SessionUtil.getLoginMemberId(...)`.
- It then loads the member and checks `member.role == Role.ADMIN`.
- If the member is not an admin, `NotAuthorizedAdminException` is thrown.

Current caveat:

- Admin authorization is no longer hardcoded as `memberId == 1L`.
- However, authorization is still not handled through Spring Security yet.
- Later, `/admin/**` should be protected through Spring Security rules such as `hasRole("ADMIN")`.

---

## 5. Current Main API Surface

### Member/User APIs

Current `MemberController` is for normal member/user behavior.

```text
POST   /members
PUT    /members/me
GET    /members/me
GET    /members/me/orders
DELETE /members/me
```

Current meaning:

- `POST /members`: signup
- `PUT /members/me`: update current logged-in member
- `GET /members/me`: fetch current logged-in member
- `GET /members/me/orders`: fetch current logged-in member's orders
- `DELETE /members/me`: delete current logged-in member

Removed or moved away from normal member controller:

```text
GET    /members
GET    /members/{number}
PUT    /members/{number}
DELETE /members/{number}
```

Reason:

- These path-id based member APIs are admin-like or unsafe for normal users.
- Normal users should act on themselves through `/members/me`.

### Login APIs

```text
POST /members/login
POST /members/logout
```

Current meaning:

- `POST /members/login`: validates email/password, creates session, stores login member id
- `POST /members/logout`: invalidates current session if it exists

### Order/User APIs

Current `OrdersController` is for normal user order behavior.

```text
GET  /orders/{orderId}
POST /orders
POST /orders/{orderId}/cancel
```

Current meaning:

- `GET /orders/{orderId}`: fetch one order detail, but only if it belongs to the logged-in member
- `POST /orders`: create an order for the logged-in member; request body does not include `memberId`
- `POST /orders/{orderId}/cancel`: cancel an order, but only if it belongs to the logged-in member

Removed or moved away from normal order controller:

```text
GET /orders
```

Reason:

- All-orders lookup is admin behavior, not normal-user behavior.

### Admin Member APIs

Current `AdminMemberController`:

```text
GET    /admin/members
GET    /admin/members/{number}
DELETE /admin/members/{number}
```

All require:

```java
adminAuthService.requireAdmin(httpServletRequest);
```

Current meaning:

- `GET /admin/members`: admin all-member lookup
- `GET /admin/members/{number}`: admin member detail lookup
- `DELETE /admin/members/{number}`: admin member deletion

### Admin Order APIs

Current `AdminOrderController`:

```text
GET /admin/orders
GET /admin/orders/{orderId}
```

All require:

```java
adminAuthService.requireAdmin(httpServletRequest);
```

Current meaning:

- `GET /admin/orders`: admin all-order lookup
- `GET /admin/orders/{orderId}`: admin unrestricted order detail lookup

### Product APIs

Product list/read behavior can remain public for now because product browsing is normal user behavior.

Expected product endpoint:

```text
GET /products
```

---

## 6. Current Order Model and Pricing Decision

The current order-item design uses unit price and count.

Current intended meaning:

```text
OrderItem.unitPrice = product price at order creation time
OrderItem.count = ordered quantity
OrderItemResponse.totalPrice = unitPrice * count
OrderDetailResponse.totalPrice = sum of each order item totalPrice
```

Important decision:

- `OrderItem` should store the ordered unit price, not the line total.
- This preserves the product price at the time of order.
- Later changes to `Product.price` should not retroactively change old order totals.

Current response structure intent:

```json
{
  "number": 1,
  "memberId": 1,
  "orderDate": "...",
  "status": "ORDER",
  "orderItems": [
    {
      "number": 10,
      "productName": "Keyboard",
      "unitPrice": 30000,
      "count": 2,
      "totalPrice": 60000
    }
  ],
  "totalPrice": 60000
}
```

---

## 7. Current Exception Model

Important `ErrorCode` values include:

```text
LOGIN_REQUIRED              -> 401 Unauthorized
NOT_AUTHORIZED_CANCEL       -> 403 Forbidden
NOT_AUTHORIZED_MEMBER       -> 403 Forbidden
NOT_AUTHORIZED_ADMIN        -> 403 Forbidden
MEMBER_NOT_FOUND            -> 404 Not Found
ALREADY_EXIST_MEMBER        -> 409 Conflict
ORDER_NOT_FOUND             -> 404 Not Found
PRODUCT_NOT_FOUND           -> 404 Not Found
EMPTY_ITEM_ORDER            -> 400 Bad Request
ALREADY_CANCELLED_ORDER     -> 400 Bad Request
NOT_ENOUGH_STOCK            -> 400 Bad Request
INVALID_INPUT_VALUE         -> 400 Bad Request
INVALID_PASSWORD            -> 401 Unauthorized
INTERNAL_SERVER_ERROR       -> 500 Internal Server Error
```

Admin authorization error:

```java
NotAuthorizedAdminException -> ErrorCode.NOT_AUTHORIZED_ADMIN
```

Current role/authorization caveat:

- `Member.role` exists and `MemberResponse` exposes `role`.
- Admin authorization now uses `Role.ADMIN` through `AdminAuthService`.
- Spring Security authorization rules are not yet introduced.

---

## 8. Current Controller Responsibilities

### `MemberController`

Purpose:

- Normal user/member behavior only.

Current responsibilities:

- Signup
- Current user lookup
- Current user update
- Current user's order list
- Current user deletion

Should not include:

- All-member lookup
- Arbitrary member lookup by id
- Arbitrary member deletion by id

### `LoginController`

Purpose:

- Custom session login/logout.

Current responsibilities:

- Login through email/password
- Store login member id in session
- Logout by invalidating session

Should later be reviewed when migrating to Spring Security.

### `OrdersController`

Purpose:

- Normal user order behavior only.

Current responsibilities:

- Create order for current user
- Fetch order detail for current owner
- Cancel order for current owner

Should not include:

- All-order lookup
- Unrestricted order detail lookup

### `AdminMemberController`

Purpose:

- Admin member management.

Current responsibilities:

- All-member lookup
- Member detail lookup by id
- Member deletion by id

### `AdminOrderController`

Purpose:

- Admin order management.

Current responsibilities:

- All-order lookup
- Unrestricted order detail lookup by id

---

## 9. Current Known Limitations

### Authentication/security

- Plain custom `HttpSession` login is used.
- Password encoding has been introduced with BCrypt.
- Spring Security authentication/authorization is not yet introduced.
- Admin authorization is role-based through `AdminAuthService`, but not yet through Spring Security request authorization rules.
- No CSRF strategy yet.
- No cookie/session production-hardening yet.
- Redis Session is not yet introduced.
- JWT is not yet introduced.

### API design

- Some return values still use plain strings such as `"성공"`.
- Admin APIs are separated, but authorization is still implemented manually through helper/service code.
- A consistent success response format has not been fully standardized.

### Code quality

- Some controllers may still use wildcard imports depending on IDE behavior. This is accepted for now and should not be treated as a blocker.
- Minor formatting issues are not a priority unless they affect readability or correctness.
- `AdminAuthService` currently depends on `MemberService.find(...)`, which returns a DTO. Later, admin authorization may be cleaner if it uses a domain/entity lookup or Spring Security `UserDetails` instead.

### Testing

- Tests are intentionally postponed.
- The user wants to learn tests later in one focused testing block.
- Do not push test-heavy recommendations until the user asks for the testing block.

---

## 10. Recommended Next Steps

Current recommended next steps:

1. Manually verify signup/login/logout with BCrypt password flow.
   - Signup creates encoded password.
   - Login succeeds with correct raw password.
   - Login fails with incorrect password.
   - Logout invalidates session.
   - `GET /members/me` fails after logout.
2. Manually verify role-based admin authorization.
   - Login as `Role.ADMIN` member.
   - Call `GET /admin/members`; expect success.
   - Call `GET /admin/orders`; expect success.
   - Call `GET /admin/orders/{orderId}`; expect success.
   - Login as `Role.USER` member.
   - Confirm admin APIs return `403 Forbidden`.
3. Manually verify owner-restricted order APIs.
   - Own order detail succeeds.
   - Another member's order detail returns `403 Forbidden`.
   - Own order cancel succeeds.
   - Another member's order cancel returns `403 Forbidden`.
4. Manually verify `DELETE /members/me`.
5. Decide how admin role is assigned or seeded.
   - Current signup defaults new members to `Role.USER`.
   - Need a clear learning-stage way to create or update an admin member.
6. Clean remaining plain string responses such as `"성공"`.
7. Prepare for Spring Security session login migration.
8. After Spring Security session login is stable, consider Redis Session.
9. Learn and add tests later in one dedicated testing block.

---

## 11. Recommended Authentication Roadmap

Recommended order:

```text
1. Verify the current custom HttpSession login flow.
2. Migrate to Spring Security session login.
3. Move role-based authorization into Spring Security rules.
4. Introduce Redis Session through Spring Session.
5. Compare JWT later after session/Spring Security basics are clear.
```

### Why Spring Security before Redis

Spring Security should come before Redis because it handles the core security responsibilities:

- Authentication
- Authorization
- Login/logout flow
- Password verification integration
- Session management rules
- CSRF strategy
- Authentication/authorization error responses
- Role-based request protection such as `/admin/**`

Redis does not replace authentication or authorization. Redis is mainly useful as an external session store.

### When Redis becomes useful

Redis Session becomes useful when:

- The server may run multiple instances.
- Session data should survive application restart/deployment better than in-memory sessions.
- Session storage should be separated from the application process.
- Spring Session should manage sessions consistently across infrastructure.

Target direction:

```text
Current custom HttpSession
    -> Spring Security session login
    -> Spring Session + Redis
    -> JWT comparison later
```

---

## 12. Recommended Commit Messages by Future Step

Manual verification / documentation:

```bash
git commit -m "docs: update project context and log"
```

Clean success responses:

```bash
git commit -m "refactor: standardize success responses"
```

Admin role setup:

```bash
git commit -m "feat: add admin role setup flow"
```

Spring Security session login:

```bash
git commit -m "feat: add spring security session login"
```

Redis Session:

```bash
git commit -m "feat: add redis backed session management"
```

Testing block:

```bash
git commit -m "test: add authentication and order authorization tests"
```

---

## 13. Assistant Guidance for Next Sessions

When continuing this project:

- Speak Korean unless the user asks otherwise.
- Review diffs directly and say whether they are commit-ready.
- Prioritize correctness and learning over over-engineering.
- Do not recommend JWT before the session/Spring Security basics are clear.
- Recommended authentication order is Spring Security session login first, Redis Session second, JWT later.
- Do not add tests unless the user explicitly starts the testing block.
- Do not focus on whitespace-only issues unless requested.
- Do not flag wildcard imports as an issue unless they cause problems.
- For API design, separate normal-user behavior from admin behavior.
- For authorization, note that admin authorization is currently `Role.ADMIN` based, but not yet Spring Security based.
- For GitHub checks, verify current repo state before generating new docs.
