# PROJECT_CONTEXT.md

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

The project is being developed step by step to study Spring MVC, JPA relationships, DTO design, business rules, exception handling, authentication, and authorization.

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
- `admin`
- `global.exception`
- `global.entity`
- `global.session`

Current implemented features include:

- Member signup
- Current-member lookup and update APIs
- Product list API
- Order detail/create/cancel flows
- Current-member order lookup
- Admin member lookup/delete APIs
- Admin all-orders lookup API
- DTO-based request/response separation
- Basic validation for order creation and login request
- Product stock decrease and restore logic
- Global exception handling with `BusinessException`, `ErrorCode`, and `ErrorResponse`
- JPA auditing with `BaseTimeEntity`
- Basic custom `HttpSession` login/logout
- Login request validation with `@NotBlank`, `@Email`, and `@Valid`
- Reusable session lookup helper: `SessionUtil.getLoginMemberId(HttpServletRequest)`
- Basic hardcoded admin authorization helper: `SessionUtil.requireAdmin(HttpServletRequest)`
- Current logged-in member lookup endpoint: `GET /members/me`
- Current logged-in member order lookup endpoint: `GET /members/me/orders`
- Session-based order creation through `POST /orders`
- Owner-restricted order detail through `GET /orders/{orderId}`
- Owner-restricted order cancellation through `POST /orders/{orderId}/cancel`

Current important design state:

- General member APIs are now `/members/me` centered.
- Admin member APIs were separated into `AdminMemberController` under `/admin/members`.
- Admin order APIs were separated into `AdminOrderController` under `/admin/orders`.
- Public `GET /orders` was removed from the normal `OrdersController` and moved to admin-only flow.
- Public `GET /members/{number}` and `DELETE /members/{number}` were removed from normal `MemberController` and moved to admin-only flow.
- Public `PUT /members/{number}` was replaced with current-user update `PUT /members/me`.

---

## 4. Current Authentication and Authorization Model

The project currently uses a custom session-based login flow.

### Session key

```java
SessionConst.LOGIN_MEMBER_ID = "login_member_id"
```

### Current login model

- Login endpoint stores the logged-in member id in `HttpSession`.
- Protected user APIs use `SessionUtil.getLoginMemberId(HttpServletRequest)`.
- If no session exists or no member id exists in session, `LoginRequiredException` is thrown.
- `LoginRequiredException` maps to `ErrorCode.LOGIN_REQUIRED` with HTTP `401 Unauthorized`.

### Current admin model

Admin authorization is intentionally simple for the current learning stage:

```java
private static final Long ADMIN_MEMBER_ID = 1L;
```

`SessionUtil.requireAdmin(HttpServletRequest)` checks:

1. User must be logged in.
2. Session member id must equal `1L`.
3. Otherwise `NotAuthorizedAdminException` is thrown.

Current meaning:

```text
memberId == 1 -> admin
memberId != 1 -> not admin
```

This is acceptable for the current learning stage, but should later be replaced with role-based authorization using `Role.ADMIN`, then eventually Spring Security.

---

## 5. Current Main API Surface

### Member/User APIs

Current `MemberController` is for normal member/user behavior.

```text
POST /members
PUT  /members/me
GET  /members/me
GET  /members/me/orders
```

Current meaning:

- `POST /members`: signup
- `PUT /members/me`: update current logged-in member
- `GET /members/me`: fetch current logged-in member
- `GET /members/me/orders`: fetch current logged-in member's orders

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
SessionUtil.requireAdmin(httpServletRequest)
```

Current meaning:

- `GET /admin/members`: admin all-member lookup
- `GET /admin/members/{number}`: admin member detail lookup
- `DELETE /admin/members/{number}`: admin member deletion

### Admin Order APIs

Current `AdminOrderController`:

```text
GET /admin/orders
```

Requires:

```java
SessionUtil.requireAdmin(httpServletRequest)
```

Current meaning:

- `GET /admin/orders`: admin all-order lookup

Possible future addition:

```text
GET /admin/orders/{orderId}
```

This can be added if admin needs unrestricted order detail lookup.

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
ORDER_NOT_FOUND             -> 404 Not Found
PRODUCT_NOT_FOUND           -> 404 Not Found
ALREADY_CANCELLED_ORDER     -> 400 Bad Request
NOT_ENOUGH_STOCK            -> 400 Bad Request
INVALID_INPUT_VALUE         -> 400 Bad Request
INVALID_PASSWORD            -> 401 Unauthorized
```

