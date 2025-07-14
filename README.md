# WhatsApp Messenger Backend

A comprehensive WhatsApp-like chat backend built with Spring Boot, featuring real-time messaging, file sharing, user authentication, and Kafka message queueing for scalable event-driven architecture.

## 🚀 Features

### Core Messaging
- **1-on-1 Chatrooms**: Private conversations between two users
- **Text Messages**: Send and receive text messages
- **File Attachments**: Support for images and videos (up to 10MB)
- **Message Editing**: Edit messages within 15 minutes of sending
- **Message Deletion**: Delete your own messages
- **Message Search**: Search messages within chatrooms
- **Message Reactions**: Emoji reactions (limited to 4 types)

### User Management
- **User Registration**: Simple registration with email verification
- **User Authentication**: JWT-based authentication
- **User Profiles**: Manage profile information, status, and avatar
- **User Status**: Online, offline, away, busy status tracking
- **User Search**: Find users by username

### Real-time Features
- **Kafka Integration**: Event-driven architecture for scalability
- **Message Events**: Real-time message delivery and status updates
- **User Events**: Online/offline status, profile updates
- **Notification Events**: Push notifications, email alerts
- **Event Consumers**: Process events for analytics and notifications

### Advanced Features
- **File Upload**: Secure file handling with validation
- **Pagination**: Efficient message retrieval with pagination
- **API Documentation**: Complete Swagger/OpenAPI documentation
- **Error Handling**: Comprehensive exception handling
- **Logging**: Detailed logging for debugging and monitoring

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.5.0
- **Language**: Java 21
- **Database**: PostgreSQL with JPA/Hibernate
- **Message Queue**: Apache Kafka
- **Authentication**: JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## 📁 Project Structure

```
src/
├── main/java/com/whatsapp/
│   ├── config/           # Configuration classes
│   ├── controller/       # REST API controllers
│   ├── dto/             # Data Transfer Objects
│   ├── exception/       # Custom exceptions
│   ├── model/           # JPA entities
│   ├── repository/      # Data access layer
│   ├── service/         # Business logic layer
│   └── util/            # Utility classes
├── resources/
│   └── application.properties
└── test/                # Test classes
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Apache Kafka 2.8+

### 1. Clone and Setup
```bash
git clone <repository-url>
cd whatsapp-messenger-backend
```

### 2. Database Setup
```sql
CREATE DATABASE whatsapp_chat;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE whatsapp_chat TO postgres;
```

### 3. Kafka Setup
```bash
# Start Kafka (using Docker)
docker run -p 9092:9092 apache/kafka:2.13-3.4.0
```

### 4. Application Configuration
Update `src/main/resources/application.properties`:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/whatsapp_chat
spring.datasource.username=postgres
spring.datasource.password=password

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
```

### 5. Run Application
```bash
mvn spring-boot:run
```

### 6. Access APIs
- **Swagger UI**: http://localhost:8090/api/swagger-ui.html
- **API Base URL**: http://localhost:8090/api

## 🌐 Free Deployment

### Quick Deploy to Railway (Recommended)

