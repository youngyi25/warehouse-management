# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a warehouse management system with a Spring Boot backend (Java 11) and Vue 2 frontend. The system manages product inventory, inbound/outbound operations, warehouses, locations, and user roles.

## Technology Stack

### Backend
- **Framework**: Spring Boot 2.7.18
- **Language**: Java 11
- **Build**: Maven 3.6+
- **ORM**: MyBatis-Plus 3.5.3.1
- **Database**: MySQL 8.0+ with Druid connection pool
- **Cache**: Redis
- **Security**: Spring Security + JWT
- **API Docs**: Knife4j (Swagger UI at /doc.html)
- **Utils**: Hutool, Lombok

### Frontend
- **Framework**: Vue 2.6.14
- **UI**: Element UI 2.15.13
- **Router**: Vue Router 3.5.1
- **State**: Vuex 3.6.2
- **HTTP**: Axios 0.27.2
- **Build**: Vue CLI 5.0

## Development Commands

### Backend (Maven)
```bash
# Compile the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Run tests
mvn test

# Package for production
mvn clean package
```

### Frontend (npm)
```bash
# Install dependencies
cd frontend
npm install

# Start development server
npm run serve

# Build for production
npm run build

# Lint code
npm run lint
```

## Database Setup

1. Create database and tables:
```bash
mysql -u root -p < sql/init.sql
```

2. Insert sample data (optional):
```bash
mysql -u root -p warehouse_db < sql/insert_sample_data.sql
```

## Architecture Overview

### Backend Structure
- **Controllers**: REST API endpoints in `src/main/java/com/warehouse/controller/`
- **Services**: Business logic in `src/main/java/com/warehouse/service/`
- **Mappers**: Data access layer using MyBatis-Plus
- **Entities**: JPA entities in `src/main/java/com/warehouse/entity/`
- **Config**: Configuration classes for security, Redis, MyBatis, etc.
- **Common**: Shared classes (Result, PageResult, GlobalExceptionHandler)

### Key Business Entities
- **Product**: Product information with categories
- **Inventory**: Stock levels by warehouse/location
- **InboundOrder**: Purchase returns transfer inbound
- **OutboundOrder**: Sales transfer scrap outbound
- **Warehouse**: Storage locations
- **Location**: Specific storage positions
- **Category**: Product categorization (tree structure)
- **User/Role**: Authentication and authorization

### Frontend Structure
- **Views**: Page components in `src/views/`
- **Router**: Route definitions
- **Store**: Vuex state management
- **Utils**: Authentication and API utilities

## API Documentation

- Swagger UI: http://localhost:8080/doc.html
- API Docs: http://localhost:8080/v3/api-docs

## Authentication

- JWT-based authentication
- Default admin account: admin/admin123
- Token must be included in Authorization header: `Bearer <token>`

## Database Configuration

Edit `src/main/resources/application.yml` to update:
- MySQL connection details (lines 9-11)
- Redis connection (lines 24-34)
- JWT secret (line 51)

## Frontend Configuration

Edit `frontend/vue.config.js` to:
- Change proxy target if backend is not on 8080
- Modify port (default 8081)

## Important Notes

1. The backend runs on port 8080 by default
2. Frontend runs on port 8081 by default
3. All database operations use MyBatis-Plus with automatic CRUD generation
4. Soft delete is implemented using `deleted` field
5. Global exception handling is in `GlobalExceptionHandler`
6. CORS is configured for frontend-backend communication

## Testing

Run the application and test endpoints:
- Health check: http://localhost:8080/actuator/health
- Login: POST /api/auth/login with credentials
- API docs: http://localhost:8080/doc.html

## Common Development Patterns

1. **CRUD Operations**: Use MyBatis-Plus BaseMapper for basic operations
2. **API Responses**: Use `Result` wrapper for consistent response format
3. **Pagination**: Use `PageResult` for paginated responses
4. **Validation**: Use `@Valid` annotations for request validation
5. **Logging**: Use SLF4J for logging in services