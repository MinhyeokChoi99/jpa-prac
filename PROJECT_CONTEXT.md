# Project Context: jpa-prac

_Last updated: 2026-06-29 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `order_item`, Spring Security session-based `login`, `admin`, and planned `cart`
- User language: Korean
- Preferred assistant style: Korean explanation, Java/Spring examples, direct code review, incremental production-minded guidance

The project is being developed step by step to study Spring MVC, JPA relationships, DTO design, business rules, exception handling, authentication, authorization, session management, testing, and future production-minded backend architecture.

This project is not just a simple CRUD exercise. It is intended to grow gradually into a portfolio-level and production-minded Spring web application.

The user wants to study Java and Spring by building, reviewing, refactoring, testing, and extending this project step by step.

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
- How to understand and write tests, especially because the user has limited test-writing experience

The user wants feedback on:

- Outdated coding style
- Poor performance patterns
- Better Spring/JPA practices
- Architecture improvements
- Feature roadmap
- Portfolio-readiness
- Production-readiness
- Test strategy and test code quality

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
- Spring Security
- Spring Security Crypto is used for password encoding
- BCryptPasswordEncoder is configured through `PasswordEncoder`
- Static frontend served from Spring Boot under `src/main/resources/static`
- QueryDSL dependencies are present, but custom QueryDSL repository/query usage has not been confirmed yet

Current authentication/security stack status:

- Spring Security session login is now the current direction.
- Passwords are encoded with BCrypt during signup.
- Login verifies passwords through Spring Security authentication flow and/or password encoder-backed logic.
- `CustomUserDetails` and `CustomUserDetailsService` are part of the security model.
- `SecurityContextHolder` and `HttpSessionSecurityContextRepository` are used for session-backed authentication.
- `SecurityConfig` disables default form login, HTTP Basic, default logout, and CSRF for the current learning/testing stage.
- Public endpoints and static resources are explicitly permitted.
- `/admin/**` should be protected as ADMIN-only.
- Redis Session has not been introduced yet.
- JWT has not been introduced yet.

Potential future stack:

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
- Object storage for product image upload later

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
- `global.config`
- `global.security`
- static frontend resources

Current implemented features include:

- Member signup
- BCrypt password encoding for signup
- Login/logout
- Spring Security session-based authentication direction
- `CustomUserDetails`
- `CustomUserDetailsService`
- Current-member lookup, update, and delete APIs
- Product public list API
- Product status management with `ProductStatus`
- Admin product CRUD APIs
- Admin product hide/show/delete APIs
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
- Static frontend page that calls backend APIs
- Frontend login/register screen
- USER dashboard flow
- ADMIN dashboard flow

Current important design state:

- General member APIs are `/members/me` centered.
- Admin member APIs are separated into `AdminMemberController` under `/admin/members`.
- Admin order APIs are separated into `AdminOrderController` under `/admin/orders`.
- Admin product APIs are separated under `/admin/products`.
- Public `GET /products` returns only `ACTIVE` products.
- Admin product list returns all products, including `ACTIVE`, `HIDDEN`, and `DELETED`.
- Product deletion is soft delete through `ProductStatus.DELETED`.
- Static frontend exists only to call and demonstrate backend APIs.
- Frontend must not compensate for backend business logic defects.

---

## 5. Current Domain Understanding

### Member

A member represents a user/customer-like entity and is also the authentication subject.

Known or expected fields:

- number
- name
- email
- password
- role

Current member-related features include:

- Signup through `POST /members`
- Login through `POST /members/login`
- Logout through `POST /members/logout`
- Current-user lookup through `GET /members/me`
- Current-user update through `PUT /members/me`
- Current-user deletion through `DELETE /members/me`
- Current user's order lookup through `GET /members/me/orders`
- Admin member lookup/delete through `/admin/members`

Important future direction:

- Email should remain the likely unique login identifier.
- Password is already added and encoded with BCrypt.
- Role exists as `USER` and `ADMIN`.
- User-specific APIs should use the authenticated principal/security context, not client-provided member IDs.
- Admin authorization should remain backend-enforced.

---

### Product

A product represents an item that can be ordered.

Known or expected fields:

- number
- name
- price
- stock
- description
- productStatus

Current product behavior includes:

- Public product list retrieval through `GET /products`
- Admin product list retrieval through `GET /admin/products`
- Admin product detail retrieval
- Admin product creation
- Admin product update
- Product hide/show
- Product soft delete
- Stock decrease
- Stock increase
- Stock validation when order quantity exceeds available stock

Current product statuses:

```java
public enum ProductStatus {
    ACTIVE,
    HIDDEN,
    DELETED
}
```

Important future direction:

- Add product image support starting with an `imageUrl` field.
- Add product search/filtering.
- Add product pagination.
- Add product category later if useful.
- Prevent direct arbitrary stock mutation from controllers.
- Prevent ordering hidden or deleted products at the backend level.

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
- Current-member order list lookup
- Admin all-order lookup
- Admin unrestricted order detail lookup
- Product stock decrease on order creation
- Product stock restoration on order cancellation
- Duplicate cancellation prevention

Important future direction:

- Use clearer order status transition rules.
- Prevent invalid status changes.
- Keep order items in order detail response.
- Keep total price calculation in response DTOs.
- Add payment-ready/payment-completed concepts later.
- Add cart-to-order flow after test coverage is improved.

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
- Keep total price calculation testable.

---

### Cart

Cart is the selected next commerce feature after the testing phase starts.

Expected future domain:

- `Cart`
- `CartItem`

Expected behavior:

- Each member has a cart.
- Users can add products to cart.
- Users can update cart item quantity.
- Users can remove cart items.
- Users can create an order from cart items.
- Cart items should be cleared after successful order creation.

Important backend rule:

- Cart must not replace order validation.
- Even if an item exists in the cart, stock and product status must be checked again when creating an order.
- The authenticated user must own the cart being modified or ordered.

---

## 6. Current Authentication and Authorization Model

The project now uses Spring Security session-based authentication as the main direction.

### Current login model

- Login endpoint accepts email/password.
- Login uses Spring Security authentication components and session-backed security context.
- Password verification should use `PasswordEncoder.matches(...)`.
- Login response returns member-facing user information such as name, email, and role.
- Logout clears the security context and invalidates the session/cookie as needed.

### Current password model

- Signup encodes the request password through `PasswordEncoder.encode(...)`.
- BCrypt is used through `BCryptPasswordEncoder`.
- Login compares the raw request password with the encoded stored password through the security/password encoder flow.
- Plain-text password comparison should not be considered valid.

### Current Spring Security filter configuration

A `SecurityConfig` exists under:

```text
kr.co.prac.global.security.SecurityConfig
```

Current role:

- Disable default form login because the project uses `POST /members/login`.
- Disable HTTP Basic because the project does not use Basic Auth.
- Disable Spring Security default logout because the project uses `POST /members/logout`.
- Disable CSRF for the current REST API learning/testing stage.
- Permit static resources, Swagger, signup, login, logout, and public product list.
- Protect `/admin/**` as ADMIN-only.
- Require authentication for other protected APIs.

Important caveat:

- Frontend role checks are only for UI display.
- Backend Spring Security authorization must remain the source of truth.
- Before production browser-session deployment, CSRF and cookie/session hardening should be revisited.

### Current admin model

Admin authorization is role-based.

Current rule:

```text
Member.role == Role.ADMIN -> admin
Member.role != Role.ADMIN -> not admin
```

Implementation direction:

- Admin APIs are under `/admin/**`.
- `/admin/**` should be protected by Spring Security role authorization.
- Normal users should not access admin APIs even if they manually call the URL.

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

- `POST /members/login`: authenticates email/password and creates a session-backed login state
- `POST /members/logout`: clears login state and invalidates session/cookie as needed

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

### Product APIs

Product browsing is public.

```text
GET /products
```

Current meaning:

- `GET /products`: returns only `ACTIVE` products.

### Admin Product APIs

Current `AdminProductController`:

```text
GET    /admin/products
GET    /admin/products/{productNumber}
POST   /admin/products
PUT    /admin/products/{productNumber}
DELETE /admin/products/{productNumber}
PATCH  /admin/products/{productNumber}/hide
PATCH  /admin/products/{productNumber}/show
```

Current meaning:

