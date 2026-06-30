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
---

## 17. Current Confirmed Implementation State

This section records the latest confirmed state after the product image thumbnail flow was implemented and manually tested.

Confirmed implemented domains now include:

- Member
- Product
- ProductImage
- Orders
- OrderItem
- CartItem
- Login/session authentication
- Admin product management
- Admin product image management
- Cart ordering
- Direct ordering

Confirmed implemented or manually tested behavior:

- Spring Security session login is used.
- User-specific APIs use the authenticated principal instead of trusting request-provided member IDs.
- Admin APIs are protected under `/admin/**`.
- Product status supports:
  - `ACTIVE`
  - `HIDDEN`
  - `DELETED`
- Public product listing should expose only `ACTIVE` products.
- Admin product management can view and manage all product statuses.
- Cart item creation and quantity changes use the authenticated user.
- Adding to cart does not reduce stock.
- Ordering reduces stock.
- Ordering from cart deletes cart items after successful order creation.
- Order creation validates product status and stock.
- Product images are managed through a separate `ProductImage` entity.
- Product can have zero, one, or many images.
- Product image metadata includes:
  - product
  - imageUrl
  - thumbnail
  - sortOrder
- One image can be selected as the representative thumbnail.
- Cart response includes the product thumbnail URL.
- Direct order response includes the product thumbnail URL.
- Order-from-cart response includes the product thumbnail URL.
- Order detail response includes the product thumbnail URL.

Manual test flow completed:

```text
Admin login
-> product creation
-> product image registration by URL
-> multiple product image registration
-> thumbnail switching
-> user login
-> add product to cart
-> GET /cart/items thumbnail response check
-> POST /orders/from-cart thumbnail response check
-> cart cleared check
-> GET /orders/{orderId} thumbnail response check
-> POST /orders direct order thumbnail response check
-> thumbnail deletion/fallback check
```

Important debugging decisions from this phase:

- `OrderItemRepository` must import JPA `@Query`, not JDBC `@Query`.
- JPQL must use entity field names, not DTO response field names.
- `ProductImage.imageUrl` is the stored image path/URL field.
- `ProductImage.thumbnail` is the boolean field used to identify the representative image.
- JPQL `select new ...` DTO projection requires a matching DTO constructor.
- `ProductImageResponse` should expose enough admin-management data:
  - image number
  - product number
  - imageUrl
  - thumbnail
  - sortOrder

---

## 18. Product Image Support Current State

The current product image implementation is URL-based.

Current admin API direction:

```text
GET    /admin/products/{productNumber}/images
POST   /admin/products/{productNumber}/images
PUT    /admin/products/{productNumber}/images/{productImageNumber}
PATCH  /admin/products/{productNumber}/images/{productImageNumber}/thumbnail
DELETE /admin/products/{productNumber}/images/{productImageNumber}
```

Current request style:

```json
{
  "imageUrl": "https://example.com/product-image.jpg"
}
```

Current design decision:

- Do not add a single `Product.imageUrl` as the primary image design.
- Keep product images in the separate `ProductImage` entity.
- Continue storing the final accessible URL/path in `ProductImage.imageUrl`.
- The next step should not discard the current `ProductImage` structure.
- File upload should produce an accessible image URL/path and then save that value into `ProductImage.imageUrl`.

Important product image rules:

- A product may have no images.
- If a product has no image, response thumbnail URL may be `null`.
- The frontend should handle `null` thumbnail URL with a placeholder image.
- A product may have multiple images.
- Exactly one image should be thumbnail when images exist.
- The first uploaded/registered image should become thumbnail automatically.
- Changing thumbnail should unmark previous thumbnail images.
- Deleting the current thumbnail should promote another remaining image, preferably the lowest `sortOrder`.
- `sortOrder` should remain product-scoped, not globally unique.

---

## 19. Next Feature: Admin Product Image File Upload

The next major implementation task is product image file upload.

Reason:

The current admin product image feature only accepts an external image URL. For a more realistic admin product-management flow, the admin should be able to upload and manage actual image files.

This is now higher priority than test-code writing.

Target direction:

```text
Admin uploads image file
-> backend validates file
-> backend stores file
-> backend creates an accessible URL/path
-> backend saves that URL/path into ProductImage.imageUrl
-> existing ProductImage thumbnail/sortOrder logic continues to work
```

Recommended first implementation stage:

Use local file storage before cloud storage.

Reason:

- Simpler to understand.
- Easier to debug.
- Good learning step before S3/NCP Object Storage.
- Keeps storage concerns visible.
- Can later be replaced behind a `StorageService` abstraction.

Recommended local upload API:

```text
POST /admin/products/{productNumber}/images/upload
Content-Type: multipart/form-data
file=<image file>
```

Recommended behavior:

- Accept `MultipartFile`.
- Reject empty file.
- Validate image content type.
- Optionally validate file size.
- Generate a unique stored filename.
- Store the file under a local upload directory.
- Expose uploaded files through a static resource mapping such as `/uploads/**`.
- Save the generated URL/path to `ProductImage.imageUrl`.
- Reuse existing thumbnail and sortOrder logic.

