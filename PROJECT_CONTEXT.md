# Project Context: jpa-prac

_Last updated: 2026-06-26 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `order_item`, custom session-based `login`, and basic `admin` APIs
- User language: Korean
- Preferred assistant style: Korean explanation, Java/Spring examples, direct code review, incremental production-minded guidance

The project is being developed step by step to study Spring MVC, JPA relationships, DTO design, business rules, exception handling, authentication, authorization, session management, and future production-minded backend architecture.

This project is not just a simple CRUD exercise. It is intended to grow gradually into a portfolio-level and production-minded Spring web application.

The user wants to study Java and Spring by building, reviewing, refactoring, and extending this project step by step.

---

## 2. User Intent

The user is learning Java and Spring with the goal of eventually building a deployable web service.

When assisting with this project, do not only provide working code. Explain:

- Why a certain structure is used
- Whether the code follows modern Spring best practices
- Whether the implementation may cause performance, maintainability, or production issues
- How to refactor the code gradually
- What should be fixed now versus what can be improved later
- What the next feature should be and why

The user wants feedback on:

- Outdated coding style
- Poor performance patterns
- Better Spring/JPA practices
- Architecture improvements
- Feature roadmap
- Portfolio-readiness
- Production-readiness

---

## 3. Current Confirmed Tech Stack

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
- Spring Security dependency is present
- Spring Security Crypto is used for password encoding
- BCryptPasswordEncoder is configured through `PasswordEncoder`
- A basic `SecurityFilterChain` is configured through `SecurityConfig`
- QueryDSL dependencies are present, but custom QueryDSL repository/query usage has not been confirmed yet

Current authentication/security stack status:

- Custom `HttpSession` login is currently used.
- Passwords are encoded with BCrypt during signup.
- Login verifies passwords through `PasswordEncoder.matches(...)`.
- Basic Spring Security filter configuration has been introduced through `SecurityConfig`.
- `SecurityConfig` currently disables Spring Security's default `csrf`, `formLogin`, `httpBasic`, and `logout` behavior.
- `SecurityConfig` currently permits static resources, Swagger, signup, login, product list, and all remaining requests.
- Full Spring Security authentication/authorization has not been introduced yet.
- Spring Security request-level authorization such as `.authenticated()` and `.hasRole("ADMIN")` is not active yet.
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

## 4. Current Confirmed Repository State

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
- `global.security`

Current implemented features include:

- Member signup
- BCrypt password encoding for signup
- BCrypt password verification for login
- Basic Spring Security filter configuration through `SecurityConfig`
- Spring Security default form login disabled
- Spring Security HTTP Basic authentication disabled
- Spring Security logout handling disabled
- CSRF disabled for the current REST API learning/testing stage
- Current custom `HttpSession` login flow preserved
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
- `SecurityConfig` exists as a bridge step before full Spring Security authentication/authorization migration.

---

## 5. Current Domain Understanding

### Member

A member represents a user/customer-like entity.

Known or expected fields:

- number
- name
- email
- password
- role

Current member-related features include:

- Signup through `POST /members`
- Current-user lookup through `GET /members/me`
- Current-user update through `PUT /members/me`
- Current-user deletion through `DELETE /members/me`
- Current user's order lookup through `GET /members/me/orders`
- Admin member lookup/delete through `/admin/members`

Important future direction:

- Member should eventually become the Spring Security authentication subject.
- Email should remain the likely unique login identifier.
- Password is already added and encoded with BCrypt.
- Role already exists as `USER` and `ADMIN`.
- Later, `Member.role` should be converted into Spring Security authorities such as `ROLE_USER` and `ROLE_ADMIN`.

---

### Product

A product represents an item that can be ordered.

Known or expected fields:

- number
- name
- price
- stock

Current product behavior appears to include:

- Product list retrieval through `GET /products`
- Stock decrease
- Stock increase
- Stock validation when order quantity exceeds available stock

Important future direction:

- Add full product CRUD for admin use.
- Add product detail API.
- Add product search/filtering.
- Add product category.
- Add product image support later.
- Prevent direct arbitrary stock mutation from controllers.

