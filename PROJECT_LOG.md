# Project Log: jpa-prac

This document tracks the current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

_Last updated: 2026-06-29 (Asia/Seoul)_

---

## 1. Project Snapshot

- Project name: `jpa-prac`
- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term goal: Build a portfolio-level, production-minded Spring web application
- Main domains:
  - Member
  - Product
  - Planned ProductImage
  - Orders
  - OrderItem
  - CartItem
  - Spring Security session login
  - Admin
  - Static frontend

---

## 2. Current Working Direction

The project should be developed step by step, with emphasis on:

- Java/Spring learning
- JPA relationship understanding
- Clean layered architecture
- API design for frontend usage
- Production-minded refactoring
- Portfolio-readiness
- Incremental feature development
- Authentication and authorization correctness
- Session management structure
- Test learning and test coverage
- Backend-centered business logic

The assistant role should be closer to a Spring/JPA teacher and code reviewer, not only a code generator.

Important frontend/backend boundary:

- The user is responsible for backend logic.
- The assistant may help generate frontend UI.
- Frontend code must not hide, patch, or compensate for backend defects.
- Backend remains the source of truth for authentication, authorization, validation, stock changes, order ownership, product status transitions, cart ownership, and persistence.
- Frontend role checks are display/routing only.

---

## 3. Current Priority

Current recommended priority:

1. Commit current cart/order-from-cart/refactoring progress.
2. Add `ProductImage` entity.
3. Add `ProductImageRepository`.
4. Add ProductImage DTOs, service, and admin controller APIs.
5. Update product responses to include representative image and/or image list.
6. Add focused tests for cart/order-from-cart and ProductImage behavior.
7. Keep frontend as an API-calling UI only.

Near-term ProductImage implementation order:

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

## 4. Current Decisions

### 2026-06-22

- Use `PROJECT_CONTEXT.md` as the stable project reference document.
- Use `PROJECT_LOG.md` as the progress and handoff document.
- The project may continue across multiple chat sessions.
- Important decisions and progress should be recorded here to preserve continuity.
- The assistant should explain Spring/JPA concepts in Korean with Java/Spring examples.
- Login knowledge will be learned from the basics before choosing a final implementation style.
- Session-based login is recommended before JWT.
- React/Vue frontend separation is a future direction, but backend API design should already consider frontend usage.

### 2026-06-24

- Keep the roadmap as `Session first -> JWT later`.
- Separate normal-user APIs from admin APIs.
- Use `/members/me` for current-user behavior.
- Move arbitrary member lookup/deletion to admin flow.
- Move all-order lookup to admin flow.
- Treat order cancellation as a domain command, not simple deletion.

### 2026-06-26

- `PROJECT_CONTEXT.md` and `PROJECT_LOG.md` should be kept as stable handoff documents.
- Exact filenames must be preserved:
  - `PROJECT_CONTEXT.md`
  - `PROJECT_LOG.md`
- Do not generate files with suffixes like `_updated`.
- Documentation format should remain consistent with the existing numbered section structure.

### 2026-06-29 Security/Admin/Product Progress

- Spring Security session login is now treated as the current authentication direction.
- Admin APIs are protected as admin-only backend APIs.
- Admin product CRUD has been added.
- Product status management has been added:
  - `ACTIVE`
  - `HIDDEN`
  - `DELETED`
- Product delete means soft delete through `ProductStatus.DELETED`.
- Public product list should expose only `ACTIVE` products.
- Admin product list should expose all product statuses.
- Static frontend files were created under:
  - `src/main/resources/static/index.html`
  - `src/main/resources/static/js/app.js`
  - `src/main/resources/static/css/style.css`
- Login page originally included an intro/hero section.
- The login intro/hero section was removed from the code, not merely hidden by CSS.
- Current login page should show only:
  - login form
  - signup form
- Browser flow was mostly verified by the user.
- Backend work should not be replaced by frontend patching.

### 2026-06-29 Testing Decision

Testing was selected as an important learning and implementation phase.

Testing should include:

- domain unit tests
- service unit tests with Mockito
- repository tests with `@DataJpaTest`
- controller/security integration tests with MockMvc
- broader integration tests later if needed