Admin authorization error:

```java
NotAuthorizedAdminException -> ErrorCode.NOT_AUTHORIZED_ADMIN
```

Current role/authorization caveat:

- `Member.role` exists and `MemberResponse` exposes `role`.
- Admin authorization currently does not use `Role.ADMIN`.
- Current admin check is hardcoded to `memberId == 1L`.
- This should later be refactored to role-based authorization.

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

Should not include:

- All-member lookup
- Arbitrary member lookup by id
- Arbitrary member deletion by id

### `OrdersController`

Purpose:

- Normal user order behavior only.

Current responsibilities:

- Create order for current user
- Fetch order detail for current owner
- Cancel order for current owner

Should not include:

- All-order lookup

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

---

## 9. Current Known Limitations

### Authentication/security

- Plain custom `HttpSession` login is used.
- Password comparison is still likely plain-text unless separately changed.
- Spring Security is not yet introduced.
- Admin authorization is hardcoded to `memberId == 1L`.
- Role-based admin authorization using `Role.ADMIN` is not yet implemented.
- No CSRF strategy yet.
- No cookie/session production-hardening yet.

### API design

- Some return values still use plain strings such as `"성공"`.
- Admin APIs are now separated, but admin role checking is still learning-stage code.
- Admin order detail endpoint is not yet implemented.
- Normal member account deletion through `/members/me` is not yet implemented.

### Code quality

- Some controllers may still use wildcard imports depending on IDE behavior. This is accepted for now and should not be treated as a blocker.
- Minor formatting issues are not a priority unless they affect readability or correctness.

### Testing

- Tests are intentionally postponed.
- The user wants to learn tests later in one focused testing block.
- Do not push test-heavy recommendations until the user asks for the testing block.

---

## 10. Recommended Next Steps

Current recommended next steps:

1. Manually verify the admin flow.
   - Login as member id `1`.
   - Call `GET /admin/members`.
   - Call `GET /admin/orders`.
   - Login as another member.
   - Confirm admin APIs return 403.
2. Consider adding admin order detail endpoint.
   - `GET /admin/orders/{orderId}`
   - Admin can view any order detail.
3. Consider adding current-user account deletion.
   - `DELETE /members/me`
4. Replace plain password comparison with BCrypt/password encoder.
5. Later replace hardcoded admin id with role-based authorization.
6. Later introduce Spring Security.
7. Later learn and add tests in one dedicated testing block.

---

## 11. Recommended Commit Messages by Future Step

Admin order detail:

```bash
git commit -m "feat: add admin order detail lookup"
```

Current user deletion:

```bash
git commit -m "feat: add current member deletion"
```

Password encoding:

```bash
git commit -m "feat: add password encoder"
```

Role-based admin authorization:

```bash
git commit -m "refactor: use role based admin authorization"
```

Spring Security migration:

```bash
git commit -m "feat: add spring security session login"
```

---

## 12. Assistant Guidance for Next Sessions

When continuing this project:

- Speak Korean unless the user asks otherwise.
- Review diffs directly and say whether they are commit-ready.
- Prioritize correctness and learning over over-engineering.
- Do not recommend JWT before the session/Spring Security basics are clear.
- Do not add tests unless the user explicitly starts the testing block.
- Do not focus on whitespace/import style unless it affects compile, readability, or consistency materially.
- For API design, separate normal-user behavior from admin behavior.
- For authorization, keep current learning-stage implementation but point toward role-based authorization later.
- For GitHub checks, verify current repo state before generating new docs.