---

### Orders

An order represents a purchase/order transaction.

Known or expected fields:

- number
- member
- orderDate
- status

Current order behavior includes:

- Order creation for the current logged-in member
- Owner-restricted order detail lookup
- Owner-restricted order cancellation
- Admin all-order lookup
- Admin unrestricted order detail lookup

Important future direction:

- Use clearer order status transition rules.
- Prevent invalid status changes.
- Return order items in order detail response.
- Keep total price calculation in response DTOs.
- Add payment-ready/payment-completed concepts later.

---

### OrderItem

An order item connects an order with a product.

Known or expected fields:

- order
- product
- unitPrice
- count

Current behavior includes:

- Create order item when an order is created
- Store ordered unit price at the time of order
- Restore product stock when an order is cancelled
- Calculate line total as `unitPrice * count`

Important future direction:

- Add bidirectional convenience methods only if needed.
- Avoid exposing entity graph directly in API responses.
- Use DTOs for order item response.
- Be careful with lazy loading and N+1 queries.

---

## 6. Current Authentication and Authorization Model

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

### Current Spring Security filter configuration

A basic `SecurityConfig` now exists under:

```text
kr.co.prac.global.security.SecurityConfig
```

Current role:

- Prevent Spring Security default behavior from blocking the current custom session login flow.
- Disable default form login because the project uses `POST /members/login`.
- Disable HTTP Basic because the project does not use Basic Auth.
- Disable Spring Security logout because the project uses `POST /members/logout`.
- Disable CSRF for the current REST API testing stage.
- Permit static resources, Swagger, signup, login, and product list.
- Keep `.anyRequest().permitAll()` for now because actual login/authorization checks are still handled manually through `SessionUtil` and `AdminAuthService`.

Important caveat:

- This is not yet full Spring Security login.
- `SecurityContextHolder`, `Authentication`, `UserDetailsService`, and `GrantedAuthority` are not yet connected.
- Therefore `.authenticated()` and `.hasRole("ADMIN")` should not be enabled yet.
- `/admin/**` should later move to `.hasRole("ADMIN")` after Spring Security authentication is properly connected.

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

## 7. Current Main API Surface

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

## 8. Current Order Model and Pricing Decision

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

## 9. Current Exception Model

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

## 10. Current Architecture

Current architecture follows layered architecture:

```text
Controller
↓
Service
↓
Repository
↓
Entity / Database
```

Current package direction includes:

```text
kr.co.prac
├── admin
│   ├── controller
│   ├── exception
│   └── service
├── global
│   ├── config
│   ├── entity
│   ├── exception
│   ├── security
│   └── session
├── login
│   ├── controller
│   ├── dto
│   ├── exception
│   └── service
├── member
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── exception
│   ├── repository
│   └── service
├── orders
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── exception
│   ├── repository
│   └── service
├── product
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── exception
│   ├── repository
│   └── service
└── PracApplication.java
```

Recommendation:

- Feature-based packages are preferred as the project grows.
- `global.config` is suitable for general configuration such as `PasswordConfig`.
- `global.security` is suitable for security-specific configuration such as `SecurityConfig`.

---

## 11. Current Controller Responsibilities

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

### `SecurityConfig`

Purpose:

- Learning-stage Spring Security filter configuration.

Current responsibilities:

- Disable Spring Security defaults that interfere with the current custom session flow.
- Permit basic public endpoints and static resources.
- Keep request-level Spring Security authorization disabled until full Spring Security authentication is implemented.

Should not include yet:

- `.authenticated()` for protected endpoints.
- `.hasRole("ADMIN")` for admin endpoints.
- Custom login processing through Spring Security.
- JWT configuration.

---

## 12. Current Known Limitations

### Authentication/security