- `GET /admin/products`: admin all-product lookup, including all statuses
- `GET /admin/products/{productNumber}`: admin product detail lookup
- `POST /admin/products`: admin product creation
- `PUT /admin/products/{productNumber}`: admin product update
- `DELETE /admin/products/{productNumber}`: soft delete through `ProductStatus.DELETED`
- `PATCH /admin/products/{productNumber}/hide`: set product to `HIDDEN`
- `PATCH /admin/products/{productNumber}/show`: set product to `ACTIVE`

### Admin Member APIs

Current `AdminMemberController`:

```text
GET    /admin/members
GET    /admin/members/{number}
DELETE /admin/members/{number}
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

Current meaning:

- `GET /admin/orders`: admin all-order lookup
- `GET /admin/orders/{orderId}`: admin unrestricted order detail lookup

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

Current role/authorization caveat:

- `Member.role` exists and `MemberResponse` exposes `role`.
- Admin authorization is role-based.
- Spring Security should remain the final backend authorization layer.

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
│   └── security
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
- `global.config` is suitable for general configuration.
- `global.security` is suitable for security-specific configuration.
- A future `cart` package can follow the same feature-based structure.

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

- Session login/logout.

Current responsibilities:

- Login through email/password
- Create session-backed login state
- Logout by clearing session/security context

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

### `AdminProductController`

Purpose:

- Admin product management.

Current responsibilities:

- Product list including all statuses
- Product detail
- Product create
- Product update
- Product soft delete
- Product hide/show

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

- Spring Security session authentication and authorization configuration.

Current responsibilities:

- Disable Spring Security defaults not used by this project.
- Permit public endpoints and static resources.
- Protect admin APIs.
- Protect authenticated user APIs.
- Use session-backed security context.

---

## 12. Current Known Limitations

### Authentication/security

- CSRF is disabled for the current learning/testing stage.
- Cookie/session production hardening is not yet completed.
- Redis Session is not yet introduced.
- JWT is not yet introduced.
- Security integration tests should be added.

### API design

- Some return values may still use plain strings such as `"성공"`.
- A consistent success response format has not been fully standardized.
- Pagination has not been added to list APIs.
- Product ordering should ensure backend validation for product status.

### Code quality

- Some controllers may still use wildcard imports depending on IDE behavior. This is accepted for now and should not be treated as a blocker.
- Minor formatting issues are not a priority unless they affect readability or correctness.
- Some DTO validation can be strengthened, such as adding `@NotNull` to required `Integer` fields.

### Testing

- Testing is now the selected next learning and implementation phase.
- The user has limited prior experience writing test code.
- Tests should be added incrementally with explanation.
- Testing should include domain unit tests, service unit tests with Mockito, repository tests with `@DataJpaTest`, and controller/security integration tests with MockMvc.

### Frontend/backend boundary

- Frontend is a UI layer for API demonstration and browser testing.
- Frontend must not hide, patch, or compensate for backend defects.
- Frontend role checks are display-only.
- Backend remains responsible for authentication, authorization, validation, stock changes, order ownership, product status transitions, and persistence.

---

## 13. Immediate Refactoring Recommendations

Before adding many large features, prioritize these improvements.

### 1. Strengthen backend tests

Testing is now the main immediate improvement area.

Recommended testing types:

```text
Domain unit tests
Service unit tests with Mockito
Repository tests with @DataJpaTest
Controller/security integration tests with MockMvc
```

### 2. Add missing validation annotations

For required nullable wrapper fields such as `Integer price` and `Integer stock`, prefer both:

```java
@NotNull
@Min(0)
private Integer price;
```

Reason:

- `@Min` alone does not reject `null`.
- Required numeric fields should explicitly reject `null`.

### 3. Standardize API responses

At minimum, decide whether APIs return:

```json
{
  "data": {},
  "message": "success"
}
```

or return raw DTOs directly.

For a beginner project, raw DTOs are acceptable. For portfolio/production direction, a consistent response format is better.

### 4. Continue exception handling cleanup

The project already has a global exception model. Continue refining:

```text
global/exception/GlobalExceptionHandler.java
global/exception/ErrorResponse.java
global/exception/ErrorCode.java
```

### 5. Keep frontend/backend responsibility separation

Frontend should not compensate for backend business logic mistakes.

Allowed frontend behavior:

- render UI
- call APIs
- show errors
- show loading/success states
- basic UX validation
- display menus based on role

Disallowed frontend behavior:

- pretending backend operations succeeded
- implementing business state transitions only in JavaScript
- using frontend checks as the only security layer
- masking backend validation defects

---

## 14. Recommended Next Steps

Current recommended next steps:

1. Learn and add backend tests.
   - Learn JUnit 5 and AssertJ basics.
   - Write pure domain unit tests first.
   - Write service unit tests with Mockito.
   - Write repository tests with `@DataJpaTest`.
   - Write controller/security integration tests with MockMvc.
2. Add cart feature after initial test coverage is started.
   - Add `Cart` and `CartItem`.
   - Add cart item add/update/remove APIs.
   - Add order-from-cart API.
   - Reuse backend order validation.
3. Add product image support starting with `imageUrl`.
   - Add `imageUrl` to `Product`.
   - Add `imageUrl` to create/update request DTOs.
   - Add `imageUrl` to response DTO.
   - Display product images in the static frontend.
   - Consider file upload only later.

---

## 15. Recommended Authentication Roadmap

Recommended order:

```text
1. Spring Security session login
2. Spring Security role-based authorization
3. Security/controller integration tests
4. Spring Session + Redis
5. JWT comparison later
```

Current status:

```text
Step 1: implemented or in active use
Step 2: implemented for admin direction and should be verified with tests
Step 3: selected as part of the next testing phase
Step 4: later
Step 5: later
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
Spring Security session login
    -> Spring Security role authorization
    -> Security integration tests
    -> Spring Session + Redis
    -> JWT comparison later
