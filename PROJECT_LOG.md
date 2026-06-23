# Project Log: jpa-prac

This document tracks the current progress, decisions, completed work, blockers, and next steps for the `jpa-prac` project.

Use this file as a handoff document when continuing the project in a new chat session.

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

1. Understand and manually verify the custom `HttpSession` login flow.
2. Use session identity in member-specific APIs.
3. Current-user order lookup such as `GET /members/me/orders`. [DONE]
4. Create orders using the session member id instead of request-body `memberId`. [DONE]
5. Restrict order cancellation so only the logged-in order owner can cancel. [NEXT]
6. Replace plain password comparison with BCrypt/password encoder.
7. Learn tests later in one focused testing block.
8. Then compare Spring Security session login, Redis Session, and JWT.
9. Prepare APIs for future React/Vue frontend separation.

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
- The next business authorization topic should be order cancellation ownership.

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

#### 2026-06-24

Task:

- Added current logged-in member lookup endpoint.
- Added validation to login request.
- Added login-required exception handling.

Notes:

- `GET /members/me` now retrieves the current member from the session.
- `LoginRequest` now uses validation annotations.
- `LoginController` now validates login requests with `@Valid`.
- `ErrorCode.LOGIN_REQUIRED` and `LoginRequiredException` were added.
- Removed unused import from `ErrorCode`.
- Later updates added `SessionUtil`, `GET /members/me/orders`, and session-based `POST /orders`.

---

## 6. In Progress

### Current Task

```text
Task: Restrict order cancellation by session member ownership
Status: Ready to start
Related files:
- src/main/java/kr/co/prac/orders/controller/OrdersController.java
- src/main/java/kr/co/prac/orders/service/OrderService.java
- src/main/java/kr/co/prac/orders/service/OrderServiceImpl.java
- src/main/java/kr/co/prac/orders/entity/Orders.java
- src/main/java/kr/co/prac/global/session/SessionUtil.java
Notes:
- Tests are intentionally postponed.
- POST /orders already uses the login member id from the session.
- Next problem: a logged-in user may still be able to cancel another user's order if they know the order id.
```

---

## 7. Next Tasks

Keep this list short and ordered.

```text
1. Manually verify login -> /members/me -> /members/me/orders -> POST /orders -> logout.
2. Restrict order cancellation to the logged-in order owner.
3. Rename/clarify the cancel endpoint from POST /orders/{orderId} to POST /orders/{orderId}/cancel.
4. Review or restrict GET /members/{memberId}/orders because logged-in user APIs should prefer /members/me/orders.
5. Replace plain password comparison with BCrypt/password encoder.
6. Learn and add tests later in a separate testing block.
```

---

## 8. Important Design Decisions

### Authentication

```text
Decision: Use custom HttpSession login first.
Reason: The user is learning session fundamentals before Spring Security/JWT.
Status: In progress; login/logout, /members/me, /members/me/orders, and session-based POST /orders are implemented.
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
Status: In progress; /members/me/orders and POST /orders now use session identity. Order cancellation ownership is next.
```

### Package Structure

```text
Decision: Keep feature-based packages.
Reason: Easier to maintain as member/product/order/login grow.
Status: Active.
```

### Database / Configuration

```text
Decision: Keep MySQL runtime and H2 test profile for now.
Reason: Practical for learning and later test setup.
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
- Order cancellation still needs ownership validation against the logged-in member.
- `GET /members/{memberId}/orders` still trusts the path variable; prefer `/members/me/orders` for current-user APIs.
- User-specific APIs should continue moving away from URL/body memberId and toward session identity.
```

### Optional

```text
- `SessionUtil` is now the current helper for session member lookup.
- Later compare `SessionUtil` with interceptor, argument resolver, or Spring Security principal.
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
- Order creation now uses the session member id through POST /orders.
- OrderCreateRequest no longer accepts memberId.

Future direction:
- Restrict order cancellation to the logged-in order owner.
- Prefer cancel command endpoint later: POST /orders/{orderId}/cancel.
- Review whether GET /members/{memberId}/orders should remain public/admin-only or be removed later.

Open questions:
- Whether order cancellation should return void, an updated OrdersResponse, or a simple status DTO.
```

