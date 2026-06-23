# PROJECT_CONTEXT.md

# Project Context: jpa-prac

## 1. Project Identity

- Repository: `https://github.com/MinhyeokChoi99/jpa-prac`
- Project name: `jpa-prac`
- Main package: `kr.co.prac`
- Current purpose: Java + Spring Boot + JPA + MySQL practice project
- Long-term purpose: Grow into a portfolio-level and production-minded Spring web application
- Main domains: `member`, `product`, `orders`, `orderItem`
- User language: Korean
- Preferred explanation style: Korean, step-by-step, concept-first, with Java/Spring code examples
- Documentation language: English is acceptable for technical reuse

This project is not a simple CRUD-only exercise. It is being built gradually to understand Spring MVC, JPA, transaction boundaries, exception handling, validation, authentication, authorization, and later deployment-oriented backend concerns.

---

## 2. User Intent and Assistance Style

The user wants to learn by implementing and then reviewing code. When helping with this project:

- Check the latest GitHub project state when code review is requested.
- Explain not only *what code to write*, but *why the structure exists*.
- Distinguish between compile/runtime correctness, Spring convention, maintainability, and production-readiness.
- Avoid jumping too far ahead before the user understands the current layer.
- Give direct feedback when code is wrong, but explain the exact reason.
- Prefer Korean explanations with concrete project-specific examples.

The user is currently focused on building backend fundamentals in this order:

```text
JPA/domain structure -> DTO/API -> exception handling -> validation -> session login -> JWT later
```

---

## 3. Confirmed / Expected Tech Stack

Current stack:

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Data JDBC dependency appears in stack
- Bean Validation
- MySQL runtime DB
- H2 test DB in MySQL compatibility mode
- Lombok
- Springdoc OpenAPI / Swagger
- Maven / Maven Wrapper

Potential future stack:

- Spring Security
- HttpSession + cookie-based login
- JWT authentication after session flow is understood
- Spring Boot profiles
- Flyway or Liquibase
- Redis / Spring Session Redis
- QueryDSL
- Docker
- GitHub Actions
- Testcontainers
- Cloud deployment
- Nginx
- Monitoring/logging tools

---

## 4. Current Repository State Summary

Current domains:

```text
member
product
orders
orderItem
```

Current implemented directions:

- Feature-based package structure exists.
- JPA auditing timestamps exist through `BaseTimeEntity`.
- `GlobalExceptionHandler` exists.
- `ErrorCode`, `ErrorResponse`, and `BusinessException` exist.
- Domain-specific custom exceptions exist.
- Custom exception class names now use the `Exception` suffix.
- `MethodArgumentNotValidException` handling exists.
- Order creation uses validation on list elements with `List<@Valid OrderCreateRequest>`.
- Product stock decreases when an order is created.
- Product stock is restored when an order is cancelled.

Current caution:

- Real Maven build/test could not be verified inside the assistant runtime because direct GitHub clone and Maven execution were limited by environment constraints.
- User should run local build/test and provide output if errors occur.

Recommended local commands:

```bash
./mvnw clean test
./mvnw clean package
```

Windows PowerShell:

```powershell
./mvnw.cmd clean test
./mvnw.cmd clean package
```

---

## 5. Current Domain Understanding

### Member

Member represents a user/customer-like entity.

Current/future meaning:

- Current member domain handles member data.
- Member will become the authentication subject later.
- Email should be treated as a login identifier.
- Password should be added when signup/login starts.
- Role should be added when authorization starts.

Future fields likely needed:

```text
id / number
name
email
password
role
createdAt
updatedAt
```

Future member-related APIs may include:

```text
POST /members/signup or POST /auth/signup
POST /members/login or POST /auth/login
POST /members/logout or POST /auth/logout
GET  /members/me or GET /me
```

---

### Product

Product represents an item that can be ordered.

Known/expected behavior:

- Product list retrieval
- Stock decrease during order creation
- Stock increase during order cancellation
- Not enough stock exception when requested count exceeds stock
- Product not found exception when order references missing product

Future direction:

- Admin product CRUD
- Product detail API
- Product search/filtering
- Category
- Image support later
- Safer stock mutation through domain methods only

---

### Orders

Orders represents a purchase/order transaction.

Known/expected behavior:

- Order list retrieval
- Order detail retrieval
- Order creation
- Order cancellation
- Member-specific order lookup
- Status transition validation

Current improvement target:

```text
Current cancellation endpoint may be ambiguous.
Recommended endpoint: POST /orders/{orderId}/cancel
```

Reason:

```text
Cancelling an order is a domain state transition, not physical deletion.
```

---

### OrderItem

OrderItem connects an order with a product.

Known/expected behavior:

- Created when order is created
- Contains product reference, order price, and count
- Participates in stock calculation and order details

Future direction:

- Avoid exposing entity graph directly
- Use response DTOs
- Watch for lazy loading and N+1 problems

---

## 6. Current Architecture

Current architecture follows layered architecture inside feature-based packages:

```text
Controller
↓
Service
↓
Repository
↓
Entity / Database
```

Current package direction:

```text
kr.co.prac
├── global
│   ├── config
│   ├── entity
│   └── exception
├── member
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── exception
├── product
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── exception
├── orders
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── exception
└── PracApplication.java
```

Recommended future additions:

```text
kr.co.prac
├── auth
│   ├── controller
│   ├── service
│   ├── dto
│   └── security
└── global
    ├── response
    └── security/config if needed
```

---

## 7. Exception Handling Status

Exception handling is mostly stabilized.

Current global exception structure:

```text
global.exception
├── ErrorCode.java
├── ErrorResponse.java
├── BusinessException.java
└── GlobalExceptionHandler.java
```

Current intended flow:

```text
Domain/service detects business failure
→ throw DomainSpecificException
→ DomainSpecificException extends BusinessException
→ BusinessException holds ErrorCode
→ GlobalExceptionHandler catches BusinessException
→ ErrorResponse returned with ErrorCode status/code/message
```

Current domain-specific exception naming is now conventionally clearer, using the `Exception` suffix:

```text
member.exception
├── MemberNotFoundException
└── AlreadyExistMemberException

product.exception
├── ProductNotFoundException
└── NotEnoughStockException

orders.exception
├── EmptyItemOrderException
├── OrderNotFoundException
└── AlreadyCancelledOrderException
```

Current known improvement:

- Generic fallback logging should preserve stack trace.
- Prefer:

```java
log.error("Unexpected exception occurred", e);
```

instead of only logging a plain message.

Validation handling exists, but later it can be improved with field-level error details.

---

## 8. Validation Direction

Current validation direction:

- `MethodArgumentNotValidException` is handled globally.
- `INVALID_INPUT_VALUE` exists in `ErrorCode`.
- Order creation request uses list element validation.

Future validation response improvement:

Current simple response is acceptable now:

```json
{
  "status": 400,
  "code": "INVALID_INPUT_VALUE",
  "message": "잘못된 입력값입니다."
}
```

Later, field-level details may be added:

```json
{
  "status": 400,
  "code": "INVALID_INPUT_VALUE",
  "message": "잘못된 입력값입니다.",
  "errors": [
    { "field": "email", "message": "must not be blank" }
  ]
}
```

Do not overcomplicate validation response until the basic API and auth flow are stable.

---

## 9. Git Workflow Notes

The user recently studied Git fetch/pull/merge/push.

Core mental model:

```text
fetch = update remote-tracking information only
pull = fetch + merge/rebase into current local branch
push = upload local commits to remote
merge = merge another branch into current branch
Pull Request = GitHub workflow to merge one branch into another, usually feature branch into main
```

For pull:

```text
current local branch ← origin/<specified branch>
```

Example:

```bash
git switch branch1
git pull origin main
```