The user has limited prior experience writing tests, so test work must include explanation of:

- what behavior is being tested
- what data is prepared
- what action is executed
- what result is asserted
- what type of test is being written

Recommended test implementation order:

1. Learn JUnit 5 and AssertJ syntax.
2. Write pure domain unit tests for `Product`.
3. Write order stock/cancel-related domain tests.
4. Write service unit tests with Mockito.
5. Write repository tests with `@DataJpaTest`.
6. Write controller/security integration tests with MockMvc.
7. Add broader API flow integration tests if needed.

Important testing distinction:

- Domain unit tests usually do not require Mockito.
- Service unit tests often use Mockito to mock repositories, encoders, and other dependencies.
- Repository tests should not mock repositories; use `@DataJpaTest`.
- Controller/security integration tests should verify real API/security behavior with MockMvc.

---

## 5. CartItem Implementation Progress

### 2026-06-29 CartItem Basic CRUD

CartItem feature was implemented under `kr.co.prac.cartitem`.

Implemented or discussed components:

- `CartItem` entity
- `CartItemRepository`
- `CartItemRequest`
- `CartItemResponse`
- `CartItemService`
- `CartItemServiceImpl`
- `CartItemController`
- `CartItemNotFoundException`
- `EmptyCartItemException`

Implemented API direction:

```text
GET    /cart/items
POST   /cart/items
PATCH  /cart/items/{cartItemId}/increase
PATCH  /cart/items/{cartItemId}/decrease
DELETE /cart/items/{cartItemId}
```

Important completed decisions:

- Cart item APIs use authenticated principal memberId instead of client-provided memberId.
- `CartItemRequest` should not contain `memberId`.
- `CartItemRequest` contains `productId` and `count`.
- Required numeric request values use `@NotNull` and `@Positive`.
- Cart item update/delete lookup should be owner-scoped by memberId and cartItemId.
- Same product for same member should increase quantity rather than create duplicate rows.
- `CartItem` has a unique member-product constraint.
- `PATCH /cart/items/{cartItemId}/decrease` deletes the row when count is 1.
- Cart items do not reserve stock.
- Stock decreases only when an order is created.

Current caveat:

- CartItem service/controller tests still need to be added.
- Manual API flow should be verified after each major backend change.

---

## 6. Order From Cart Progress

### 2026-06-29 Order From Cart

Added cart-based order creation.

Implemented API:

```text
POST /orders/from-cart
```

Implemented service method:

```java
OrderDetailResponse orderFromCart(Long memberId);
```

Current order-from-cart flow:

1. Find current member.
2. Find cart items by member number.
3. Throw `EmptyCartItemException` if the cart is empty.
4. Create `Orders`.
5. For each cart item:
   - find product
   - validate `ProductStatus.ACTIVE`
   - decrease product stock
   - create `OrderItem`
   - add `OrderItemResponse`
6. Delete cart items after successful order creation.
7. Return `OrderDetailResponse`.

Important backend rule:

- Cart must not replace backend order validation.
- Product status and stock are checked again at order time.
- Cart items are deleted only after successful order creation.
- If an exception occurs, transaction rollback should preserve stock and cart items.

### Direct Order Update

`createOrder()` also validates `ProductStatus.ACTIVE` now, so hidden or deleted products should not be orderable through direct API calls.

### Light Refactoring Decision

The order creation logic was lightly refactored to reduce duplicated product lookup/status check/stock decrease behavior.

Decision:

- Do not over-engineer the order flow yet.
- Avoid introducing a full command object or service factory at this stage.
- Current small private-method refactoring is acceptable.
- Deeper refactoring can be revisited after tests are added.

Recommended next tests:

- direct order success
- direct order empty request failure
- direct order inactive product failure
- order-from-cart success
- order-from-cart empty cart failure
- order-from-cart inactive product failure
- order-from-cart deletes cart items after success

---

## 7. Testing Progress

Current testing state:

- `OrderServiceTest` was accidentally reduced/deleted during editing and then restored.
- `OrderServiceTest` includes the basic `create_order` test.
- `OrderServiceTest` includes the `empty_request` test.
- `CartItemRepository` mock was added because `OrderServiceImpl` now depends on it.
- Existing tests should not be deleted just because DTO construction is inconvenient.
- For DTOs without setters/constructors, `ReflectionTestUtils` can be used in tests.

