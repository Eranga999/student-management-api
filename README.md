# Student Management System Backend 

## Overview
This is a comprehensive REST API for managing students and courses, built with Spring Boot 3.5.6 and Firebase Firestore as the database. The system provides full CRUD operations for both Student and Course entities with proper error handling, input validation, and API documentation.

## Technology Stack
- **Java**: 21
- **Spring Boot**: 3.5.6
- **Database**: Google Firebase Firestore (NoSQL)
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Code Enhancement**: Lombok
- **Containerization**: Docker & Docker Compose
- **Monitoring**: Spring Boot Actuator
- **Testing**: Spring Boot Test, Postman

## Features
- ✅ Complete CRUD operations for Students
- ✅ Complete CRUD operations for Courses  
- ✅ Firebase Firestore integration
- ✅ Input validation with meaningful error messages
- ✅ Global exception handling
- ✅ Swagger API documentation
- ✅ Lombok integration for cleaner code
- ✅ Proper logging with SLF4J
- ✅ Asynchronous operations with CompletableFuture
- ✅ Pagination support for all list endpoints
- ✅ Docker containerization with multi-stage builds
- ✅ Spring Boot Actuator for health monitoring
- ✅ Multi-environment configuration (dev, docker, prod)
- ✅ Cross-platform deployment scripts

## Project Structure
```
student-management-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/studentmanagement/api/
│   │   │       ├── StudentManagementApiApplication.java
│   │   │       ├── config/
│   │   │       │   ├── FirebaseConfig.java
│   │   │       │   └── SwaggerConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── StudentController.java
│   │   │       │   └── CourseController.java
│   │   │       ├── dto/
│   │   │       │   ├── StudentRequestDto.java
│   │   │       │   ├── StudentResponseDto.java
│   │   │       │   ├── CourseRequestDto.java
│   │   │       │   ├── CourseResponseDto.java
│   │   │       │   ├── PageRequest.java
│   │   │       │   └── PageResponse.java
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── StudentNotFoundException.java
│   │   │       │   ├── CourseNotFoundException.java
│   │   │       │   └── ErrorResponse.java
│   │   │       ├── model/
│   │   │       │   ├── Student.java
│   │   │       │   └── Course.java
│   │   │       ├── repository/
│   │   │       │   ├── StudentRepository.java
│   │   │       │   └── CourseRepository.java
│   │   │       └── service/
│   │   │           ├── StudentService.java
│   │   │           └── CourseService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-docker.properties
│   │       ├── application-prod.properties
│   │       └── firebase-service-account.json
│   └── test/
│       └── java/
│           └── com/studentmanagement/api/
│               └── StudentManagementApiApplicationTests.java
├── target/
│   └── student-management-api-0.0.1-SNAPSHOT.jar
├── logs/
│   └── application.log
├── .mvn/
├── Docker Files:
├── Dockerfile
├── docker-compose.yml
├── docker-compose.prod.yml
├── .dockerignore
├── docker-run.bat
├── docker-run.sh
├── Configuration:
├── pom.xml
├── mvnw
├── mvnw.cmd
├── Documentation:
├── README.md
├── DOCKER.md
└── HELP.md
```

## API Endpoints

### Student Management

#### Create Student
- **POST** `/api/v1/student`
- **Request Body**:
```json
{
    "title": "Mr",
    "name": "Eranga harsha", 
    "address": "test",
    "city": "test",
    "course": "IT"
}
```
- **Response**: `201 CREATED` with created student details

#### Get Student by ID
- **GET** `/api/v1/student/{id}`
- **Response**: `200 OK` with student details

#### Get All Students
- **GET** `/api/v1/students` 
- **Response**: `200 OK` with list of all students

#### Get All Students (Paginated)
- **GET** `/api/v1/students/paginated`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0)
  - `size` (optional): Page size (default: 10)  
  - `sortBy` (optional): Field to sort by (default: "createdAt")
  - `sortDirection` (optional): ASC or DESC (default: DESC)
- **Example**: `/api/v1/students/paginated?page=0&size=5&sortBy=name&sortDirection=ASC`
- **Response**: `200 OK` with paginated student list

#### Update Student
- **PUT** `/api/v1/student/{id}`
- **Request Body**: Same as create student
- **Response**: `200 OK` with updated student details

#### Delete Student
- **DELETE** `/api/v1/student/{id}`
- **Response**: `204 NO CONTENT`

### Course Management

#### Create Course
- **POST** `/api/v1/courses`
- **Request Body**:
```json
{
    "name": "Introduction to Java",
    "fee": "299.99",
    "lecturerId": "abcde", 
    "lecturerName": "Dr. eranga"
}
```
- **Response**: `201 CREATED` with created course details

#### Get Course by ID
- **GET** `/api/v1/courses/{id}`
- **Response**: `200 OK` with course details

#### Get All Courses
- **GET** `/api/v1/courses`
- **Response**: `200 OK` with list of all courses

#### Get All Courses (Paginated)
- **GET** `/api/v1/courses/paginated`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0)
  - `size` (optional): Page size (default: 10)
  - `sortBy` (optional): Field to sort by (default: "createdAt")
  - `sortDirection` (optional): ASC or DESC (default: DESC)
