# Project Context: jpa-prac

_Last updated: 2026-06-29 (Asia/Seoul)_

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term purpose: Grow into a portfolio-level, production-minded Spring web application
- Main domains: `member`, `product`, `product_image` planned, `orders`, `order_item`, `cartitem`, Spring Security session-based `login`, and `admin`
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

- Spring Security session login is the current authentication direction.
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
- `cartitem`
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
- Product status validation during order creation
- Cart item add/view/increase/decrease/delete APIs
- Cart item ownership validation using authenticated member ID
- Cart item unique member-product behavior
- Order-from-cart flow through `POST /orders/from-cart`
- Empty cart validation through `EmptyCartItemException`
- Cart item cleanup after successful order-from-cart
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
- Direct order creation and cart order creation should both validate product status and stock at the backend layer.
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

Important direction:

- Email should remain the likely unique login identifier.
- Password is encoded with BCrypt.
- Role exists as `USER` and `ADMIN`.
- User-specific APIs should use the authenticated principal/security context, not client-provided member IDs.
- Admin authorization should remain backend-enforced.

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
- Backend product status validation when creating orders

Current product statuses:

```java
public enum ProductStatus {
    ACTIVE,
    HIDDEN,
    DELETED
}
```

Important next image direction:

- Do not add only `Product.imageUrl` because the project may support multiple product images.
- Add a separate `ProductImage` entity/table.
- Model the relationship as `Product 1 : N ProductImage`.
- A product may have only one representative image, or multiple images with exactly one representative image.
- Start with URL storage only: `ProductImage.imageUrl`.
- Do not start with `MultipartFile` upload, local file storage, S3, or object storage yet.
- Later file upload should store the resulting accessible URL into `ProductImage.imageUrl`.

### ProductImage planned

ProductImage is the selected next feature.

Expected fields:

- number
- product
- imageUrl
- thumbnail or representative flag
- sortOrder

Expected behavior:

- An admin can add an image URL to a product.
- A product can have one or many images.
- One image can be marked as representative/thumbnail.
- Product list APIs may expose only the representative image URL.
- Product detail APIs may expose the full image list.
- If a new image is marked as representative, existing representative images for the same product should be unmarked.

Recommended entity direction:

```text
Product 1 ─ N ProductImage
```

Initial API direction:

```text
POST   /admin/products/{productNumber}/images
GET    /admin/products/{productNumber}/images
PATCH  /admin/products/{productNumber}/images/{imageId}/thumbnail
PUT    /admin/products/{productNumber}/images/{imageId}
DELETE /admin/products/{productNumber}/images/{imageId}
```

### Orders

An order represents a purchase/order transaction.

Known or expected fields:

- number
- member
- orderDate
- status

Current order behavior includes:

- Direct order creation for the current logged-in member through `POST /orders`
- Cart-based order creation through `POST /orders/from-cart`
- Owner-restricted order detail lookup
- Owner-restricted order cancellation
- Current-member order list lookup
- Admin all-order lookup
- Admin unrestricted order detail lookup
- Product stock decrease on order creation
- Product stock restoration on order cancellation
- Duplicate cancellation prevention
- ProductStatus.ACTIVE validation during direct order and cart order
- Cart item deletion after successful order-from-cart

Important future direction:

- Keep order status transition rules clear.
- Prevent invalid status changes.
- Keep order items in order detail response.
- Keep total price calculation in response DTOs.
- Add payment-ready/payment-completed concepts later.
- The current order creation logic has been lightly refactored, but deeper cleanup can be deferred.

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

Important direction:

- Add bidirectional convenience methods only if needed.
- Avoid exposing entity graph directly in API responses.
- Use DTOs for order item response.
- Be careful with lazy loading and N+1 queries.
- Keep total price calculation testable.

### CartItem

CartItem is now implemented as the current cart feature.

Current behavior:

- Users can add products to cart through `POST /cart/items`.
- Users can view their cart through `GET /cart/items`.
- Users can increase/decrease cart item count.
- If count is 1 and decrease is requested, the cart item is deleted.
- Users can delete cart items.
- Cart item APIs use authenticated principal memberId, not request-provided memberId.
- CartItem has `ManyToOne` relationships to Member and Product.
- CartItem has a unique constraint on member/product so the same product for the same member is represented as one row.
- Cart items are not stock reservations.
- Product stock is decreased only when an order is created.

Important backend rules:

- Cart must not replace order validation.
- Even if an item exists in the cart, stock and product status must be checked again when creating an order.
- The authenticated user must own the cart item being modified or ordered.
- Order success should delete the ordered cart items.
- Order failure should not delete cart items because the transaction should roll back.