```

---

## 16. Testing Roadmap

Testing is now the immediate focused learning block.

The user has limited test-writing experience, so testing guidance should explain both how and why.

### Testing learning goals

Required topics:

- JUnit 5
- AssertJ
- Mockito
- Spring Test
- MockMvc
- `@DataJpaTest`
- `@SpringBootTest`
- `@WebMvcTest`
- `@Transactional`
- test fixture/data setup
- exception testing
- security testing
- unit test vs repository test vs integration test

Important principle:

Do not only copy test code. For every test, understand:

- what behavior is being tested
- what data is prepared
- what action is executed
- what result is asserted
- what test type it is

### Domain unit tests

Domain unit tests usually do not require Mockito.

Recommended targets:

- `Product`
  - stock increase
  - stock decrease
  - insufficient stock failure
  - status change to `ACTIVE`
  - status change to `HIDDEN`
  - status change to `DELETED`
- `Orders`
  - order creation state
  - order cancellation state
  - duplicate cancellation prevention if handled in domain
- `OrderItem`
  - total price calculation

### Service unit tests with Mockito

Service unit tests should use Mockito when the class under test depends on repositories, encoders, or other services.

Recommended targets:

- `MemberService`
  - signup success
  - duplicate email signup failure
  - password encoding called correctly
- `LoginService`
  - login success
  - login failure
- `AdminProductService`
  - product create
  - product update
  - product hide/show/delete
- `OrderService`
  - order creation
  - stock decrease request
  - insufficient stock failure
  - order cancellation
  - duplicate cancellation failure
  - ownership validation failure

Mockito concepts to learn:

- `@ExtendWith(MockitoExtension.class)`
- `@Mock`
- `@InjectMocks`
- `given(...).willReturn(...)`
- `verify(...)`
- `verify(..., never())`
- `any()`
- `eq()`

Important rule:

Do not use Mockito just because a test is called a unit test.

Use Mockito only when dependencies should be isolated.

### Repository tests

Repository tests should be included.

Repository tests are usually not Mockito-based unit tests.

They should usually be written as JPA slice tests using `@DataJpaTest`.

Recommended targets:

- `MemberRepository`
  - `findByEmail`
  - `existsByEmail`
- `ProductRepository`
  - `findAllByProductStatus(ProductStatus.ACTIVE)`
  - confirm public product query can exclude `HIDDEN` and `DELETED` products
- `OrdersRepository`
  - member-specific order lookup
  - order lookup by member number if used
- `OrderItemRepository`
  - order-item lookup by order if custom methods exist

Important rule:

Do not mock the repository when testing the repository itself.

Use a real test database through `@DataJpaTest`.

Do not over-test Spring Data JPA built-in methods like `save`, `findById`, or `findAll` unless the test verifies entity mapping or relationship behavior.

### Controller/security integration tests

Recommended tools:

- MockMvc
- Spring Security test utilities
- `@SpringBootTest`
- `@WebMvcTest` when appropriate

Recommended targets:

- signup API
- login API
- logout API
- current user API
- public product list API
- user order creation API
- user order detail API
- user order cancellation API
- admin product create/update/hide/show/delete APIs
- admin order list/detail APIs
- admin member list/delete APIs

Security tests should verify:

- unauthenticated users cannot access protected APIs
- normal USER cannot access `/admin/**`
- ADMIN can access `/admin/**`
- logged-in USER can access their own APIs
- user-specific APIs do not trust client-provided member IDs

### Recommended test implementation order

Recommended order:

```text
1. Learn JUnit 5 and AssertJ syntax.
2. Write pure domain unit tests for Product.
3. Write order stock/cancel-related domain tests.
4. Write service unit tests with Mockito.
5. Write repository tests with @DataJpaTest.
6. Write controller/security integration tests with MockMvc.
7. Add broader API flow integration tests if needed.
```

Do not start with the most complex security integration tests first.

Start with small tests so the test structure is clear.

Recommended tools:

- JUnit 5
- AssertJ
- Mockito
- Spring Boot Test
- MockMvc
- `@DataJpaTest`
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
14. Repository testing with `@DataJpaTest`

The assistant should explain JPA concepts using this project's domain whenever possible.

Example:

- Member has many orders.
- Order has many order items.
- Order item references product.
- Product stock changes through domain methods.
- Cart will belong to member and contain cart items.
- Cart item will reference product.

---

## 18. Production-Minded Standards

As this project moves toward a commercializable web service, apply these standards gradually.

### Security

- Do not store raw passwords.
- Do not expose internal entity fields.
- Do not trust client-provided user ID after login.
- Use authenticated principal/security context for user-specific operations.
- Separate user and admin permissions.
- Keep backend as the source of truth for authorization.
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
- Keep response DTOs stable for frontend use.

### Code

- Keep controllers thin.
- Keep business rules in service or domain methods.
- Avoid entity exposure.
- Use DTOs.
- Keep package names consistent.
- Use clear method names.
- Add tests when fixing bugs or adding features.
- Avoid frontend workarounds for backend defects.

### Testing

- Add tests before large new features where possible.
- Use pure domain unit tests for entity/domain rules.
- Use Mockito for service unit tests with dependencies.
- Use `@DataJpaTest` for repository tests.
- Use MockMvc/Spring integration tests for API and security behavior.
- Test both success paths and failure paths.
- Treat authorization tests as critical.

---

## 19. Recommended Commit Messages by Future Step

Testing block:

```bash
git commit -m "test: add domain and service tests"
```

Repository tests:

```bash
git commit -m "test: add repository tests"
```

Security/controller integration tests:

```bash
git commit -m "test: add security integration tests"
```

Cart feature:

```bash
git commit -m "feat: add cart feature"
```

Product image URL:

```bash
git commit -m "feat: add product image url"
```

Documentation update:

```bash
git commit -m "docs: update project context and log"
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
- Current security configuration
- Current static frontend files
- Whether the user wants domain unit tests, service unit tests, repository tests, or integration tests first
- Whether the user wants to start cart after test coverage
- Whether product image should remain `imageUrl` or eventually support upload
- Whether the project will be deployed to AWS, NCP, or another platform

This document gives long-term context, but exact code review should still be based on the current source code.

---

## 21. Assistant Guidance for Future Conversations

When continuing this project:

- Speak Korean unless the user asks otherwise.
- Review diffs directly and say whether they are commit-ready.
- Prioritize correctness and learning over over-engineering.
- Do not recommend JWT before the session/Spring Security basics are clear.
- Current recommended direction is testing first, then cart, then product image URL.
- For testing, explain the test purpose, setup, action, assertion, and test type.
- Do not add frontend logic that compensates for backend defects.
- Do not focus on whitespace-only issues unless requested.
- Do not flag wildcard imports as an issue unless they cause problems.
- For API design, separate normal-user behavior from admin behavior.
- For authorization, backend Spring Security must remain the source of truth.
- For GitHub checks, verify current repo state before generating new docs.
- Use exact filenames:
  - `PROJECT_CONTEXT.md`
  - `PROJECT_LOG.md`
- Do not add `_updated` to generated file names.