Recommended next test additions:

```text
1. orderFromCart_success
2. orderFromCart_empty_cart
3. direct_order_inactive_product
4. order_from_cart_inactive_product
5. cart item service add/increase/decrease/delete tests
6. ProductImage repository/service tests after ProductImage implementation
```

---

## 8. ProductImage Decision

### 2026-06-29 Product Image Design Change

The project should not use a single `Product.imageUrl` field as the final design because the user clarified that:

1. A product may have only one representative image.
2. A product may have multiple images, with one representative image among them.

Decision:

```text
Product 1 : N ProductImage
```

Use a separate `ProductImage` entity/table instead of adding only `imageUrl` to `Product`.

Planned `ProductImage` fields:

```text
number
product
imageUrl
thumbnail or representative flag
sortOrder
createdAt / updatedAt through BaseTimeEntity
```

Recommended initial implementation:

- Store only image URLs first.
- Do not implement file upload yet.
- Do not implement S3/object storage yet.
- Do not add multiple storage strategies yet.
- Add upload later by saving the uploaded file and storing the generated URL in `ProductImage.imageUrl`.

Representative image rule:

- A product should have at most one representative image.
- Initially enforce this in service logic.
- If a new image is marked as representative, existing representative images for the same product should be unmarked.
- Database-level partial unique constraints can be considered later if needed.

Planned admin APIs:

```text
POST   /admin/products/{productNumber}/images
GET    /admin/products/{productNumber}/images
PATCH  /admin/products/{productNumber}/images/{imageId}/thumbnail
PUT    /admin/products/{productNumber}/images/{imageId}
DELETE /admin/products/{productNumber}/images/{imageId}
```

Planned public response behavior:

- Product list: expose representative image URL only.
- Product detail: expose full image list ordered by `sortOrder`.

---

## 9. Current Blockers and Risks

Current blockers:

- ProductImage entity has not been implemented yet.
- Product response DTOs do not yet include product images.
- Product image API design must be implemented before frontend image display.
- Cart/order-from-cart flow should still be manually verified.
- `orderFromCart` tests should be added.

Risks:

- Multiple product images can create N+1 query issues if product list returns image data naively.
- Representative image uniqueness is a business rule that must be enforced consistently.
- File upload should not be added until URL-based ProductImage flow is stable.
- Frontend should not fake product image behavior if backend response does not support it yet.

---

## 10. Next Concrete Steps

Immediate next steps:

1. Commit current cart/order-from-cart/refactoring progress.
2. Add `ProductImage` entity under the product domain.
3. Add `ProductImageRepository`.
4. Add `ProductImageRequest` and `ProductImageResponse`.
5. Add admin service/controller for product image management.
6. Add representative image setting behavior.
7. Update product response DTOs.
8. Add manual API tests.
9. Add service/repository tests.

Suggested next commit sequence:

```bash
git commit -m "refactor: simplify order creation flow"
git commit -m "docs: update project context and log"
git commit -m "feat: add product image entity"
git commit -m "feat: add product image management APIs"
```

If the current code and documentation are committed together, use:

```bash
git commit -m "docs: update project context after cart order flow"
```

---

## 11. Assistant Guidance for Future Sessions

When continuing this project:

- Start by checking `PROJECT_CONTEXT.md` and `PROJECT_LOG.md`.
- Then inspect the current GitHub code before giving exact code review.
- Speak Korean unless the user asks otherwise.
- Review diffs directly and say whether they are commit-ready.
- Prioritize correctness and learning over over-engineering.
- Do not recommend JWT before session/Spring Security basics are clear.
- For ProductImage, start with URL storage and `Product 1 : N ProductImage`.
- Do not jump to file upload before ProductImage URL flow is stable.
- For testing, explain purpose, setup, action, assertion, and test type.
- Do not add frontend logic that compensates for backend defects.
- Do not focus on whitespace-only issues unless requested.
- Use exact filenames:
  - `PROJECT_CONTEXT.md`
  - `PROJECT_LOG.md`
- Do not add `_updated` to generated file names.