1. **Push to GitHub**: Ensure your code is in a GitHub repository
2. **Sign up at Railway**: [railway.app](https://railway.app)
3. **Deploy**: Connect your GitHub repo and Railway will auto-deploy
4. **Add PostgreSQL**: Railway will auto-provision a database
5. **Set Environment Variables**:
   ```
   JWT_SECRET=your-super-secure-jwt-secret-key-here
   PORT=8090
   ```

### Alternative Platforms
- **Render**: [render.com](https://render.com) - 750 hours/month free
- **Fly.io**: [fly.io](https://fly.io) - 3 VMs free

### Deployment Files Included
- `railway.json` - Railway configuration
- `nixpacks.toml` - Build configuration  
- `Procfile` - Process definition
- `Dockerfile` - Container configuration
- `DEPLOYMENT.md` - Detailed deployment guide

**Note**: Free tiers don't support Kafka. The app will work without Kafka (event publishing disabled).

## �� API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "priya_sharma",
  "password": "securepassword",
  "email": "priya@example.com",
  "fullName": "Priya Sharma",
  "phoneNumber": "+919876543210"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "priya_sharma",
  "password": "securepassword"
}
```

### User Management

#### Get User Profile
```http
GET /api/users/{userId}
Authorization: Bearer <jwt-token>
```

#### Update User Profile
```http
PUT /api/users/{userId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "fullName": "Priya Sharma Updated",
  "bio": "Updated bio",
  "status": "ONLINE"
}
```

### Messaging

#### Send Text Message
```http
POST /api/messages/text?chatroomId=1&senderId=1&content=Namaste! How are you?
Authorization: Bearer <jwt-token>
```

#### Send Message with Attachment
```http
POST /api/messages/attachment
Authorization: Bearer <jwt-token>
Content-Type: multipart/form-data

chatroomId: 1
senderId: 1
content: Check out this image!
files: [image.jpg]
```

#### Get Chatroom Messages
```http
GET /api/messages/chatroom/{chatroomId}?page=0&size=20
Authorization: Bearer <jwt-token>
```

#### Edit Message
```http
PUT /api/messages/{messageId}?userId=1&newContent=Updated message
Authorization: Bearer <jwt-token>
```

#### Search Messages
```http
GET /api/messages/chatroom/{chatroomId}/search?searchTerm=hello
Authorization: Bearer <jwt-token>
```

### Chatroom Management

#### Create Chatroom
```http
POST /api/chatrooms
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "user1Id": 1,
  "user2Id": 2
}
```

#### Get User Chatrooms
```http
GET /api/chatrooms/user/{userId}
Authorization: Bearer <jwt-token>
```

## 🔄 Kafka Events

### Message Events
- `MESSAGE_SENT`: When a new message is sent
- `MESSAGE_DELETED`: When a message is deleted
- `MESSAGE_EDITED`: When a message is edited

### User Events
- `USER_ONLINE`: When user comes online
- `USER_OFFLINE`: When user goes offline
- `USER_STATUS_CHANGED`: When user status changes
- `USER_PROFILE_UPDATED`: When user profile is updated

### Notification Events
- `NEW_MESSAGE`: Notification for new message
- `MESSAGE_REACTION`: Notification for message reaction
- `USER_MENTION`: Notification for user mention
- `STATUS_UPDATE`: Notification for status update

## 🗄 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    profile_picture VARCHAR(255),
    status VARCHAR(20) DEFAULT 'OFFLINE',
    bio VARCHAR(200),
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Chatrooms Table
```sql
CREATE TABLE chatrooms (
    id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT REFERENCES users(id),
    user2_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Messages Table
```sql
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    message_type VARCHAR(20) NOT NULL,
    sender_id BIGINT REFERENCES users(id),
    chatroom_id BIGINT REFERENCES chatrooms(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🐳 Docker Deployment

### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/whatsapp-messenger-backend-1.0.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8090:8090"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/whatsapp_chat
    depends_on:
      - db
      - kafka

  db:
    image: postgres:13
    environment:
      POSTGRES_DB: whatsapp_chat
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"

  kafka:
    image: apache/kafka:2.13-3.4.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
    depends_on:
      - zookeeper

  zookeeper:
    image: confluentinc/cp-zookeeper:7.3.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
```bash
mvn jacoco:report
```

## 🔒 Security Considerations

- **JWT Authentication**: Secure token-based authentication
- **Password Hashing**: BCrypt password encryption
- **Input Validation**: Comprehensive request validation
- **File Upload Security**: File type and size validation
- **CORS Configuration**: Proper cross-origin resource sharing
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries

## 📊 Monitoring & Logging

- **Application Logs**: Detailed logging for debugging
- **Kafka Events**: Event tracking for analytics
- **Database Metrics**: Query performance monitoring
- **Error Tracking**: Comprehensive exception handling

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new features
5. Submit a pull request

## 🚀 Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Deployment
```bash
mvn clean package
java -jar target/whatsapp-messenger-backend-1.0.0.jar
```

### Environment Variables
```bash
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://your-db-host:5432/whatsapp_chat
export KAFKA_BOOTSTRAP_SERVERS=your-kafka-host:9092
export JWT_SECRET=your-secure-jwt-secret
```

---

**Note**: This is a comprehensive WhatsApp-like backend implementation with real-time messaging, user authentication, file sharing, and Kafka integration for scalable event-driven architecture. 
