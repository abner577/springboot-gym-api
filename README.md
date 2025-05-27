# 🏋️ Spring Boot Gym API

A RESTful Gym Management API built with Spring Boot, Docker, and PostgreSQL. 
This project manages coaches, members, and workers, each with distinct roles and functionality, with plans for role-based authentication and security integration.

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
