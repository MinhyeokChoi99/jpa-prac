# PROJECT_CONTEXT.md

# Project Context: jpa-prac

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Current purpose: Java + Spring + MySQL practice project
- Long-term purpose: Build and deploy a web application that feels close to a real commercial service
- Main domain: Product, member, order, and order item management
- User language: Korean
- Preferred assistant response style: Korean explanation + Java/Spring code examples
- Documentation language: English is acceptable and preferred for technical reuse

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

Current stack:

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Data JDBC
- Bean Validation
- MySQL
- H2 for tests
- Lombok
- Springdoc OpenAPI / Swagger
- Maven

Primary runtime database:

- MySQL

Test database:

- H2 in MySQL compatibility mode

Potential future stack to study and add:

- Spring Security
- Session-based login or JWT authentication
- Spring Boot profiles
- Flyway or Liquibase
- QueryDSL
- Redis
- Docker
- GitHub Actions
- Testcontainers
- AWS, NCP, or another cloud platform
- Nginx
- Monitoring/logging tools

---

## 4. Current Known Repository State

The current repository appears to include the following domains:

- Member
- Product
- Orders
- OrderItem

The current project appears to include:

- Member CRUD
- Product list API
- Order list API
- Order detail API
- Order creation API
- Order cancellation logic
- Member-specific order lookup
- Basic DTO separation
- Basic validation for order creation
- Product stock decrease on order creation
- Product stock restore on order cancellation
- Static HTML page that calls backend APIs
- MySQL runtime configuration
- H2 test configuration
- Some unit/integration tests for member-related logic

Seed data currently includes:

- Products
- Members
- Orders
- Order items

The project is already beyond a pure CRUD toy example because it contains basic business behavior around orders and stock.

---

## 5. Current Domain Understanding

### Member

A member represents a user/customer-like entity.

Known or expected fields:

- number
- name
- email

Current member-related features appear to include:

- Create member
- Find member by ID
- Find all members
- Update member
- Delete member
- Find orders by member

Important future direction:

- Member should eventually become the authentication subject.
- Email should probably become a unique login identifier.
- Password should be added only when Spring Security is introduced.
- Member roles should be added later, such as `USER` and `ADMIN`.

---

### Product

A product represents an item that can be ordered.

Known or expected fields:

- number
- name
- price
- stock

Current product behavior appears to include:

- Product list retrieval
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

Current order behavior appears to include:

- Order list retrieval
- Order detail retrieval
- Order creation
- Order cancellation
- Member-specific order lookup

Important future direction:

- Use clearer order status transition rules.
- Prevent invalid status changes.
- Separate order creation request into a wrapper DTO.
- Return order items in order detail response.
- Add order total price.
- Add payment-ready/payment-completed concepts later.

---

### OrderItem

An order item connects an order with a product.

Known or expected fields:

- order
- product
- orderPrice
- count

Current behavior appears to include:

- Create order item when an order is created
- Restore product stock when an order is cancelled

Important future direction:

- Add bidirectional convenience methods only if needed.
- Avoid exposing entity graph directly in API responses.
- Use DTOs for order item response.
- Be careful with lazy loading and N+1 queries.

---

## 6. Current API Direction

Current or expected API shape:

```text
GET    /members
POST   /members
GET    /members/{memberId}
PUT    /members/{memberId}
DELETE /members/{memberId}
GET    /members/{memberId}/orders

GET    /products

GET    /orders
GET    /orders/{orderId}
POST   /orders
DELETE /orders/{orderId}
```

This API design is already moving in a REST-like direction.

Recommended future API direction:

```text
POST   /auth/signup
POST   /auth/login
POST   /auth/logout
GET    /me

GET    /products
GET    /products/{productId}
POST   /admin/products
PUT    /admin/products/{productId}
DELETE /admin/products/{productId}

POST   /orders
GET    /orders
GET    /orders/{orderId}
POST   /orders/{orderId}/cancel
```

For production-style APIs, prefer action endpoints only when the action is a domain command, such as order cancellation.