means:

```text
branch1 ← origin/main
```

It does not mean switching to `main`.

Safe update routine:

```bash
git status
git fetch origin
git branch -vv
git pull
```

If local changes exist, commit or stash before pulling.

---

## 10. Current Code Review Priorities

### Critical

Issues that may cause runtime errors, data inconsistency, security problems, or production failure:

- Unsafe authentication logic
- Plaintext password storage once auth starts
- Missing transaction boundary
- Incorrect stock handling
- Exposing sensitive user data
- Incorrect JPA relationship mapping
- Data loss caused by `ddl-auto=create`
- Missing validation for important requests

### Important

Issues that affect maintainability, scalability, or backend conventions:

- Inconsistent API response format
- Missing field-level validation error response
- Service methods doing too many things
- No pagination for list APIs
- Possible N+1 query problems
- Weak test coverage
- Ambiguous order cancellation endpoint
- Logging unexpected exceptions without stack trace

### Optional

Useful but not urgent:

- More detailed Swagger documentation
- Custom response wrapper
- More expressive method names
- Better frontend UI
- Dockerization
- CI/CD
- QueryDSL
- Monitoring

---

## 11. Immediate Next Tasks Before Authentication

Before starting authentication, recommended cleanup:

```text
1. Run local ./mvnw clean test and ./mvnw clean package.
2. Improve fallback exception logging with stack trace.
3. Improve order cancellation endpoint later: POST /orders/{orderId}/cancel.
4. Separate application profiles later.
5. Add focused tests for exception and order behavior.
```

Recommended tests before auth:

```text
- Member duplicate email exception
- Member not found exception
- Product not found exception
- Not enough stock exception
- Empty order request exception
- Order cancellation stock restoration
- Already cancelled order exception
- Validation failure response
```

---

## 12. Authentication Roadmap: Session First, JWT Later

The user has decided to learn and implement authentication in this order:

```text
1단계: 기본 HttpSession으로 로그인 흐름 이해
2단계: session timeout, cookie, logout 이해
3단계: multi-server 문제 이해
4단계: Redis Session 또는 JWT 비교
```

Final decision:

```text
Session first → JWT later
```

Do not jump directly to JWT before the user understands cookie-based server sessions and their scaling limitations.

Spring Security may be introduced during the session stage, but explanations must connect Spring Security behavior to:

```text
HttpSession
JSESSIONID
SecurityContext
Authentication
cookie behavior
session timeout
logout
```

---

### Stage 1: Basic `HttpSession` Login Flow

Goal: understand how server-side login state works.

Concepts:

```text
HttpSession
JSESSIONID cookie
server-side session storage
login success flow
login-required endpoint flow
where login state is stored
```

Basic mental model:

```text
Browser cookie:
JSESSIONID=abc123

Server session store:
abc123 -> LOGIN_MEMBER_ID = 1
```

If Spring Security is used:

```text
Browser cookie:
JSESSIONID=abc123

Server session store:
abc123 -> SPRING_SECURITY_CONTEXT -> Authentication -> Principal/UserDetails
```

Implementation direction:

```text
1. Add password to Member.
2. Add signup request DTO.
3. Add login request DTO.
4. Signup stores encoded password.
5. Login verifies email/password.
6. On login success, create HttpSession or SecurityContext.
7. Add /members/me or /me endpoint.
```

---

### Stage 2: Session Timeout, Cookie, and Logout

Goal: understand how session lifetime and cookie behavior affect login state.

Concepts:

```text
server session timeout
browser session cookie
JSESSIONID
HttpOnly
Secure
SameSite
session.invalidate()
logout cookie deletion
```

Recommended configuration example:

```yaml
server:
  servlet:
    session:
      timeout: 30m
      cookie:
        name: JSESSIONID
        http-only: true
        same-site: lax
```

Important distinction:

```text
Cookie timeout controls how long the browser keeps the cookie.
Server session timeout controls how long the server keeps the login state.
```