- Plain custom `HttpSession` login is still used.
- Password encoding has been introduced with BCrypt.
- Basic Spring Security filter configuration has been introduced.
- Spring Security authentication/authorization is not yet introduced.
- Admin authorization is role-based through `AdminAuthService`, but not yet through Spring Security request authorization rules.
- `SecurityConfig` currently uses `.anyRequest().permitAll()` intentionally to avoid conflicting with the custom session login flow.
- `.authenticated()` and `.hasRole("ADMIN")` should only be enabled after Spring Security authentication is connected.
- No production CSRF strategy yet.
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
- `AdminAuthService` currently depends on member repository/domain lookup. Later, admin authorization should move into Spring Security authorities.

### Testing

- Tests are intentionally postponed.
- The user wants to learn tests later in one focused testing block.
- Do not push test-heavy recommendations until the user asks for the testing block.

---

## 13. Immediate Refactoring Recommendations

Before adding many large features, prioritize these improvements.

### 1. Separate environment configuration

Current local database settings should not be treated as production settings.

Recommended files:

```text
application.yml
application-local.yml
application-test.yml
application-prod.yml
```

Local example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/prac?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### 2. Stop relying on `ddl-auto=create` for long-term development

Use `create` only while learning or resetting local data.

Later direction:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Then use Flyway or Liquibase for schema migration.

### 3. Continue global exception handling cleanup

The project already has a global exception model. Continue refining:

```text
global/exception/GlobalExceptionHandler.java
global/exception/ErrorResponse.java
global/exception/ErrorCode.java
```

### 4. Standardize API responses

At minimum, decide whether APIs return:

```json
{
  "data": {},
  "message": "success"
}
```

or return raw DTOs directly.

For a beginner project, raw DTOs are acceptable. For portfolio/production direction, a consistent response format is better.

### 5. Prepare for Spring Security session login migration

The next authentication migration should connect:

```text
Member
CustomUserDetails
CustomUserDetailsService
SecurityContextHolder
Authentication
GrantedAuthority
SecurityFilterChain
```

---

## 14. Recommended Next Steps

Current recommended next steps:

1. Commit the basic `SecurityConfig` setup.
2. Manually verify that Swagger and public APIs are not blocked by Spring Security defaults.
   - Swagger UI loads.
   - `GET /products` succeeds without login.
   - `POST /members` succeeds without login.
   - `POST /members/login` succeeds without login.
3. Manually verify signup/login/logout with BCrypt password flow.
   - Signup creates encoded password.
   - Login succeeds with correct raw password.
   - Login fails with incorrect password.
   - Logout invalidates session.
   - `GET /members/me` fails after logout.
4. Manually verify role-based admin authorization through the existing manual helper.
   - Login as `Role.ADMIN` member.
   - Call `GET /admin/members`; expect success.
   - Call `GET /admin/orders`; expect success.
   - Call `GET /admin/orders/{orderId}`; expect success.
   - Login as `Role.USER` member.
   - Confirm admin APIs return `403 Forbidden`.
5. Manually verify owner-restricted order APIs.
   - Own order detail succeeds.
   - Another member's order detail returns `403 Forbidden`.
   - Own order cancel succeeds.
   - Another member's order cancel returns `403 Forbidden`.
6. Manually verify `DELETE /members/me`.
7. Decide how admin role is assigned or seeded.
8. Clean remaining plain string responses such as `"성공"`.
9. Start Spring Security session login migration.
10. After Spring Security session login is stable, consider Redis Session.
11. Learn and add tests later in one dedicated testing block.

---

## 15. Recommended Authentication Roadmap

Recommended order:

```text
1. Current custom HttpSession login verification
2. Basic SecurityFilterChain configuration
3. Spring Security session login
4. Spring Security role-based authorization
5. Spring Session + Redis
6. JWT comparison later
```

Current status:

```text
Step 1: mostly implemented
Step 2: being added in the current commit
Step 3: not started
Step 4: not started
Step 5: not started
Step 6: later
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
    -> Basic SecurityFilterChain configuration
    -> Spring Security session login
    -> Spring Security role authorization
    -> Spring Session + Redis
    -> JWT comparison later
```

---

## 16. Testing Roadmap

Tests are intentionally not the immediate focus, but recommended future tests include:

### Service tests