### OrderItem

```text
Current state:
- OrderItem connects Orders and Product.

Future direction:
- Return order items in order detail response.
- Watch for lazy loading and N+1 issues.

Open questions:
- Whether fetch join or DTO projection is needed later.
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
- Order cancellation ownership tests after cancellation is refactored.
```

---

## 12. Blockers / Questions

```text
- Need local manual verification because remote execution/build was not available in the prior environment.
- Need to implement and verify order cancellation ownership check.
- Need to decide whether the legacy GET /members/{memberId}/orders endpoint should become admin-only, remain temporarily, or be removed later.
```

---

## 13. New Chat Handoff Summary

When starting a new chat session, paste or upload `PROJECT_CONTEXT.md` and this file.

Use this summary:

```text
This project is `jpa-prac`, a Java 21 + Spring Boot + MySQL practice project.
The goal is to grow it into a portfolio-level Spring web application.
The main domains are Member, Product, Orders, OrderItem, and basic Login/session.

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
- POST /orders now uses the session member id; OrderCreateRequest no longer accepts memberId.
- LoginRequest validation and LoginRequiredException are implemented.
- Tests are postponed for a later focused learning block.

Current priority:
1. Manually verify login -> /members/me -> /members/me/orders -> POST /orders -> logout.
2. Restrict order cancellation to the logged-in order owner.
3. Avoid trusting client-provided memberId for user-specific data.
4. Add BCrypt/password encoder later.
5. Learn tests later.
6. Compare Spring Security session login, Redis Session, and JWT later.
```

---

## 2026-06-23 - Session Login Implementation Update

### Completed

- Verified that basic custom session login is now implemented in the GitHub repository.
- Added `POST /members/login` in `LoginController`.
- Added `POST /members/logout` in `LoginController`.
- Added `SessionConst.LOGIN_MEMBER_ID` under `kr.co.prac.global.session`.
- Updated login flow so the controller stores authenticated member id in `HttpSession` using `SessionConst.LOGIN_MEMBER_ID`.
- Updated `LoginResponse` so `memberId` exists for server-side session use and is hidden from JSON response with `@JsonIgnore`.
- Verified logout uses `request.getSession(false)` and invalidates an existing session with `session.invalidate()`.
- Updated `ErrorCode.INVALID_PASSWORD` to use `HttpStatus.UNAUTHORIZED`.
- Verified `InvalidPasswordException` delegates to `ErrorCode.INVALID_PASSWORD`.
- Verified `data.sql` now quotes role values as SQL strings, preventing `ADMIN`/`USER` from being interpreted as column names.

### Current Authentication State

- Authentication is currently a learning-stage custom `HttpSession` implementation, not yet Spring Security-based.
- Login identity is stored as a session attribute:
  - key: `SessionConst.LOGIN_MEMBER_ID`
  - value: authenticated member id
- Browser-session association is handled through servlet session mechanism and `JSESSIONID` cookie.
- Logout removes login state by invalidating the current session.

### Decisions

- Keep the roadmap as `Session first -> JWT later`.
- Continue custom session login temporarily to understand `HttpSession`, `JSESSIONID`, and logout mechanics.
- Hide `LoginResponse.memberId` from API response because it is needed internally for session storage.
- Keep `SessionConst` in `global.session` because `/members/me`, interceptors, and user-specific APIs will likely need the same key.

### Problems Found

- Password comparison is still plain string comparison. This is acceptable only for the current learning step; later replace it with `PasswordEncoder.matches()`.
- `data.sql` role values are now quoted, but password seed values are still numeric-looking. Since the column is `varchar`, single-quoted password strings are cleaner: `'123'`, `'456'`, `'789'`.

### Next

1. Implement current user lookup.
2. Add login/session tests using MockMvc later.
3. Replace plain password comparison with BCrypt/password encoder after raw session flow is understood.
4. Later compare Spring Security session login, Redis Session, and JWT.