Recommended package direction:

```text
kr.co.prac.global.storage
├── StorageService.java
├── LocalStorageService.java
└── StorageProperties.java
```

Recommended service contract:

```java
public interface StorageService {
    String store(MultipartFile file);
    void delete(String storedUrlOrPath);
}
```

Recommended controller shape:

```java
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ProductImageResponse uploadProductImage(
        @PathVariable Long productNumber,
        @RequestPart MultipartFile file
) {
    return productImageService.uploadProductImage(productNumber, file);
}
```

Recommended service flow:

```text
ProductImageService.uploadProductImage(productNumber, file)
1. Validate product exists.
2. Store file through StorageService.
3. Convert stored file path to accessible URL/path.
4. Create ProductImage with imageUrl.
5. Apply first-image thumbnail rule.
6. Return ProductImageResponse.
```

Important design decision:

Keep the existing URL registration API temporarily.

Reason:

- It remains useful for quick manual testing.
- It helps compare URL-based and file-based flows.
- It avoids breaking existing tested behavior.
- It can be deprecated or removed later after file upload is stable.

Recommended API coexistence:

```text
POST /admin/products/{productNumber}/images
- JSON body with imageUrl
- Keep for now

POST /admin/products/{productNumber}/images/upload
- multipart/form-data file upload
- Add next
```

Do not jump directly to S3 or cloud storage yet.

Cloud storage should come after:

- local upload works
- uploaded files can be served
- ProductImage is saved correctly
- thumbnail switching still works
- deletion policy is decided
- local code is hidden behind StorageService

---

## 20. File Upload Implementation Checklist

Implement file upload in this order.

### Step 1: Configuration

Add upload configuration.

Example properties:

```yaml
app:
  upload:
    dir: uploads
    url-prefix: /uploads
```

### Step 2: Static Resource Mapping

Expose uploaded files through Spring MVC resource handling.

Expected behavior:

```text
Saved local file:
uploads/product-images/uuid-image.jpg

Accessible URL:
http://localhost:8080/uploads/product-images/uuid-image.jpg
```

### Step 3: Storage Service

Add a storage abstraction.

Recommended responsibilities:

- validate file is not empty
- validate image file type
- generate unique filename
- create directories if missing
- save file to disk
- return accessible URL/path

### Step 4: ProductImage Service Upload Method

Add a new service method:

```java
ProductImageResponse uploadProductImage(Long productNumber, MultipartFile file);
```

This method should reuse existing image creation rules.

### Step 5: Admin Upload Endpoint

Add endpoint:

```text
POST /admin/products/{productNumber}/images/upload
```

Request type:

```text
multipart/form-data
```

Field name:

```text
file
```

### Step 6: Manual Test

Manual test flow:

```text
Admin login
-> create product
-> upload image file
-> GET /admin/products/{productNumber}/images
-> confirm imageUrl points to uploaded file
-> open imageUrl in browser
-> change thumbnail
-> user login
-> add product to cart
-> GET /cart/items thumbnail check
-> POST /orders/from-cart thumbnail check
-> GET /orders/{orderId} thumbnail check
```

### Step 7: Cleanup Policy

Decide deletion behavior.

Recommended first rule:

- When deleting `ProductImage`, delete DB row first.
- Also try to delete the local file if the image belongs to local upload storage.
- If physical file deletion fails, log it and do not fail the whole business operation yet.

Potential later improvement:

- Store original filename, stored filename, file size, and content type separately.
- Add a `ProductImageFile` value object or additional fields.
- Use cloud object storage and signed URLs if needed.

---

## 21. Testing Roadmap Update

Testing is still important, but it should come after the immediate file upload feature is implemented and manually verified.

Updated test priority:

1. Implement local file upload.
2. Manually verify uploaded product image flow.
3. Add focused tests around the finished file-upload behavior.
4. Then expand into broader service/repository/controller/security tests.

Recommended file-upload-related tests after implementation:

- Storage service rejects empty files.
- Storage service rejects non-image files.
- Storage service stores image file and returns accessible path.
- Product image upload creates `ProductImage`.
- First uploaded image becomes thumbnail.
- Second uploaded image is not thumbnail.
- Thumbnail switching still works after upload.
- Deleting image removes or attempts to remove stored local file.
- Cart response uses uploaded image URL.
- Order detail response uses uploaded image URL.

---

## 22. Current Best Next Step

Recommended immediate next step:

Design and implement admin product image file upload.

Priority order:

1. Decide local upload directory and URL prefix.
2. Add `StorageService` and `LocalStorageService`.
3. Add static resource mapping for `/uploads/**`.
4. Add `ProductImageService.uploadProductImage(...)`.
5. Add admin multipart endpoint:
   - `POST /admin/products/{productNumber}/images/upload`
6. Manually test uploaded file access in browser.
7. Verify uploaded file thumbnail appears in:
   - admin image list
   - cart response
   - direct order response
   - order-from-cart response
   - order detail response
8. Only after this flow works, write focused tests.

Do not replace the existing URL-based image registration immediately.

Do not start broad test-code work before file upload is working, because the current next product-management gap is actual file handling by the admin.