- Member signup success
- Duplicate email failure
- Login success
- Invalid password failure
- Product stock decrease
- Product stock shortage failure
- Order creation success
- Order cancellation success
- Duplicate cancellation failure
- Admin authorization success/failure

### Repository tests

- Find member by email
- Find orders by member
- Find order items by order

### Controller tests

- Member API validation
- Login validation
- Order creation validation
- Product list API
- Error response format
- Admin API forbidden response
- SecurityConfig public endpoint accessibility

### Integration tests

- Full signup/login/logout flow
- Full order creation flow
- Full order cancellation flow
- Full admin/user authorization flow
- Spring Security session login flow after migration

Recommended tools:

- JUnit 5
- AssertJ
- Spring Boot Test
- MockMvc
- Testcontainers later

---

## 17. JPA Learning Priorities

For this project, the most important JPA concepts are:

1. Entity identity
2. `@ManyToOne`
3. `@OneToMany`
4. Lazy loading
5. Owning side
6. Cascade
7. Orphan removal
8. Dirty checking
9. Transaction boundary
10. N+1 query problem
11. Fetch join
12. JPQL
13. QueryDSL

The assistant should explain JPA concepts using this project's domain whenever possible.

Example:

- Member has many orders.
- Order has many order items.
- Order item references product.
- Product stock changes through domain methods.

---

## 18. Production-Minded Standards

As this project moves toward a commercializable web service, apply these standards gradually.

### Security

- Do not store raw passwords.
- Do not expose internal entity fields.
- Do not trust client-provided user ID after login.
- Use authenticated principal for user-specific operations after Spring Security migration.
- Separate user and admin permissions.
- Do not enable `.authenticated()` or `.hasRole("ADMIN")` until Spring Security authentication is connected.
- Add a real CSRF strategy before browser-based production session login.
- Harden cookies/session settings before deployment.

### Database

- Do not use `ddl-auto=create` in production.
- Use migration tools.
- Add indexes where needed.
- Use unique constraints for email.
- Think about transaction isolation for stock changes.

### API

- Use consistent endpoint naming.
- Use proper HTTP status codes.
- Add validation.
- Add error response body.
- Add Swagger documentation.
- Add pagination for list APIs.

### Code

- Keep controllers thin.
- Keep business rules in service or domain methods.
- Avoid entity exposure.
- Use DTOs.
- Keep package names consistent.
- Use clear method names.
- Add tests when fixing bugs or adding features, after the user starts the testing block.

---

## 19. Recommended Commit Messages by Future Step

Basic SecurityConfig:

```bash
git commit -m "feat: add basic security filter configuration"
```

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

## 20. What To Ask or Check Later

When more precision is needed, check or ask for:

- Current entity source code
- Current controller source code
- Current service source code
- Current repository source code
- Current DTOs
- Current test files
- Current API behavior
- Current database schema
- Whether the project uses only custom sessions or has started Spring Security migration
- Whether the project has a frontend direction
- Whether the project will be deployed to AWS, NCP, or another platform

This document gives long-term context, but exact code review should still be based on the current source code.

---

## 21. Assistant Guidance for Future Conversations

When continuing this project:

- Speak Korean unless the user asks otherwise.
- Review diffs directly and say whether they are commit-ready.
- Prioritize correctness and learning over over-engineering.
- Do not recommend JWT before the session/Spring Security basics are clear.
- Recommended authentication order is custom session verification, basic `SecurityFilterChain`, Spring Security session login, Redis Session, JWT later.
- Do not add tests unless the user explicitly starts the testing block.
- Do not focus on whitespace-only issues unless requested.
- Do not flag wildcard imports as an issue unless they cause problems.
- For API design, separate normal-user behavior from admin behavior.
- For authorization, note that admin authorization is currently `Role.ADMIN` based, but not yet Spring Security based.
- For GitHub checks, verify current repo state before generating new docs.
- Use exact filenames:
  - `PROJECT_CONTEXT.md`
  - `PROJECT_LOG.md`
- Do not add `_updated` to generated file names.
