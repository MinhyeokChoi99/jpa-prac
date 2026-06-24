# Project Log: jpa-prac

This document tracks the current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

_Last updated: 2026-06-24 (Asia/Seoul)_

---

## 1. Project Snapshot

- Project name: `jpa-prac`
- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Main purpose: Java + Spring + MySQL practice project
- Long-term goal: Build a portfolio-level, production-minded Spring web application
- Main domain:
  - Member
  - Product
  - Orders
  - OrderItem
  - Login/session

---

## 2. Current Working Direction

The project should be developed step by step, with emphasis on:

- Java/Spring learning
- JPA relationship understanding
- Clean layered architecture
- API design for future frontend separation
- Production-minded refactoring
- Portfolio-readiness
- Incremental feature development

The assistant role should be closer to a Spring/JPA teacher and code reviewer, not only a code generator.

---

## 3. Current Priority

Current recommended priority:

1. Manually verify the custom `HttpSession` login flow.
2. Manually verify owner-restricted order detail and cancel flows.
3. Review broad/legacy order/member-order list APIs:
   - `GET /orders`
   - `GET /members/{memberId}/orders`
4. Replace plain password comparison with BCrypt/password encoder.
5. Learn tests later in one focused testing block.
6. Then compare Spring Security session login, Redis Session, and JWT.
7. Prepare APIs for future React/Vue frontend separation.

---

## 4. Current Decisions

### 2026-06-22

- Use `PROJECT_CONTEXT.md` as the stable project reference document.
- Use `PROJECT_LOG.md` as the progress and handoff document.
- The project may continue across multiple chat sessions.
- Important decisions and progress should be recorded here to preserve continuity.
- The assistant should explain Spring/JPA concepts in Korean with Java/Spring examples.
- Login knowledge will be learned from the basics before choosing a final implementation style.
- Session-based login is currently recommended before JWT.
- React/Vue frontend separation is a future direction, but backend API design should already consider it.

### 2026-06-24

- Keep the roadmap as `Session first -> JWT later`.
- Use `GET /members/me` instead of `GET /me` for now to match the current `/members/...` API style.
- Do not add tests in this step; the user wants to learn tests later in one focused block.
- Treat `/members/me` as both a session-login verification endpoint and the first real current-user API.
- Use `SessionUtil` as the current learning-stage helper for repeated session member-id lookup.
- Use `GET /members/me/orders` as the current-user order lookup API.
- Use session member id for `POST /orders`; do not accept `memberId` in `OrderCreateRequest`.
- Restrict order cancellation to the logged-in order owner.
- Rename cancel endpoint to `POST /orders/{orderId}/cancel`.
- Restrict order detail lookup to the logged-in order owner.
- Store order-time unit price in `OrderItem.unitPrice` and calculate item/order totals in response DTOs.
- Keep tests postponed.

---

## 5. Completed Work

### Entries

#### 2026-06-22

Task:

- Created initial `PROJECT_LOG.md` skeleton.

Notes:

- This file will be used to preserve project continuity across deleted or newly created chat sessions.

#### 2026-06-23

Task:

- Verified and documented the custom session-based login/logout implementation.

Notes:

- `POST /members/login` stores authenticated member id in `HttpSession`.
- `POST /members/logout` invalidates the current session.
- `SessionConst.LOGIN_MEMBER_ID` is used as the session key.
- `LoginResponse.memberId` exists for server-side session use and is hidden from JSON with `@JsonIgnore`.
- Authentication is still custom `HttpSession`-based, not Spring Security-based.

#### 2026-06-24 - Current Member Endpoint Update

Task:

- Added current logged-in member lookup endpoint.
- Added validation to login request.
- Added login-required exception handling.

Notes:

- `GET /members/me` retrieves the current member from the session.
- `LoginRequest` uses validation annotations.
- `LoginController` validates login requests with `@Valid`.
- `ErrorCode.LOGIN_REQUIRED` and `LoginRequiredException` were added.
- `SessionUtil.getLoginMemberId(request)` became the reusable helper for session member lookup.

#### 2026-06-24 - Current User Orders and Session-Based Order Creation

Task:

- Added current logged-in member order lookup.
- Changed order creation to use session identity.

Completed:

- Added `GET /members/me/orders`.
- Changed `POST /orders` so it obtains `loginMemberId` from `SessionUtil`.
- Removed `memberId` from `OrderCreateRequest`.
- Changed `OrderService.createOrder` to receive `memberId` as a method argument.
- Changed new member default role from `ADMIN` to `USER`.

Decision:

- After login, the server should derive the current user from the session instead of trusting request body or URL `memberId` for user-specific operations.

#### 2026-06-24 - Order Cancellation Ownership

Task:

- Restrict order cancellation to the logged-in order owner.
- Rename cancel endpoint semantically.

Completed:

- Changed cancel endpoint to `POST /orders/{orderId}/cancel`.
- Renamed service/controller methods from delete-oriented names to `cancelOrder`.
- Controller extracts `loginMemberId` using `SessionUtil`.
- Service verifies `orders.getMember().getNumber().equals(loginMemberId)` before cancellation.
- Added/used forbidden authorization exception for non-owner cancellation.
- Cancelled orders restore product stock and set status to `CANCEL`.

Decision:

- Cancellation is a business command, not a deletion.
- `403 FORBIDDEN` is appropriate when the user is logged in but not authorized to access another member's order.

#### 2026-06-24 - Order Detail Response and Ownership

Task:

- Improve order detail response.
- Restrict order detail lookup to the logged-in order owner.
- Clarify order item price semantics.

Completed:

- Added/updated `OrderDetailResponse`.
- Added/updated `OrderItemResponse`.
- `GET /orders/{orderId}` now uses session identity and owner validation.
- `OrderService.findOne(orderId, loginMemberId)` validates order ownership before returning detail.
- `OrderItem` stores `unitPrice` and `count`.
- Order creation stores `product.getPrice()` into `OrderItem.unitPrice` at order time.
- `OrderItemResponse.totalPrice` is calculated as `unitPrice * count`.
- `OrderDetailResponse.totalPrice` is calculated as the sum of item totals.

Decision:

- Store order-time unit price, not only line total, in `OrderItem`.
- Do not calculate historical order detail from current `Product.price`, because product prices may change later.

---

## 6. In Progress

### Current Task

```text
Task: Review broad/legacy order/member-order lookup APIs
Status: Next recommended task
Related files:
- src/main/java/kr/co/prac/member/controller/MemberController.java
- src/main/java/kr/co/prac/orders/controller/OrdersController.java
- src/main/java/kr/co/prac/orders/service/OrderService.java
- src/main/java/kr/co/prac/orders/service/OrderServiceImpl.java
Notes:
- Tests are intentionally postponed.
- POST /orders is session-member based.
- GET /orders/{orderId} is owner-restricted.
- POST /orders/{orderId}/cancel is owner-restricted.
- Remaining concern: broad list APIs may still expose too much data.
```

---

## 7. Next Tasks

Keep this list short and ordered.

```text
1. Manually verify login -> POST /orders -> GET /orders/{orderId} -> POST /orders/{orderId}/cancel.
2. Verify that another logged-in member cannot view or cancel the order.
3. Review GET /members/{memberId}/orders and decide whether to remove, restrict, or keep as admin-only later.
4. Review GET /orders and decide whether it should become admin-only later.
5. Rename OrderItemResponse.productPrice -> unitPrice if not already completed locally.
6. Replace plain password comparison with BCrypt/password encoder.
7. Learn and add tests later in a separate testing block.
```

---

## 8. Important Design Decisions

### Authentication

```text
Decision: Use custom HttpSession login first.
Reason: The user is learning session fundamentals before Spring Security/JWT.
Status: Implemented for core current-user and order ownership flows.
```

```text
Decision: Use GET /members/me as the current-user endpoint.
Reason: It confirms that JSESSIONID -> HttpSession -> login_member_id -> Member lookup works.
Status: Implemented.
```

```text
Decision: Do not add JWT yet.
Reason: JWT adds token complexity before session fundamentals are clear.
Status: Active decision.
```

### API Design

```text
Decision: Move user-specific APIs toward session identity.
Reason: After login, the server should not trust client-provided memberId for user-specific data.
Status: Implemented for /members/me/orders, POST /orders, GET /orders/{orderId}, and POST /orders/{orderId}/cancel.
```

```text
Decision: Treat order cancellation as cancel, not delete.
Reason: The order still exists; only status changes to CANCEL and stock is restored.
Status: Implemented with POST /orders/{orderId}/cancel.
```

### Order Price Model

```text
Decision: Store unit price and count in OrderItem.
Reason: The order-time unit price must be preserved even if Product.price changes later. Item total and order total can be calculated from unitPrice and count.
Status: Implemented.
```

### Package Structure

```text
Decision: Keep feature-based packages.
Reason: Easier to maintain as member/product/order/login grow.
Status: Active.
```

---

## 9. Code Review Notes

### Critical

```text
- Plain password comparison is still used. Acceptable for the current learning step only; replace with PasswordEncoder later.
```

### Important

```text
- GET /orders/{orderId} and POST /orders/{orderId}/cancel are now owner-restricted.
- GET /members/{memberId}/orders still trusts the path variable; prefer /members/me/orders for normal user APIs.
- GET /orders may expose all orders and should later become admin-only or be removed from normal user-facing APIs.
- File encoding/messages should later be normalized if Korean text continues to appear garbled in Git Bash/diff output.
```

### Optional

```text
- SessionUtil is now the current helper for session member lookup.
- Later compare SessionUtil with interceptor, argument resolver, or Spring Security principal.
- Consider renaming response field productPrice to unitPrice if not already completed locally.
- Add GitHub Actions CI later.
```

