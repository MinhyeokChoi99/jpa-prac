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

1. Add or improve global exception handling
2. Separate application configuration by profile
3. Improve order cancellation API
4. Add member signup
5. Add session-based login
6. Add authorization with roles
7. Prepare APIs for future React/Vue frontend separation

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

---

## 5. Completed Work

Record completed work here.

### Example Format

```text
Date: YYYY-MM-DD
Task:
- What was completed

Notes:
- Important implementation details
- Important design decisions
- Any issues found
```

### Entries

#### 2026-06-22

Task:
- Created initial `PROJECT_LOG.md` skeleton.

Notes:
- This file will be used to preserve project continuity across deleted or newly created chat sessions.

---

## 6. In Progress

Record the feature or refactoring currently being worked on.

### Current Task

```text
Task:
Status:
Related files:
Notes:
```

---

## 7. Next Tasks

Keep this list short and ordered.

```text
1. 
2. 
3. 
```

---

## 8. Important Design Decisions

Record decisions that should not be forgotten.

### Authentication

```text
Decision:
Reason:
Status:
```

### API Design

```text
Decision:
Reason:
Status:
```

### Package Structure

```text
Decision:
Reason:
Status:
```

### Database / Configuration

```text
Decision:
Reason:
Status:
```

---

## 9. Code Review Notes

Use this section when reviewing code.

### Critical

Issues that may cause runtime errors, wrong data, security problems, or production failure.

```text
- 
```

### Important

Issues that affect maintainability, scalability, or backend conventions.

```text
- 
```

### Optional

Useful improvements that are not urgent.

```text
- 
```

---

## 10. Current Domain Notes

### Member

```text
Current state:
Future direction:
Open questions:
```

### Product

```text
Current state:
Future direction:
Open questions:
```

### Orders

```text
Current state:
Future direction:
Open questions:
```

### OrderItem

```text
Current state:
Future direction:
Open questions:
```

---

## 11. Testing Notes

Record test coverage and future test targets.

### Existing Tests

```text
- 
```

### Recommended Tests

```text
- 
```

---

## 12. Blockers / Questions

Record anything that is currently unclear or blocking progress.

```text
- 
```

---

## 13. New Chat Handoff Summary

When starting a new chat session, paste or upload `PROJECT_CONTEXT.md` and this file.

Use this summary:

```text
This project is `jpa-prac`, a Java 21 + Spring Boot + MySQL practice project.
The goal is to grow it into a portfolio-level Spring web application.
The main domains are Member, Product, Orders, and OrderItem.

Use PROJECT_CONTEXT.md as the stable project reference.
Use PROJECT_LOG.md as the current progress and handoff record.

The assistant should act as a Spring/JPA teacher and code reviewer.
Explain concepts in Korean, provide Java/Spring examples when useful, and review code with production-readiness and portfolio-readiness in mind.

Current priority:
1. Add or improve global exception handling
2. Separate application profiles
3. Improve order cancellation API
4. Add signup
5. Add session-based login
6. Add authorization
7. Prepare for future React/Vue frontend separation
```

---

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
- `LoginServiceImpl` still has an unused `MemberService` import. Remove it for cleanliness.
- `LoginRequest` still has no validation annotations such as `@NotBlank` or `@Email`.
- Password comparison is still plain string comparison. This is acceptable only for the current learning step; later replace it with `PasswordEncoder.matches()`.
- `data.sql` role values are now quoted, but password seed values are still numeric-looking. Since the column is `varchar`, single-quoted password strings are cleaner: `'123'`, `'456'`, `'789'`.

### Next
1. Remove unused `MemberService` import from `LoginServiceImpl`.
2. Add validation to `LoginRequest`:
   - `@NotBlank`
   - `@Email`
3. Add `@Valid` to the login controller request parameter.
4. Implement current user lookup: `GET /members/me` or `GET /me`.
5. In `/members/me`, retrieve `SessionConst.LOGIN_MEMBER_ID` from `HttpSession` and load current `Member` from DB.
6. Add login/session tests using MockMvc.
7. Replace plain password comparison with BCrypt/password encoder after raw session flow is understood.
8. Later compare Spring Security session login, Redis Session, and JWT.

### Notes for Future Chat
- The user has now implemented the first working custom session login/logout stage.
- Future auth explanations should build from current concrete code:
  - `LoginController`
  - `LoginServiceImpl`
  - `LoginResponse`
  - `SessionConst`
- The next teaching step should probably be `/members/me`, because it proves that `JSESSIONID -> HttpSession -> login_member_id -> Member` works across requests.
- Do not jump directly to JWT unless the user explicitly changes the roadmap.

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