---

## 6. Current Authentication and Authorization Model

The project uses Spring Security session-based authentication as the main direction.

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

```text
POST   /members
PUT    /members/me
GET    /members/me
GET    /members/me/orders
DELETE /members/me
```

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

### Order/User APIs

```text
GET  /orders/{orderId}
POST /orders
POST /orders/from-cart
POST /orders/{orderId}/cancel
```

Current meaning:

- `GET /orders/{orderId}`: fetch one order detail, but only if it belongs to the logged-in member.
- `POST /orders`: create a direct order for the logged-in member; request body does not include `memberId`.
- `POST /orders/from-cart`: create an order from the logged-in member's cart items and delete cart items after successful order creation.
- `POST /orders/{orderId}/cancel`: cancel an order, but only if it belongs to the logged-in member.

### CartItem APIs

```text
GET    /cart/items
POST   /cart/items
PATCH  /cart/items/{cartItemId}/increase
PATCH  /cart/items/{cartItemId}/decrease
DELETE /cart/items/{cartItemId}
```

Current meaning:

- `GET /cart/items`: fetch current logged-in user's cart items.
- `POST /cart/items`: add product to cart or increase existing cart item count.
- `PATCH /cart/items/{cartItemId}/increase`: increase cart item count.
- `PATCH /cart/items/{cartItemId}/decrease`: decrease cart item count or delete item when count is 1.
- `DELETE /cart/items/{cartItemId}`: remove a cart item.

### Product APIs

Product browsing is public.

```text
GET /products
```

Current meaning:

- `GET /products`: returns only `ACTIVE` products.

### Admin Product APIs

```text
GET    /admin/products
GET    /admin/products/{productNumber}
POST   /admin/products
PUT    /admin/products/{productNumber}
DELETE /admin/products/{productNumber}
PATCH  /admin/products/{productNumber}/hide
PATCH  /admin/products/{productNumber}/show
```

Planned admin product image APIs:

```text
POST   /admin/products/{productNumber}/images
GET    /admin/products/{productNumber}/images
PATCH  /admin/products/{productNumber}/images/{imageId}/thumbnail
PUT    /admin/products/{productNumber}/images/{imageId}
DELETE /admin/products/{productNumber}/images/{imageId}
```

### Admin Member APIs

```text
GET    /admin/members
GET    /admin/members/{number}
DELETE /admin/members/{number}
```

### Admin Order APIs

```text
GET /admin/orders
GET /admin/orders/{orderId}
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
  "status": "READY",
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
CARTITEM_NOT_FOUND          -> 404 Not Found
EMPTY_ITEM_ORDER            -> 400 Bad Request
EMPTY_CART_ITEM             -> 400 Bad Request
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
├── cartitem
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── exception
│   ├── repository
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
- Product image code can start under the `product` domain because it is tightly coupled to product management.

---

## 11. Current Known Limitations

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
- Product image APIs are not yet implemented.
- Product list/detail response design should be revisited after ProductImage is added.

### Code quality

- Some controllers may still use wildcard imports depending on IDE behavior. This is accepted for now and should not be treated as a blocker.
- Minor formatting issues are not a priority unless they affect readability or correctness.
- Some DTO validation can be strengthened, such as adding `@NotNull` to required `Integer` fields.
- Direct order and cart order share similar order creation behavior; current light refactoring is acceptable, but deeper refactoring can be considered later.
- `ProductStatus.ACTIVE` validation currently throws `ProductNotFoundException`; a more precise `NotAvailableProductException` can be introduced later.

### Testing

- Testing is ongoing and should remain incremental.
- The user has limited prior experience writing test code.
- Tests should include domain unit tests, service unit tests with Mockito, repository tests with `@DataJpaTest`, and controller/security integration tests with MockMvc.
- OrderService tests have been restored after accidental deletion.
- `orderFromCart`-specific tests should be added.

### Frontend/backend boundary

- Frontend is a UI layer for API demonstration and browser testing.
- Frontend must not hide, patch, or compensate for backend defects.
- Frontend role checks are display-only.
- Backend remains responsible for authentication, authorization, validation, stock changes, order ownership, product status transitions, and persistence.

---

## 12. Recommended Next Steps

Current recommended next steps:

1. Commit the current cart/order-from-cart/refactoring progress.
2. Start ProductImage entity implementation.
3. Add `ProductImageRepository`.
4. Add ProductImage request/response DTOs.
5. Add admin ProductImage service and controller APIs.
6. Update product list/detail responses to include representative image and/or image list.
7. Add focused tests for ProductImage behavior.
8. Later, add file upload that stores generated URLs into `ProductImage.imageUrl`.

ProductImage implementation order:

```text
1. ProductImage entity
2. ProductImageRepository
3. ProductImageRequest / ProductImageResponse
4. AdminProductImageService
5. AdminProductImageController
6. Product response DTO integration
7. Manual API testing
8. Service/repository tests
```

---

## 13. ProductImage Design Decision

The project should not add only `Product.imageUrl` because the requirements include both single representative image and multiple product images.

Chosen model:

```text
Product 1 : N ProductImage
```

Planned `ProductImage` fields:

```text
number
product
imageUrl
thumbnail or representative flag
sortOrder
createdAt / updatedAt through BaseTimeEntity
```

Rules:

- A product can have zero, one, or many images.
- If there is only one image, it can be marked as representative.
- If there are many images, at most one should be marked as representative.
- The representative image should be used in product list responses.
- Product detail responses can include all images ordered by `sortOrder`.
- When a new image is marked as representative, existing representative images for the same product should be unmarked.
- File upload is not the first step. The first step is storing image URLs.

Later upload flow:

```text
MultipartFile upload
-> store file locally or in object storage
-> generate public/access URL
-> save URL into ProductImage.imageUrl
```

---

## 14. Recommended Authentication Roadmap

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
Step 3: selected as part of the testing phase
Step 4: later
Step 5: later
```

