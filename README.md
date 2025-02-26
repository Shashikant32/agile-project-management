# Agile Project Management Tool

## Project Overview
A comprehensive, cloud-based Agile project management application built with Spring Boot (Backend) and React.js (Frontend). 

## Features
- Role-based Access Control
- JWT Authentication
- Multi-Factor Authentication
- Adaptive Security
- Audit Logging
- Project, Task, and Sprint Management

## Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 13+
- Node.js 16+
- Git

## Backend Setup

### 1. Clone the Repository
```bash
git https://github.com/shashikant32/agile-project-management.git
cd agile-project-management/backend
```

### 2. Database Configuration
1. Install PostgreSQL
2. Create a database:
```sql
CREATE DATABASE agile_project_management;
CREATE USER agile_admin WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE agile_project_management TO agile_admin;
```

### 3. Configure Application Properties
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/agile_project_management
    username: agile_admin
    password: your_secure_password

  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: your-very-long-and-secure-secret-key
  expiration: 86400000  # 24 hours
```

### 4. Set Environment Variables (Optional but Recommended)
```bash
# Windows PowerShell
$env:JWT_SECRET = "your-secret-key"
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/agile_project_management"
$env:DATABASE_USERNAME = "agile_admin"
$env:DATABASE_PASSWORD = "your_secure_password"
```

### 5. Build and Run Backend
```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## Frontend Setup (React)

### 1. Navigate to Frontend Directory
```bash
cd ../frontend
```

### 2. Install Dependencies
```bash
npm install
```

### 3. Configure Environment
Create `.env` file:
```
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_JWT_SECRET=your-frontend-secret
```

### 4. Run Frontend
```bash
npm start
```

## API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/v3/api-docs`

## Authentication
1. Register a new user at `/api/auth/signup`
2. Login at `/api/auth/login`
3. Use JWT token for authenticated requests

## Security Features
- JWT Authentication
- Multi-Factor Authentication
- Role-Based Access Control
- Adaptive Authentication
- Audit Logging

## Development Tools
- Backend: Spring Boot, Spring Security, JPA
- Frontend: React.js, Axios
- Database: PostgreSQL
- Authentication: JWT, TOTP
- Documentation: Swagger/OpenAPI

## Deployment
- Backend: Spring Boot JAR
- Frontend: React Build
- Recommended: Docker Compose for containerization
