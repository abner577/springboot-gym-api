# 🏋️ Spring Boot Gym API

A RESTful Gym Management API built with Spring Boot, Docker, and PostgreSQL. 
This project manages coaches, members, and workers, each with distinct roles and functionality, with plans for role-based authentication and security integration.

---

## Project Structure

src
└── main
├── java
│ └── com.yourpackage
│ ├── config # Configuration classes (CommandLineRunners, ModelMapper, etc.)
│ ├── controller # REST controllers
│ ├── entity # JPA entity classes
│ ├── entity.enums # Enum definitions (e.g., Role)\
│ ├── dto # DTO clases
│ ├── dto.impl # DTO clases
│ ├── repository # Spring Data JPA repositories
│ └── service # Business logic 
│ └── service.impl # Business logic 
└── resources
└── application.properties

---

## Features

- CRUD support for 'Member', 'Coach', and 'Worker'
- Role-based design using enum values ('ROLE_MEMBER', 'ROLE_COACH', 'ROLE_WORKER')
- PostgreSQL integration via Docker
- Entity seeding with CommandLineRunner
- Modular and extensible project structure

---

## Tech Stack

- **Java 21** (with OpenJDK)
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL** (via Docker)
- **Docker & Docker Compose**