### Notes for Future Chat

- The user has now implemented the first working custom session login/logout stage.
- Future auth explanations should build from current concrete code:
  - `LoginController`
  - `LoginServiceImpl`
  - `LoginResponse`
  - `SessionConst`
- Do not jump directly to JWT unless the user explicitly changes the roadmap.

---

## 2026-06-24 - Current Member Endpoint Update

### Completed

- Added login request validation to `LoginRequest`.
  - `@NotBlank` for email.
  - `@Email` for email format.
  - `@NotBlank` for password.
- Added `@Valid` to the login request parameter in `LoginController`.
- Added `ErrorCode.LOGIN_REQUIRED` with `HttpStatus.UNAUTHORIZED`.
- Added `LoginRequiredException` extending `BusinessException`.
- Added current logged-in member endpoint:

```text
GET /members/me
```

- Implemented `/members/me` using the current `HttpSession`.
  - Uses `request.getSession(false)` to avoid creating a new session.
  - Throws `LoginRequiredException` when no session exists.
  - Reads `SessionConst.LOGIN_MEMBER_ID` from the session.
  - Throws `LoginRequiredException` when the session does not contain a login member id.
  - Loads and returns the current member as `MemberResponse`.
- Removed unused import from `ErrorCode`.

### Decisions

- Use `GET /members/me` instead of `GET /me` for now.
- Keep the endpoint under the current `/members/...` API style for consistency while learning.
- Treat `/members/me` as both:
  - a session-login verification endpoint, and
  - the first real current-user API.
- Do not add tests in this step.
- Postpone MockMvc/session tests to a later focused testing study block.

### Current Authentication State

- Login is still custom `HttpSession`-based.
- Login success stores the authenticated member id in session using:

```java
SessionConst.LOGIN_MEMBER_ID
```

- `/members/me` proves the following flow:

```text
JSESSIONID -> HttpSession -> login_member_id -> Member lookup -> MemberResponse
```

### Problems Found

- Repeated session lookup logic may become duplicated if future APIs such as `/members/me/orders`, cart APIs, or order creation also need the current member id.
- For now, the code can stay inside the controller for learning clarity.
- Later, session member lookup should be extracted into a reusable structure.

### Next

1. Manually verify the flow:
   - Call `GET /members/me` before login and confirm `401 Unauthorized`.
   - Login with `POST /members/login`.
   - Call `GET /members/me` again and confirm current member response.
   - Logout with `POST /members/logout`.
   - Call `GET /members/me` again and confirm `401 Unauthorized`.
2. Start converting member-specific APIs to use session identity:
   - Consider `GET /members/me/orders`.
   - Avoid trusting client-provided `memberId` for user-specific data after login.
3. Add BCrypt/password encoder after the basic session flow is understood.
4. Learn and add tests later in a separate testing block.
5. Compare Spring Security session login after custom session fundamentals are clear.

### Notes for Future Chat

- The project now has a working current-user endpoint based on `HttpSession`.
- Do not recommend JWT next.
- The next architectural topic should be how to avoid repeating session lookup logic.
- The next business-feature topic should be member-specific order lookup using the logged-in session member.

---

## 2026-06-24 - Session Identity Order Flow Update

### Completed

- Added reusable session helper:

```java
SessionUtil.getLoginMemberId(HttpServletRequest request)
```

- Moved current-user member lookup into the member API flow:

```text
GET /members/me
```

- Added current-user order lookup:

```text
GET /members/me/orders
```

- Changed order creation to use session identity:

```text
POST /orders
```

- Removed `memberId` from `OrderCreateRequest`.
- Changed `OrderService.createOrder(...)` to receive the authenticated member id as a method argument:

```java
OrdersResponse createOrder(Long memberId, List<OrderCreateRequest> orderCreateRequests)
```

