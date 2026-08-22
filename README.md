# 🛒 Spring Boot E-Commerce Application

A RESTful E-Commerce backend application built using **Java, Spring Boot, Spring Data JPA, Hibernate, and MySQL**.

The application provides APIs for managing products, categories, users, carts, and other e-commerce operations. It follows a layered architecture and uses DTOs, exception handling, pagination, sorting, and API documentation with Swagger/OpenAPI.

---

## 🚀 Features

- Product management
- Category management
- User management
- Shopping cart management
- Add products to cart
- Update product quantity in cart
- Remove products from cart
- Product and category relationships
- JPA/Hibernate entity mapping
- RESTful APIs
- DTO-based request/response handling
- Global exception handling
- Custom exceptions
- HTTP status code handling
- Pagination
- Sorting
- JPQL queries
- Custom repository queries
- Case-insensitive search
- Swagger/OpenAPI API documentation

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| MySQL | Relational database |
| Maven | Build and dependency management |
| ModelMapper | Entity-DTO mapping |
| Swagger / OpenAPI | API documentation and testing |
| IntelliJ IDEA | Development environment |

---

## 🏗️ Project Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