Example:

```text
POST /orders/{orderId}/cancel
```

is usually clearer than:

```text
DELETE /orders/{orderId}
```

because cancelling an order is not the same as deleting order history.

---

## 7. Current Architecture

Current architecture appears to follow layered architecture:

```text
Controller
↓
Service
↓
Repository
↓
Entity / Database
```

Current package direction appears to include:

```text
kr.co.prac
├── controller
├── dto
├── entity
├── repository
├── service
└── resources
```

Recommended future package structure:

```text
kr.co.prac
├── global
│   ├── exception
│   ├── response
│   └── config
├── member
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── product
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── order
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
└── PracApplication.java
```

Recommendation:

As the project grows, feature-based packages are likely cleaner than layer-only packages.

For example:

```text
member/controller
member/service
member/repository
member/dto
```

is usually easier to maintain than:

```text
controller/membercontroller
service/memberservice
dto/member
```

---

## 8. Current Code Review Priorities

When reviewing this project, classify feedback into three levels.

### Critical

Issues that may cause runtime errors, wrong data, security problems, or production failure.

Examples:

- Hardcoded database credentials
- Missing transaction boundary
- Incorrect stock handling
- Exposing sensitive user data
- Unsafe authentication logic
- Incorrect JPA relationship mapping
- Data loss caused by `ddl-auto=create`
- Missing validation for important requests

### Important

Issues that affect maintainability, scalability, or backend conventions.

Examples:

- Package naming convention
- Entity exposed directly to API
- Missing global exception handler
- Inconsistent API response format
- Service methods doing too many things
- No pagination for list APIs
- Possible N+1 query problem
- Weak test coverage
- Using `DELETE` for order cancellation

### Optional

Issues that are useful but not urgent.

Examples:

- More detailed Swagger documentation
- Custom response wrapper
- More expressive method names
- Better frontend UI
- Dockerization
- CI/CD
- QueryDSL
- Monitoring

---

## 9. Immediate Refactoring Recommendations

Before adding many new features, prioritize these improvements.

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

### 3. Add global exception handling

Create a consistent error response instead of returning raw exceptions.

Recommended classes:

```text
global/exception/GlobalExceptionHandler.java
global/exception/ErrorResponse.java
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

### 5. Improve package naming

Java package names should be lowercase.

Prefer:

```text
product/service
member/service
order/service
```

Avoid mixed-case package names such as:

```text
productService
memberService
orderService
```

### 6. Replace order deletion with order cancellation command

Business-wise, cancelling an order should preserve order history.

Recommended endpoint:

```text
POST /orders/{orderId}/cancel
```

Service method:

```java
@Transactional
public OrderResponse cancelOrder(Long orderId) {
    Orders order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

    order.cancel();

    return OrderResponse.from(order);
}
```

---

## 10. Recommended Next Feature: Login

The user is considering login as the next feature. This is a good direction, but it should be added in stages.

### Login Stage 1: Member signup

Add:

```text
POST /auth/signup
```

Concepts:

- Signup request DTO
- Password encoding
- Duplicate email validation
- Member role
- Validation

Required dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Login Stage 2: Session-based login

Start with session login before JWT if the user is still learning Spring Security.

Why:

- Easier to understand
- Closer to traditional web login
- Good for learning authentication flow
- Less token complexity

### Login Stage 3: Authorization

Add roles:

```java
public enum Role {
    USER,
    ADMIN
}
```

Example rule:

- Normal users can create and view their own orders.
- Admin users can create/update/delete products.
- Admin users can view all orders.

### Login Stage 4: JWT

Add JWT only after basic Spring Security flow is understood.

JWT is useful for stateless REST APIs and frontend/backend separation, but it adds more complexity.

---

## 11. Recommended Feature Roadmap

### Phase 0: Stabilization

Goal: Make the existing project cleaner before adding large features.

Tasks:

- Clean package structure
- Add global exception handling
- Add error response DTO
- Clean application profiles
- Remove hardcoded DB password
- Remove duplicated dependencies
- Add README
- Add this `PROJECT_CONTEXT.md`

### Phase 1: Member and Authentication

Goal: Turn `Member` into a real user account.

Tasks:

- Add password field
- Add password encoder
- Add signup
- Add login
- Add logout
- Add role
- Add current user API: `GET /me`

### Phase 2: Product Management

Goal: Make product management realistic.

Tasks:

- Product create/update/delete APIs
- Product detail API
- Product search
- Product pagination
- Product category
- Admin-only product management

### Phase 3: Order Flow

Goal: Make orders closer to a real service.

Tasks:

- Create order from multiple order items
- Validate stock
- Calculate total price
- Cancel order
- Restore stock on cancellation
- Prevent duplicate cancellation
- View my orders
- View order detail with order items

### Phase 4: Cart

Goal: Add a common e-commerce feature.

Tasks:

- Add cart
- Add cart item
- Add product to cart
- Change cart item quantity
- Remove cart item
- Create order from cart

### Phase 5: Payment Simulation

Goal: Practice real-world business flow without real payment integration first.

Tasks:

- Payment status
- Mock payment API
- Payment success/failure
- Order status transition after payment
- Payment cancellation

### Phase 6: Production Readiness

Goal: Make the project deployable.

Tasks:

- Docker
- MySQL container
- Spring profiles
- Environment variables
- GitHub Actions
- Cloud deployment
- Nginx reverse proxy
- HTTPS
- Logging
- Monitoring

---

## 12. Testing Roadmap

Current project already contains some member-related tests.

Recommended next tests:

### Service tests

- Member signup success
- Duplicate email failure
- Product stock decrease
- Product stock shortage failure
- Order creation success
- Order cancellation success
- Duplicate cancellation failure

### Repository tests

- Find member by email
- Find orders by member
- Find order items by order

### Controller tests

- Member API validation
- Order creation validation
- Product list API
- Error response format

### Integration tests

- Full order creation flow
- Full order cancellation flow
- Signup/login/order flow after authentication is added

Recommended tools:

- JUnit 5
- AssertJ
- Spring Boot Test
- MockMvc
- Testcontainers later

---

## 13. JPA Learning Priorities

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

## 14. Production-Minded Standards

As this project moves toward a commercializable web service, apply these standards gradually.

### Security

- Do not store raw passwords.
- Do not expose internal entity fields.
- Do not trust client-provided user ID after login.
- Use authenticated principal for user-specific operations.
- Separate user and admin permissions.

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
- Add tests when fixing bugs or adding features.

---

## 15. Assistant Instructions for Future Conversations

When the user asks about this project:

1. Assume the user is working on `jpa-prac`.
2. Answer in Korean.
3. Provide Java/Spring code when useful.
4. Connect explanations to the current product/member/order domain.
5. Review code with production-readiness and portfolio-readiness in mind.
6. Separate beginner-friendly explanation from best-practice recommendation.
7. Recommend incremental changes instead of large rewrites.
8. If reviewing code, classify issues as Critical, Important, or Optional.
9. Suggest the next best feature or refactoring step when relevant.
10. Treat login/authentication as a likely next major feature.

---

## 16. What To Ask or Check Later

When more precision is needed, check or ask for:

- Current entity source code
- Current controller source code
- Current service source code
- Current repository source code
- Current DTOs
- Current test files
- Current API behavior
- Current database schema
- Whether the project uses sessions or JWT
- Whether the project has a frontend direction
- Whether the project will be deployed to AWS, NCP, or another platform

This document gives long-term context, but exact code review should still be based on the current source code.

---

## 17. Current Best Next Step

Recommended next step:

1. Add `PROJECT_CONTEXT.md`
2. Add or improve `README.md`
3. Refactor package naming and structure
4. Add global exception handling
5. Separate local/test/prod configuration
6. Then add login with Spring Security

The next major feature can be login, but the project should first stabilize error handling, configuration, and package structure so authentication does not get built on top of fragile foundations.