---

## 10. Current Domain Notes

### Member

```text
Current state:
- Member is now also the authentication subject.
- Login is based on email/password.
- Current logged-in member can be read through GET /members/me.
- New members default to Role.USER instead of Role.ADMIN.

Future direction:
- Add unique email constraint.
- Add BCrypt password encoding.
- Use session identity for "my" APIs.

Open questions:
- Whether signup should stay under /members or move to /auth later.
```

### Product

```text
Current state:
- Product list and stock behavior exist.
- Order creation decreases stock.
- Order cancellation restores stock.

Future direction:
- Add product detail, admin CRUD, search, pagination, category, and authorization.

Open questions:
- When to separate admin APIs.
```

### Orders

```text
Current state:
- Order list/detail/create/cancel flows exist.
- Current-user order lookup exists through GET /members/me/orders.
- Order creation uses the session member id through POST /orders.
- OrderCreateRequest no longer accepts memberId.
- Order detail is owner-restricted.
- Order cancellation is owner-restricted.
- Cancel endpoint is POST /orders/{orderId}/cancel.

Future direction:
- Review whether GET /orders should remain public, become admin-only, or be removed later.
- Review whether GET /members/{memberId}/orders should remain public, become admin-only, or be removed later.
- Consider returning an updated response after cancellation instead of void.

Open questions:
- Whether order cancellation should return void, an updated OrderResponse, or a simple status DTO.
```

### OrderItem

```text
Current state:
- OrderItem connects Orders and Product.
- OrderItem stores unitPrice and count.
- Order item total is calculated in response DTOs.
- Order total is calculated in OrderDetailResponse.

Future direction:
- Watch for lazy loading and N+1 issues.
- Consider fetch join or DTO projection later.

Open questions:
- Whether response field should be named productPrice or unitPrice.
```

---

## 11. Testing Notes

Testing is intentionally postponed for now.

### Existing Tests

```text
- Some member-related tests exist based on prior repository state.
```

### Recommended Tests Later

```text
- Login success with correct email/password.
- Login failure when member does not exist.
- Login failure when password is invalid.
- Successful login creates session and stores SessionConst.LOGIN_MEMBER_ID.
- Logout invalidates an existing session.
- Logout without session does not create a new session.
- GET /members/me returns current member when session exists.
- GET /members/me returns 401 when session does not exist.
- GET /members/me returns 401 after logout.
- GET /members/me/orders returns only orders for the session member.
- POST /orders returns 401 when no session exists.
- POST /orders creates the order for the session member.
- GET /orders/{orderId} returns detail for owner.
- GET /orders/{orderId} returns 403 for non-owner.
- POST /orders/{orderId}/cancel cancels owner order.
- POST /orders/{orderId}/cancel returns 403 for non-owner.
- Cancelled order restores product stock.
- Already cancelled order cannot be cancelled again.
```

---

## 12. Blockers / Questions

```text
- Need manual verification of the full session/order flow in local runtime.
- Need to decide how to handle broad list APIs: GET /orders and GET /members/{memberId}/orders.
- Need to decide when to introduce BCrypt/password encoder.
- Need to decide when to begin the testing block.
```

---

## 13. New Chat Handoff Summary

When starting a new chat session, paste or upload `PROJECT_CONTEXT.md` and this file.

Use this summary:

```text
This project is jpa-prac, a Java 21 + Spring Boot + MySQL practice project.
The goal is to grow it into a portfolio-level Spring web application.
The main domains are Member, Product, Orders, OrderItem, and custom Login/session.

Use PROJECT_CONTEXT.md as the stable project reference.
Use PROJECT_LOG.md as the current progress and handoff record.

The assistant should act as a Spring/JPA teacher and code reviewer.
Explain concepts in Korean, provide Java/Spring examples when useful, and review code with production-readiness and portfolio-readiness in mind.

Current authentication/session state:
- Custom HttpSession login/logout is implemented.
- Login stores SessionConst.LOGIN_MEMBER_ID in HttpSession.
- SessionUtil.getLoginMemberId(request) extracts the login member id or throws LoginRequiredException.
- GET /members/me is implemented and returns the current logged-in member.
- GET /members/me/orders is implemented and returns orders for the current logged-in member.
- POST /orders uses the session member id; OrderCreateRequest no longer accepts memberId.
- GET /orders/{orderId} is owner-restricted.
- POST /orders/{orderId}/cancel is owner-restricted.
- OrderItem stores unitPrice and count; totals are calculated in response DTOs.
- Tests are postponed for a later focused learning block.

Current priority:
1. Manually verify owner-restricted order detail/cancel behavior.
2. Review GET /orders and GET /members/{memberId}/orders.
3. Avoid trusting client-provided memberId for user-specific data.
4. Add BCrypt/password encoder later.
5. Learn tests later.
6. Compare Spring Security session login, Redis Session, and JWT later.
```
