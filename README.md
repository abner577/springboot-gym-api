# 🏋️ Spring Gym API

A robust backend API built with **Spring Boot** for managing gym operations involving **coaches**, **members**, and **workers**. 
Designed for extensibility, security, and clarity with layered architecture, custom authentication, and detailed documentation via **Swagger UI**.

---

## 🧭 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Authentication](#-authentication)
- [Getting Started](#-getting-started)
- [How to Use](#-how-to-use)
- [Swagger UI Screenshots](#-swagger-ui-preview)
- [Testing](#-testing)
- [Contact](#-contact)

---

## 🚀 Features
- Full **CRUD operations** for `Coach`, `Worker`, and `Member`
- Custom **authentication filters** using headers
- Separation of concerns with Controller → DTO → Service → Repository layers
- Swagger/OpenAPI 3.0 integration
- Integration + Unit Testing across layers
- Dockerized with PostgreSQL backend
---

## 🛠 Tech Stack

| Tool/Framework   | Purpose                         |
|------------------|---------------------------------|
| Java 17+         | Backend logic                   |
| Spring Boot      | Web framework                   |
| Spring Security  | Custom header-based auth        |
| Spring Data JPA  | ORM layer                       |
| PostgreSQL       | Relational database             |
| Swagger / OpenAPI| Interactive API docs            |
| JUnit / Mockito  | Testing                         |
| Docker Compose   | Container orchestration         |

---

## 📁 Project Structure
| Path                                     | Purpose / Contents                                               |
|------------------------------------------|------------------------------------------------------------------|
| `controller/`                            | Coach, Member, and Worker controllers                            |
| `dto/impl/`                              | DTO mappers                                                      |
| `dto/request/`                           | Models for handling POST and PATCH requests                      |
| `entity/`                                | JPA entity classes                                               |
| `repository/`                            | JpaRepository interfaces                                         |
| `service/`                               | Business logic per entity                                        |
| `security/filter/`                       | `CoachAuthFilter`, `WorkerAuthFilter`, `ValidRequestFilter`     |
| `security/token/`                        | Custom Auth tokens (`CoachAuthToken`, `WorkerAuthToken`)        |
| `security/provider/`                     | `WorkerAuthProvider` logic                                      |
| `security/SecurityConfig`                | Defines the security filter chain                                |
| `resources/application.yml`              | DB configuration & Spring profiles                               |
| `test/coach/`                            | Unit + integration tests for Coach endpoints                     |
| `test/member/`                           | Unit + integration tests for Member endpoints                    |
| `test/worker/`                           | Unit + integration tests for Worker endpoints                    |
| `test/testdata/`                         | Seed/mocked test data for integration tests                      |
| `test/SpringGymApiApplicationTests.java` | Verifies Spring context loads properly        


---

## 🔐 Authentication

This API uses **custom Spring Security filters** instead of OAuth or JWT.

| Operation Type | Required Headers               | Description                                           |
|----------------|--------------------------------|-------------------------------------------------------|
| `PUT`, `PATCH` | `x-coach-id`, `x-coach-code`   | Coach-only operations                                |
| `POST`, `DELETE` | `x-worker-id`, `x-worker-code` | Worker-only operations                              |
| Optional       | `x-valid`                      | Triggers `ValidRequestFilter` (must be `yes`)        |

Security is handled via:
- `CoachAuthToken`, `WorkerAuthToken`
- Manual injection into `SecurityContextHolder`
- Provider-based validation (`WorkerAuthProvider`)

---

## 🏁 Getting Started

### Prerequisites
Ensure you have the following installed:
- **Java 17+**
- **Maven 3.8+**
- **Docker & Docker Compose**
- **PostgreSQL** (optional if running DB via Docker)


---


## 🚀 How to Use

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/springboot-gym-api.git
cd springboot-gym-api
```

### 2️⃣ Set Environment Variables
Before starting the PostgreSQL service, set the following environment variables:

- **DB_URL** → JDBC connection URL for PostgreSQL (e.g., `jdbc:postgresql://localhost:5400/gymdb`)
- **DB_USERNAME** → Your PostgreSQL username
- **DB_PASSWORD** → Your PostgreSQL password

**Example (Linux/macOS):**
```bash
export DB_URL=jdbc:postgresql://localhost:5400/gymdb
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

**Example (Windows PowerShell):**
```powershell
setx DB_URL "jdbc:postgresql://localhost:5400/gymdb"
setx DB_USERNAME "postgres"
setx DB_PASSWORD "your_password"
```

> ⚠️ Ensure these variables are set **before** starting the PostgreSQL container.

### 3️⃣ Start Services with Docker
```bash
docker compose up --build
```
This will start:
- **PostgreSQL** database container
- **Spring Boot API** container

### 4️⃣ Access the API
Once running, the base API URL is:
```
http://localhost:8080/api/v1/gym-api
```

### 5️⃣ Stopping the Services
```bash
docker compose down
```

---

## 📄 Swagger UI Preview

The Gym API includes a **fully interactive Swagger UI** for exploring and testing endpoints directly from your browser.

You can access it after starting the application at:
```
http://localhost:8081/swagger-ui.html
```

---

### 🛠 How to Use Swagger UI

1. **Open Swagger UI**
    - Navigate to `http://localhost:8080/swagger-ui.html` in your browser.

2. **Locate Your Desired Endpoint**
    - Endpoints are grouped by controller (e.g., `Coach Controller`, `Member Controller`, `Worker Controller`).

3. **Authorize Requests (If Required)**
    - Some endpoints require authentication headers such as:
        - **`x-worker-id`**
        - **`x-worker-code`**
        - **`x-coach-id`**
        - **`x-coach-code`**
    - You can find these values in the `entityConfig` class within the source code, which contains seeded entity configurations for local testing.

4. **Input Request Parameters**
    - Click on the endpoint you want to test.
    - Fill in **path variables**, **query parameters**, and **request body** as needed.
    - For POST/PUT/PATCH requests, ensure the body matches the required DTO schema (visible in the `Schemas` section).

5. **Execute the Request**
    - Click the **"Try it out"** button.
    - Review the generated **cURL command** or raw request.
    - Press **"Execute"** to send the request.
    - Swagger UI will display:
        - **Request URL**
        - **Response status code**
        - **Response body**
        - **Response headers**

---

### 💻 Example cURL Request
```bash
curl -X GET "http://localhost:8081/api/v1/gym-api/coaches/1" \
     -H "x-coach-id: 1" 
```

---

### 📸 Swagger UI Screenshots

#### 🧑‍🏫 Coach Controller
![Coach Controller](./docs/Coach%20Controller.png)

#### 🧑‍💼 Member Controller
![Member Controller](./docs/Member%20Controller.png)

#### 🧑 Worker Controller
![Worker Controller](./docs/Worker%20Controller.png)

#### 📦 Schemas
![Schemas](./docs/Schemas.png)

#### ✅ Coach GET Success
![Coach GET Success](./docs/Coach%20GET%20Sucess.png)

#### ❌ Coach GET Failure
![Coach GET Failure](./docs/Coach%20GET%20Failure.png)

---

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run only integration tests:
```bash
mvn verify -DskipUnitTests=true
```

---

## 📬 Contact

For questions, suggestions, or contributions, please reach out via:

- **Email:** abner07282005@gmail.com
- **GitHub:** [abner577](https://github.com/abner577)
- **LinkedIn:** [Abner Rodriguez](https://linkedin.com/in/abner-rodriguez-)

---