- Updated `OrdersController` so order creation calls `SessionUtil.getLoginMemberId(request)` before calling the service.
- Updated `OrderServiceImpl` so the new order's member is found by the session member id instead of `orderCreateRequests.get(0).getMemberId()`.
- Renamed controller field usage from `orderServiceImpl` to `orderService` where reviewed.
- Changed newly created member default role from `Role.ADMIN` to `Role.USER`.

### Decisions

- Keep custom `HttpSession` login for now.
- Use `SessionUtil` as the current learning-stage abstraction for repeated session-member lookup.
- Keep API style under `/members/...` for now:

```text
GET /members/me
GET /members/me/orders
```

- Make `POST /orders` login-required by deriving the member id from the session.
- Do not allow clients to choose the order owner through request body.
- Do not add tests in this step.

### Problems Found

- Order cancellation still likely trusts only `orderId` and does not yet verify ownership.
- A logged-in user may be able to cancel another user's order if they know the order id.
- Existing `GET /members/{memberId}/orders` still trusts the path variable and should later be reviewed, restricted, or made admin-only.
- Password comparison is still plain string comparison and should later be replaced with BCrypt/password encoder.

### Next

1. Manually verify the flow:
   - Login with `POST /members/login`.
   - Call `GET /members/me`.
   - Call `GET /members/me/orders`.
   - Create an order with `POST /orders` using only `productNumber` and `count`.
   - Confirm the created order belongs to the logged-in member.
   - Logout and confirm `POST /orders` fails with unauthorized error.
2. Refactor order cancellation to require login.
3. Verify that the order being cancelled belongs to the logged-in member.
4. Rename or add clearer cancel endpoint:

```text
POST /orders/{orderId}/cancel
```

5. Add BCrypt/password encoder after the session-based business flow is understood.
6. Learn and add tests later in a separate testing block.

### Notes for Future Chat

- The project now has working session-identity usage beyond simple login verification.
- `GET /members/me/orders` and `POST /orders` prove that the server can use session identity for business APIs.
- The next high-value backend concept is authorization at the business-operation level: users should only cancel their own orders.
- Do not recommend JWT next.
- Do not recommend tests immediately unless the user asks; tests are postponed by user preference.

---

## 14. Update Template

Use this template whenever the project state changes.

```md
## YYYY-MM-DD

### Completed
- 

### Decisions
- 

### Problems Found
- 

### Next
- 

### Notes for Future Chat
- 
```

---

#### 2026-06-23 Build/Test Verification Attempt

Task:

- Attempted to verify the latest pushed GitHub project with build/test execution.
- Direct `git clone` in the execution container failed because `github.com` could not be resolved.
- Fetched known source files individually through the GitHub connector and reconstructed the project under `/mnt/data/jpa-prac-manual`.
- Attempted to run Maven tests.

Result:

- `java` and `javac` 21 are available in the execution environment.
- `mvn` is not installed in the execution environment.
- Maven Wrapper exists in the repository, but the wrapper requires downloading Maven from `repo.maven.apache.org`, which is not reachable from the container environment.
- A true Maven `clean test` or `clean package` PASS/FAIL result could not be produced in this environment.
- The authoritative check must be run locally or in CI with network access.

Commands attempted:

```bash
git clone https://github.com/MinhyeokChoi99/jpa-prac.git /mnt/data/jpa-prac
cd /mnt/data/jpa-prac-manual && mvn -q test
```

Observed errors:

```text
fatal: unable to access 'https://github.com/MinhyeokChoi99/jpa-prac.git/': Could not resolve host: github.com
bash: mvn: command not found
```

Recommended local verification commands:

```bash
cd <project-root>
./mvnw clean test
./mvnw clean package
```

Windows PowerShell:

```powershell
cd <project-root>
./mvnw.cmd clean test
./mvnw.cmd clean package
```

Follow-up:

- Add GitHub Actions CI later so every push automatically runs `./mvnw clean test`.
- Keep `src/test/resources/application.properties` for H2-based test execution.
- If local tests fail, inspect whether `data.sql` is compatible with the generated H2 schema and whether `@Valid` on `List<OrderCreateRequest>` validates list elements as intended.