In the basic session model, the cookie does not store member information. It only stores the session id.

---

### Stage 3: Multi-server Session Problem

Goal: understand why default in-memory sessions become problematic when there are multiple Tomcat/application servers.

Problem model:

```text
Login request -> Tomcat A
Tomcat A memory: abc123 -> memberId=1

Next request -> Tomcat B
Tomcat B memory: no abc123 session

Result: user may appear logged out.
```

Concepts:

```text
in-memory session limitation
load balancer
sticky session
session replication
external session store
why server-local session storage does not scale cleanly
```

---

### Stage 4: Redis Session or JWT Comparison

Goal: compare common ways to handle authentication after the basic session model is understood.

#### Redis-backed session

Redis is useful because session data is short-lived key-value state.

```text
Browser cookie:
JSESSIONID=abc123

Redis:
abc123 -> memberId=1, TTL=30m

Tomcat A and Tomcat B both read Redis.
```

Why Redis instead of RDBMS for session:

```text
Session lookup is key-value based.
Session data is temporary.
TTL expiration is natural in Redis.
Redis keeps session I/O separate from core business RDBMS load.
RDBMS session is possible, but generally heavier for frequent session read/write traffic.
```

#### JWT

JWT should be studied after sessions.

Mental model:

```text
Session:
Server remembers login state.
Client stores only session id.

JWT:
Client stores signed token.
Server validates token instead of looking up server-side session state for every request.
```

JWT topics to study later:

```text
access token
refresh token
stateless authentication
token expiration
token storage risk
refresh token rotation
logout difficulty with stateless tokens
Redis use for refresh token storage or blacklist
```

---

## 13. Authentication Implementation Recommendation

Recommended implementation order:

```text
1. Run local build/test first.
2. Improve fallback exception logging.
3. Add Member password field.
4. Implement signup.
5. Implement basic session login.
6. Confirm JSESSIONID cookie in browser/dev tools or API client.
7. Implement /me.
8. Implement logout.
9. Configure session timeout and cookie options.
10. Study multi-server in-memory session limitation.
11. Compare Redis Session and JWT.
12. Implement JWT only after session flow is clear.
```

Preferred next concrete feature:

```text
Cookie-based Session login using HttpSession first, then JWT later.
```

Spring Security decision:

- Spring Security can be introduced during the session-login stage.
- But the assistant must explain how Spring Security uses session/cookie internally.
- Avoid introducing JWT before the session model is clear.

---

## 14. Handoff Summary for New Chat

Use this summary when continuing the project:

```text
This project is `jpa-prac`, a Java 21 + Spring Boot + MySQL practice project.
The goal is to grow it into a portfolio-level Spring web application.
The main domains are Member, Product, Orders, and OrderItem.

Use PROJECT_CONTEXT.md as the stable project reference.
Use PROJECT_LOG.md as the progress and handoff record.

The assistant should act as a Spring/JPA teacher and code reviewer.
Explain concepts in Korean, provide Java/Spring examples when useful, and review code with production-readiness and portfolio-readiness in mind.

Current repository status:
- Feature-based package structure exists.
- JPA auditing timestamps exist.
- Global exception handling exists.
- Domain-specific exceptions exist and use the `Exception` suffix.
- `MethodArgumentNotValidException` handler exists.
- Fallback logging exists but should include the exception object for stack trace preservation.
- Build/test could not be executed inside the assistant environment due DNS/Maven availability limits; user should run `./mvnw clean test` locally.

Current priority:
1. Run and review real local build/test output.
2. Improve fallback logging with stack trace preservation.
3. Add focused tests around exceptions/order behavior.
4. Start authentication with Session first, JWT later.

Authentication roadmap:
1단계: 기본 HttpSession으로 로그인 흐름 이해
2단계: session timeout, cookie, logout 이해
3단계: multi-server 문제 이해
4단계: Redis Session 또는 JWT 비교
```

