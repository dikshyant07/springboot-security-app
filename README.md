# SpringBoot Security App

**Spring Boot authentication system** with JWT, refresh tokens, fine-grained roles & permissions, input validations, and unit tests using Mockito. Built with Spring Security, Spring Boot, MapStruct, and other modern Java technologies for secure and maintainable APIs.

---

## 🛠️ Technologies Used

- **Spring Boot** – Application framework  
- **Spring Security** – Authentication & Authorization  
- **JWT (JSON Web Tokens)** – Access & refresh tokens  
- **MapStruct** – DTO mapping  
- **Hibernate / JPA** – Database ORM  
- **PostgreSQL** – Relational database  
- **Validation** – Input validations with `jakarta.validation`  
- **Mockito & JUnit 5** – Unit testing and mocks  
- **Lombok** – Boilerplate reduction  

---

## ⚙️ Features

- JWT-based authentication with access and refresh tokens
- Fine-grained role-based authorization
- Permissions system for granular access control
- Input validation for secure API requests
- Unit tests for the service layer using Mockito
- Configurable profiles (`dev` and `prod`) with safe environment variables
- Pagination support for admin APIs (e.g., list users in pages)
- Sorting support for admin APIs (e.g., sort users by name, role, or creation date)
- Swagger integration for API testing and documentation
- Basic crud operations by admins.




## 🚀 Getting Started

### Prerequisites

- Java 17+  
- Maven 3.8+  
- PostgreSQL 12+  

### Setup

1. **Clone the repository**
```bash
git clone git@github.com:dikshyant07/springboot-security-app.git
cd springboot-security-app
 ```
2.📄 Configure application-prod.yml

Before running the project, open src/main/resources/application-prod.yml and update the default placeholder values with your local settings. 
```bash
logging:
  level:
    root: INFO

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: "*"

spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/security_project_db   # <-- your local DB name
    username: postgres                                           # <-- your DB username
    password: root                                               # <-- your DB password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

utils:
  admin:
    name: Admin Name
    age: 20
    gender: MALE
    email: admin@example.com            # <-- change to your email
    password: admin123                  # <-- change to your password

  manager:
    name: Manager Name
    age: 20
    gender: MALE
    email: manager@example.com          # <-- change to your email
    password: manager123                # <-- change to your password

  jwt:
    secret: change-this-secret-key     # <-- change to your secret key
    expiry: 30                          # <-- token expiry in minutes
```
### Instructions

1. Replace the database URL, username, and password with your local PostgreSQL settings.
2. Update admin/manager emails and passwords as needed.
3. Change the JWT secret to a strong random value.
4. Save the file.

## How to Access Swagger UI(For API testing and documentation)

1. Launch the Spring Boot application using:

```bash
./mvnw spring-boot:run
```
Open your web browser and navigate to:
```bash
http://localhost:8090/swagger-ui/index.html
```

