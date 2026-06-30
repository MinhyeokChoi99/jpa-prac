# PROJECT_LOG.md

# Project Log: jpa-prac

This document tracks current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

_Last updated: 2026-07-01 (Asia/Seoul)_

---

## 1. Project Snapshot

- Project name: `jpa-prac`
- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term goal: Build a portfolio-level, production-minded Spring web application
- Main domains:
  - Member
  - Product
  - ProductImage
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
- Backend-centered business logic
- Product-management realism

The assistant role should be closer to a Spring/JPA teacher and code reviewer, not only a code generator.

Important frontend/backend boundary:

- The user is responsible for backend logic.
- Frontend code must not hide, patch, or compensate for backend defects.
- Backend remains the source of truth for authentication, authorization, validation, stock changes, order ownership, product status transitions, cart ownership, product image ownership, and persistence.
- Frontend role checks are display/routing only.

---

## 3. Current Priority

Current recommended priority:

1. Add admin product image file upload.
2. Keep existing URL-based image registration temporarily.
3. Store uploaded files locally first.
4. Save the generated accessible URL/path into `ProductImage.imageUrl`.
5. Reuse the existing thumbnail and sortOrder logic.
6. Manually test upload -> admin image list -> cart/order thumbnail flow.
7. After file upload is stable, add focused tests.

The next priority is not broad test-code writing yet.

Reason:

The admin product image feature currently works with URL strings, but a realistic admin product-management flow needs actual file upload and file management.

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

### 2026-06-29

- Spring Security session login is the current authentication direction.
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

### 2026-07-01

- Product image thumbnail integration was implemented and manually tested.
- Separate `ProductImage` entity approach is confirmed.
- Do not move back to a single `Product.imageUrl` primary design.
- Cart and order responses now use product thumbnail URLs.
- The immediate next feature is admin product image file upload.
- Broad test-code writing is deferred until file upload is implemented and manually verified.

---

## 5. ProductImage Thumbnail Integration Completed

Status: completed and manually verified.

This phase connected product images to cart and order responses.

Implemented or confirmed:

- Separate `ProductImage` entity is used instead of a single `Product.imageUrl` field.
- Product image admin APIs exist under `/admin/products/{productNumber}/images`.
- Product can have multiple images.
- One representative thumbnail image is selected by `thumbnail = true`.
- First image should become thumbnail automatically.
- Thumbnail switching should leave exactly one thumbnail image.
- Deleting the current thumbnail should promote another remaining image.
- Cart item response includes product thumbnail URL.
- Direct order creation response includes product thumbnail URL.
- Order-from-cart response includes product thumbnail URL.
- Order detail response includes product thumbnail URL.

Manual test flow completed:

```text
Admin login
-> product creation
-> product image registration by URL
-> multiple image registration
-> thumbnail switching
-> user login
-> add to cart
-> GET /cart/items thumbnail check
-> POST /orders/from-cart thumbnail check
-> cart cleared check
-> GET /orders/{orderId} thumbnail check
-> POST /orders direct order thumbnail check
-> thumbnail deletion/fallback check
```

Important bugs fixed during this phase:

1. Wrong `@Query` import
   - Incorrect: `org.springframework.data.jdbc.repository.query.Query`
   - Correct: `org.springframework.data.jpa.repository.Query`

2. Wrong JPQL image field
   - Incorrect: `pi.thumbnailUrl`
   - Correct: `pi.imageUrl`

3. Wrong JPQL thumbnail condition
   - Incorrect: `pi.imageUrl = true`
   - Correct: `pi.thumbnail = true`

4. Missing DTO projection constructor
   - JPQL `select new OrderItemResponse(...)` requires a matching constructor in `OrderItemResponse`.

5. Insufficient product image response DTO
   - `ProductImageResponse` should expose `number`, `productNumber`, `imageUrl`, `thumbnail`, and `sortOrder` for admin image management and manual verification.

Current confirmed status:

- Compile passes.
- Manual API testing passes according to user verification.
- URL-based product image registration is stable enough to extend toward file upload.

---

## 6. Current Product Image Design

Current image storage model:

```text
Product 1:N ProductImage
```

Current `ProductImage` role:

- Store the product-image relationship.
- Store the accessible image URL/path in `imageUrl`.
- Track whether the image is the representative thumbnail.
- Track display order through `sortOrder`.

Current request model:

```json
{
  "imageUrl": "https://example.com/product-image.jpg"
}
```

Important decision:

The current URL-based registration should remain for now.

Reason:

- It is already manually tested.
- It is useful for quick debugging.
- It does not block file upload.
- File upload can reuse the same `ProductImage.imageUrl` field after generating a local accessible URL/path.

---

## 7. Next Feature: Admin Product Image File Upload

Status: next task.

Goal:

Allow admin users to upload and manage actual image files when registering product images.

Recommended first implementation:

Local file upload.

Do not start with S3, NCP Object Storage, or other cloud object storage yet.

Reason:

- Local upload is easier to understand.
- It helps clarify multipart request handling.
- It makes file path, URL, and static resource mapping easier to debug.
- Cloud storage can be introduced later behind a storage abstraction.

Target API:

```text
POST /admin/products/{productNumber}/images/upload
Content-Type: multipart/form-data
file=<image file>
```

Target backend flow:

```text
Admin uploads MultipartFile
-> validate file
-> store file locally
-> generate accessible URL/path
-> save URL/path into ProductImage.imageUrl
-> apply thumbnail and sortOrder rules
-> return ProductImageResponse
```

Recommended package:

```text
kr.co.prac.global.storage
```

Recommended classes:

```text
StorageService
LocalStorageService
StorageProperties
```

Recommended interface:

```java
public interface StorageService {
    String store(MultipartFile file);
    void delete(String storedUrlOrPath);
}
```

Recommended controller method shape:

```java
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ProductImageResponse uploadProductImage(
        @PathVariable Long productNumber,
        @RequestPart MultipartFile file
) {
    return productImageService.uploadProductImage(productNumber, file);
}
```

Recommended service method:

```java
ProductImageResponse uploadProductImage(Long productNumber, MultipartFile file);
```

Recommended behavior:

- Empty file should be rejected.
- Non-image file should be rejected.
- File size should eventually be limited.
- Stored filename should be unique.
- Directory should be created automatically when missing.
- Stored file should be accessible through a URL such as `/uploads/product-images/{filename}`.
- The URL/path should be saved into `ProductImage.imageUrl`.
- Existing first-image thumbnail logic should still apply.
- Existing thumbnail switching logic should still apply.
- Existing delete/fallback logic should still apply.

---

## 8. File Upload Manual Test Plan

After implementation, manually test in this order.

```text
1. Admin login.
2. Create product.
3. Upload product image file with multipart/form-data.
4. Confirm response contains ProductImageResponse.
5. Confirm imageUrl points to `/uploads/...`.
6. Open returned imageUrl in browser.
7. Upload second image file.
8. Confirm only the first image is thumbnail unless changed.
9. Change thumbnail to second uploaded image.
10. Confirm image list has exactly one thumbnail.
11. User login.
12. Add product to cart.
13. GET /cart/items and confirm uploaded image URL appears.
14. POST /orders/from-cart and confirm uploaded image URL appears.
15. GET /orders/{orderId} and confirm uploaded image URL appears.
16. Delete thumbnail image.
17. Confirm fallback thumbnail behavior.
18. Confirm physical local file deletion policy if implemented.
```

---

## 9. Testing Roadmap Update

Testing remains important, but the immediate sequence is updated.

Current order:

1. Implement local product image file upload.
2. Manually verify the full upload-to-thumbnail flow.
3. Add focused file-upload and product-image tests.
4. Then expand to broader cart/order/security tests.

Recommended focused tests after upload implementation:

- Empty upload rejected.
- Non-image upload rejected.
- Image upload stores file.
- Image upload creates `ProductImage`.
- First uploaded image becomes thumbnail.
- Second uploaded image is not thumbnail.
- Thumbnail switching still works.
- Deleting current thumbnail promotes another image.
- Cart response uses uploaded image URL.
- Order detail response uses uploaded image URL.

---

## 10. Current Immediate Recommendation

Start file upload implementation.

Recommended implementation order:

1. Add upload properties.
2. Add static resource mapping for `/uploads/**`.
3. Add `StorageService`.
4. Add `LocalStorageService`.
5. Add product image upload service method.
6. Add admin multipart upload endpoint.
7. Manually test file upload and URL access.
8. Verify existing cart/order thumbnail responses still work.
9. Add focused tests only after upload flow is confirmed.

Do not remove the existing URL-registration endpoint yet.

Do not start broad test-code work until admin file upload is working.
