# 🎯 End Goal: Secure Backend Auth System with Real-World Defenses

## ✅ Final Outcome

You will build a **Spring Boot-based secure backend system** that uses:

| Feature                           | Description                                                                       |
| --------------------------------- | --------------------------------------------------------------------------------- |
| ✅ **JWT Authentication**          | Secure, stateless login system using industry-standard token handling.            |
| ✅ **Role-based Authorization**    | Fine-grained access control at method-level using `@PreAuthorize`.                |
| ✅ **Brute-force Protection**      | Defense against login abuse using rate limiting and BCrypt hashing.               |
| ✅ **Database Security Practices** | Users stored securely in **PostgreSQL running in Docker**, with hashed passwords. |
| ✅ **Token Expiry & Refresh**      | Stateless access tokens (short-lived), optional refresh flow (bonus)              |
| ✅ **Dockerized Local Dev Setup**  | PostgreSQL will be containerized, optionally Spring Boot as well.                 |
| ✅ **Secure Spring Config**        | Secret keys managed via environment variables or config vaults (prod-ready).      |

---

## 🧱 System Architecture Overview

```text
             +-------------+        +----------------+
             |  Client UI  |        |   Brute Force  |
             |  (Postman / | -----> |  Defender      |
             |  React)     |        +----------------+
             +------+------+       
                    |
                    v
         +-----------------------+
         |   Spring Boot App     |   <-- JWT Filter + Security Layer
         |  - /login             |
         |  - /admin             |
         |  - /me                |
         +----------+------------+
                    |
                    v
         +-----------------------+
         |   PostgreSQL DB       |   <-- User table with roles + BCrypt passwords
         |   (Dockerized)        |
         +-----------------------+
```

---

## 📦 Environment Setup Plan

| Component         | How it will be run                                |
| ----------------- | ------------------------------------------------- |
| ✅ Spring Boot App | Local dev on IntelliJ or Docker                   |
| ✅ PostgreSQL      | Docker container with persistent volume           |
| ✅ DB Init         | `data.sql` or liquibase/flyway for user bootstrap |
| ✅ Secrets         | `application.yml` → later moved to env vars       |
| ✅ Testing         | Postman, curl, `MockMvc`, and JUnit               |

---

## 🛡️ Security Objectives

| Goal                                     | How We'll Achieve                                  |
| ---------------------------------------- | -------------------------------------------------- |
| **Protect sensitive data**               | Store passwords with `BCrypt` in PostgreSQL        |
| **Prevent credential leaks**             | Never store plain passwords; no sensitive logs     |
| **Prevent replay attacks**               | Use **short-lived JWT** tokens with expiry         |
| **Prevent brute-force attacks**          | Implement **IP + username-based rate limiting**    |
| **Enforce principle of least privilege** | Use **role-based method access** (`@PreAuthorize`) |

---
## 🧪 Functional Scope for Hands-on

| API Endpoint  | Purpose                     | Auth Required? | Roles   |
| ------------- | --------------------------- | -------------- | ------- |
| `POST /login` | Authenticate and return JWT | ❌              | N/A     |
| `GET /me`     | Return user details         | ✅              | Any     |
| `GET /admin`  | Admin-only endpoint         | ✅              | `ADMIN` |

Later we can add:

* `POST /refresh` for token refresh
* `POST /register` to onboard new users securely

---

## ✅ Summary of Next Steps

| Phase     | Goal                                          |
| --------- | --------------------------------------------- |
| ✅ Step 0  | Define dependencies and architecture (✅ done) |
| 🔜 Step 1 | Build `UserEntity` and `UserRepository`       |
| 🔜 Step 2 | Build `UserService` with BCrypt validation    |
| 🔜 Step 3 | Build JWT Generation (`JWTService`)           |
| 🔜 Step 4 | Add `JWTAuthenticationFilter`                 |
| 🔜 Step 5 | Configure `SecurityConfig` with roles + paths |
| 🔜 Step 6 | Add Docker PostgreSQL setup                   |
| 🔜 Step 7 | Add Brute-force protection + logs + expiry    |

---

Would you like to:

1. 🔨 Start **Step 1: UserEntity + Repository**
2. 🐳 Setup **Docker PostgreSQL and connect Spring Boot**
3. ⚙️ Explore **Brute-force attack logic + protection design**

🔐 Step 2: UserService with BCrypt Password Checking


🛡️ Step 6: Add Brute Force Protection using Rate Limiting (Spring Boot + Bucket4j)

| Risk                             | Protection                                             |
| -------------------------------- | ------------------------------------------------------ |
| 🚨 Bruteforce login attacks      | 🛡️ Limit failed login attempts per IP or per username |
| ⚠️ Bots/scripted abuse           | 🛡️ Slow down or block suspicious sources              |
| 🧪 Transparent retry after limit | ✅ Standard `429 Too Many Requests` response            |


