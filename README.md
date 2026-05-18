# StudyHub Backend

Backend API cho hệ thống chia sẻ tài liệu học tập **StudyHub** được xây dựng bằng Spring Boot.  
Hệ thống hỗ trợ quản lý tài liệu, người dùng, môn học, trường học và xác thực người dùng bằng JWT.

---

## Technologies

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT Authentication
- Maven
- RESTful API

---

## Features

### Authentication & User
- Register
- Login with JWT
- Role-based authorization
- User profile management

### Document
- Upload documents
- Download documents
- Search documents
- Filter by school and subject
- Bookmark documents
- Count downloads and views

### School & Subject
- Manage schools
- Manage subjects
- Link documents to schools and subjects

### Admin
- Manage users
- Manage documents
- Handle reported documents

---

## Installation

### 1. Clone repository

```bash
git clone https://github.com/your-username/studyhub-be.git](https://github.com/tranvanhuy-dev-it/StudyHub.git
```

---

### 2. Create database

```sql
CREATE DATABASE studyhub;
```

---

### 3. Configure application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studyhub
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

File location:

```text
src/main/resources/application.properties
```

---

### 4. Run project

Using Maven:

```bash
mvn spring-boot:run
```

Or run directly using IntelliJ IDEA / Eclipse.

---

## API Base URL

```text
http://localhost:8080/api
```

---

## Example Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Login |
| POST | `/auth/register` | Register |
| GET | `/documents` | Get documents |
| POST | `/documents/upload` | Upload document |
| GET | `/subjects` | Get subjects |

---

## Project Structure

```text
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.studyhub
 ┃ ┃    ┣ controller
 ┃ ┃    ┣ service
 ┃ ┃    ┣ repository
 ┃ ┃    ┣ entity
 ┃ ┃    ┣ dto
 ┃ ┃    ┣ security
 ┃ ┃    ┗ config
 ┃ ┗ resources
 ┃    ┗ application.properties
```

---

## JWT Authentication

After login success, API returns token:

```json
{
  "token": "your_jwt_token"
}
```

Use token in request header:

```http
Authorization: Bearer your_jwt_token
```

---

## Future Improvements

- Google OAuth2 Login
- AI document recommendation
- Comments & Ratings
- Realtime notifications
- Cloud storage
- Elasticsearch integration

---

## Author

Developed by Tran Van Huy