---

## 15. Testing Roadmap

Testing should continue incrementally.

Recommended tools:

- JUnit 5
- AssertJ
- Mockito
- Spring Boot Test
- MockMvc
- `@DataJpaTest`
- Testcontainers later

Recommended near-term test targets:

- `OrderService.orderFromCart` success
- `OrderService.orderFromCart` empty cart failure
- inactive product cannot be ordered through direct order
- inactive product cannot be ordered through cart order
- cart items are deleted after successful order-from-cart
- ProductImage repository lookup by product
- ProductImage representative image logic

Important rule:

- Do not mock the repository when testing the repository itself.
- Use Mockito for service tests with dependencies.
- Use `@DataJpaTest` for repository tests.

---

## 16. JPA Learning Priorities

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

Examples:

- Member has many orders.
- Order has many order items.
- Order item references product.
- Product stock changes through domain methods.
- Cart item references member and product.
- Product will have many product images.
- ProductImage will be the owning side of the Product/ProductImage relationship.

---

## 17. Production-Minded Standards

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
- ProductImage may need an index on `product_id` and possibly `(product_id, sort_order)`.
- Representative product image uniqueness should be enforced by service logic initially; database-level constraints can be considered later.

### API

- Use consistent endpoint naming.
- Use proper HTTP status codes.
- Add validation.
- Add error response body.
- Add Swagger documentation.
- Add pagination for list APIs.
- Keep response DTOs stable for frontend use.
- Product list response should eventually expose representative image URL.
- Product detail response should eventually expose image list.

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

- Add tests before or during large new features where possible.
- Use pure domain unit tests for entity/domain rules.
- Use Mockito for service unit tests with dependencies.
- Use `@DataJpaTest` for repository tests.
- Use MockMvc/Spring integration tests for API and security behavior.
- Test both success paths and failure paths.
- Treat authorization tests as critical.

---

## 18. Recommended Commit Messages by Future Step

Current cart/order/refactor documentation commit:

```bash
git commit -m "docs: update project context and log"
```

ProductImage entity:

```bash
git commit -m "feat: add product image entity"
```

ProductImage API:

```bash
git commit -m "feat: add product image management APIs"
```

Product response update:

```bash
git commit -m "feat: include product images in responses"
```

Tests:

```bash
git commit -m "test: add cart and product image tests"
```

---

## 19. What To Ask or Check Later

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
- Whether product image representative field should be named `thumbnail`, `representative`, or `main`
- Whether product image ordering should start at 0 or 1
- Whether image URLs should be nullable or required
- Whether product image deletion should hard delete or soft delete
- Whether file upload will use local storage, S3, NCP Object Storage, or another storage service

---

## 20. Assistant Guidance for Future Conversations

When continuing this project:

- Speak Korean unless the user asks otherwise.
- Review diffs directly and say whether they are commit-ready.
- Prioritize correctness and learning over over-engineering.
- Do not recommend JWT before the session/Spring Security basics are clear.
- Current recommended direction is: finish current cart/order progress, then ProductImage entity, then ProductImage APIs, then frontend image display.
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