- **Example**: `/api/v1/courses/paginated?page=1&size=5&sortBy=name&sortDirection=DESC`
- **Response**: `200 OK` with paginated course list

#### Update Course
- **PUT** `/api/v1/courses/{id}`
- **Request Body**: Same as create course
- **Response**: `200 OK` with updated course details

#### Delete Course
- **DELETE** `/api/v1/courses/{id}`
- **Response**: `204 NO CONTENT`

## Setup Instructions

### Prerequisites
- Java 21 or later
- Maven 3.8+
- Firebase project with Firestore enabled
- Firebase service account key

### 1. Firebase Setup
1. Create a Firebase project at https://console.firebase.google.com/
2. Enable Firestore Database
3. Generate a service account key:
   - Go to Project Settings → Service Accounts
   - Click "Generate new private key"
   - Save the JSON file as `firebase-service-account.json`

### 2. Configuration
1. Place `firebase-service-account.json` in `src/main/resources/`
2. Update `application.properties`:
```properties
server.port=8080
spring.application.name=student-management-api
logging.level.com.studentmanagement.api=DEBUG
```

### 3. Firestore Security Rules (for development)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

### 4. Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run

# Or create JAR and run
mvn clean package
java -jar target/student-management-api-0.0.1-SNAPSHOT.jar
```

### 4.1. Docker Deployment (Recommended)
```bash
# Prerequisites: Docker and Docker Compose installed
# Ensure firebase-service-account.json is in src/main/resources/

# Quick start with Docker
docker-run.bat          # Windows
./docker-run.sh         # Linux/Mac

# Or manually with docker-compose
docker-compose up --build

# Production deployment
docker-compose -f docker-compose.prod.yml up -d
```

For detailed Docker instructions, see [DOCKER.md](DOCKER.md)

### 5. Access the API
- **Application**: http://localhost:8080
- **Swagger Documentation**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health
- **Application Info**: http://localhost:8080/actuator/info

## Testing with Postman

### Environment Setup
Create a Postman environment with:
- `baseUrl`: `http://localhost:8080`

### Sample Requests

#### 1. Create Student
```
POST {{baseUrl}}/api/v1/student
Content-Type: application/json

{
    "title": "Ms",
    "name": "kamala",
    "address": "456 ", colombo
    "city": "colombo",
    "course": "Data Science"
}
```

#### 2. Get All Students
```
GET {{baseUrl}}/api/v1/students
```

#### 3. Create Course
```
POST {{baseUrl}}/api/v1/courses
Content-Type: application/json

{
    "name": "Advanced Java Programming",
    "fee": "233333333.99",
    "lecturerId": "rswre",
    "lecturerName": "Prof. Eranga"
}
```

#### 4. Get All Courses  
```
GET {{baseUrl}}/api/v1/courses
```

#### 5. Get Paginated Students
```
GET {{baseUrl}}/api/v1/students/paginated?page=0&size=3&sortBy=name&sortDirection=ASC
```

#### 6. Get Paginated Courses
```
GET {{baseUrl}}/api/v1/courses/paginated?page=0&size=2&sortBy=fee&sortDirection=DESC
```

## Error Handling
The API implements comprehensive error handling:

- **400 Bad Request**: Validation errors
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server errors

Example error response:
```json
{
    "error": "Validation failed",
    "message": "Name is required",
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/v1/student"
}
```

## Data Models

### Student Entity
```java
{
    "id": "string",
    "title": "string",
    "name": "string (required)",
    "address": "string (required)",
    "city": "string (required)", 
    "course": "string (required)",
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
}
```

### Course Entity
```java
{
    "id": "string",
    "name": "string (required)",
    "fee": "decimal (required)",
    "lecturerId": "string (required)",
    "lecturerName": "string (required)",
    "createdAt": "timestamp", 
    "updatedAt": "timestamp"
}
```

### Paginated Response Structure
```java
{
    "content": [
        // Array of Student or Course objects
    ],
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false,
    "hasNext": true,
    "hasPrevious": false
}
```

## Development Features

### Lombok Annotations Used
- `@Data`: Generates getters, setters, toString, equals, hashCode
- `@Builder`: Implements builder pattern
- `@NoArgsConstructor`: Generates no-argument constructor
- `@AllArgsConstructor`: Generates all-arguments constructor
- `@RequiredArgsConstructor`: Generates constructor for required fields
- `@Slf4j`: Provides logger instance

### Validation Annotations
- `@NotNull`: Field cannot be null
- `@NotBlank`: Field cannot be null or empty
- `@Valid`: Enables validation on request bodies

## Future Enhancements
- [ ] Search and filtering capabilities
- [ ] Student-Course relationship management
- [ ] User authentication and authorization
- [ ] Unit and integration tests
- [ ] Caching with Redis
- [ ] API versioning
- [ ] Advanced monitoring and metrics
- [ ] CI/CD pipeline integration

## Troubleshooting

### Common Issues
1. **Firebase Connection**: Ensure service account JSON is correct
2. **Port Conflicts**: Change server.port in application.properties
3. **Java Version**: Verify Java 21 is installed
4. **Firestore Rules**: Check security rules allow read/write

### Logs
Check application logs for detailed error information:
```bash
tail -f logs/application.log
```

## Author
 Eranga harsha

## License
This project is licensed under the MIT License.