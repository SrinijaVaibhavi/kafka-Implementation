# Message Processing Microservices Application

A full-stack event-driven message processing application built with Angular frontend and Spring Boot microservices backend, deployed on Google Cloud Platform with Apache Kafka for inter-service communication.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Angular UI    │────│  Message Producer│────│   MySQL DB      │
│  (Firebase)     │    │   (Cloud Run)    │    │  (Cloud SQL)    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌──────────────────┐
                       │  Apache Kafka    │
                       │   (Event Bus)    │
                       └──────────────────┘
                              │
                              ▼
                       ┌──────────────────┐    ┌─────────────────┐
                       │ Email Consumer   │────│  SMTP Service   │
                       │  (Cloud Run)     │    │   (Email)       │
                       └──────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌──────────────────┐
                       │  File Storage    │
                       │ (Google Cloud    │
                       │    Storage)      │
                       └──────────────────┘
```

## 🚀 Features

- **Contact Form Submission**: Users can submit messages with file attachments
- **File Upload**: Support for file attachments stored in Google Cloud Storage
- **Event-Driven Processing**: Messages are published to Kafka for asynchronous processing
- **Email Notifications**: Automatic email sending via dedicated email microservice
- **Data Persistence**: All submissions stored in MySQL database
- **Real-time Status Tracking**: Monitor message processing status across microservices
- **Microservices Architecture**: Decoupled services for scalability and maintainability
- **Responsive Design**: Mobile-friendly Angular frontend
- **Cloud-Native**: Fully deployed on Google Cloud Platform

## 🛠️ Technology Stack

### Frontend
- **Framework**: Angular 18+
- **Language**: TypeScript
- **Styling**: CSS3
- **Deployment**: Firebase Hosting
- **URL**: https://message-app-project-464401.web.app

### Backend Microservices

#### Message Producer Service
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Build Tool**: Gradle
- **Deployment**: Google Cloud Run
- **URL**: https://message-producer-60630737665.us-central1.run.app
- **Responsibilities**: Handle form submissions, file uploads, publish to Kafka

#### Email Consumer Service
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Build Tool**: Gradle
- **Deployment**: Google Cloud Run
- **Responsibilities**: Consume Kafka messages, send emails, handle attachments

### Database & Storage
- **Database**: MySQL 8.0 (Google Cloud SQL)
- **File Storage**: Google Cloud Storage
- **Message Queue**: Apache Kafka

### Infrastructure
- **Cloud Provider**: Google Cloud Platform (GCP)
- **Container Runtime**: Cloud Run
- **CI/CD**: Firebase CLI, gcloud CLI

## 📁 Project Structure

```
message-microservices/
├── frontend/                 # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── services/
│   │   │   └── environments/
│   │   └── assets/
│   ├── angular.json
│   ├── package.json
│   └── firebase.json
├── message-producer/         # Message Producer Service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/messages/
│   │   │   │       ├── controller/
│   │   │   │       ├── entity/
│   │   │   │       ├── repository/
│   │   │   │       ├── service/
│   │   │   │       └── config/
│   │   │   └── resources/
│   │   └── test/
│   ├── build.gradle
│   └── Dockerfile
├── email-consumer/           # Email Consumer Service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/email/
│   │   │   │       ├── consumer/
│   │   │   │       ├── service/
│   │   │   │       ├── model/
│   │   │   │       └── config/
│   │   │   └── resources/
│   │   └── test/
│   ├── build.gradle
│   └── Dockerfile
└── README.md
```

## 🚦 Getting Started

### Prerequisites

- Node.js 18+ and npm
- Java 17+
- Google Cloud CLI
- Firebase CLI
- MySQL (for local development)

### Local Development Setup

#### Frontend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd message-app/frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure environment**
   ```bash
   # Update src/environments/environment.ts for local development
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/api'
   };
   ```

4. **Start development server**
   ```bash
   ng serve
   ```
   Access at: http://localhost:4200

#### Message Producer Setup

1. **Navigate to message producer directory**
   ```bash
   cd message-microservices/message-producer
   ```

2. **Configure application properties**
   ```properties
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/messages_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   
   # Kafka Producer configuration
   spring.kafka.bootstrap-servers=localhost:9092
   spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
   spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
   
   # GCS configuration
   spring.cloud.gcp.storage.bucket-name=your-bucket-name
   ```

3. **Build and run**
   ```bash
   ./gradlew bootRun
   ```
   API available at: http://localhost:8080

#### Email Consumer Setup

1. **Navigate to email consumer directory**
   ```bash
   cd message-microservices/email-consumer
   ```

2. **Configure application properties**
   ```properties
   # src/main/resources/application.properties
   # Kafka Consumer configuration
   spring.kafka.bootstrap-servers=localhost:9092
   spring.kafka.consumer.group-id=email-consumer-group
   spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
   spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
   
   # Email configuration (SMTP)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   
   # GCS configuration for attachments
   spring.cloud.gcp.storage.bucket-name=your-bucket-name
   ```

3. **Build and run**
   ```bash
   ./gradlew bootRun
   ```
   Consumer service available at: http://localhost:8081

## 🌐 Deployment

### Frontend Deployment (Firebase)

1. **Build for production**
   ```bash
   ng build --configuration production
   ```

2. **Deploy to Firebase**
   ```bash
   firebase deploy --only hosting
   ```

### Message Producer Deployment (Cloud Run)

1. **Build with Gradle**
   ```bash
   cd message-producer
   ./gradlew clean build -x test
   ```

2. **Deploy to Cloud Run**
   ```bash
   gcloud run deploy message-producer --source . \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated
   ```

### Email Consumer Deployment (Cloud Run)

1. **Build with Gradle**
   ```bash
   cd email-consumer
   ./gradlew clean build -x test
   ```

2. **Deploy to Cloud Run**
   ```bash
   gcloud run deploy email-consumer --source . \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated
   ```

## 🔄 Message Flow

1. **User Submission**: User fills out contact form with optional file attachment
2. **Message Producer**: Receives HTTP request, validates data, uploads file to GCS
3. **Database Storage**: Message details stored in MySQL database
4. **Kafka Publishing**: Message producer publishes event to Kafka topic
5. **Email Consumer**: Listens to Kafka topic, receives message event
6. **Email Processing**: Downloads attachment from GCS, composes email
7. **Email Delivery**: Sends email via SMTP service with attachment

## 📊 Database Schema

### MessageRecord Entity (Message Producer Service)

```sql
CREATE TABLE message_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    subject VARCHAR(255),
    message TEXT,
    attachment_file_name VARCHAR(255),
    attachment_gcs_path VARCHAR(500),
    status VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### EmailLog Entity (Email Consumer Service - Optional)

```sql
CREATE TABLE email_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id BIGINT,
    recipient_email VARCHAR(255),
    subject VARCHAR(255),
    email_status VARCHAR(50),
    sent_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES message_record(id)
);
```

## 🔧 API Endpoints

### Message Producer Service

#### POST /api/messages
Submit a new message with optional file attachment.

**Request**: `multipart/form-data`
- `firstName` (string): Sender's first name
- `lastName` (string): Sender's last name
- `email` (string): Sender's email address
- `subject` (string): Message subject
- `message` (string): Message content
- `attachment` (file, optional): File attachment

**Response**: `200 OK`
```json
{
  "status": "Message received, saved, and sent to Kafka"
}
```

### Email Consumer Service

#### Internal Kafka Consumer
- **Topic**: `message-events`
- **Group ID**: `email-consumer-group`
- **Message Format**: JSON containing message details and GCS file path

**Expected Kafka Message**:
```json
{
  "toEmail": "recipient@example.com",
  "subject": "Contact Form Submission",
  "body": "Message content",
  "attachmentFileName": "document.pdf",
  "attachmentGcsPath": "gs://bucket/path/to/file"
}
```

## 🔐 Security & CORS

### Message Producer Service
The message producer is configured to accept requests from:
- Production: `https://message-app-project-464401.web.app`
- Development: `http://localhost:4200`

CORS configuration in `MessageController`:
```java
@CrossOrigin(origins = {
    "https://message-app-project-464401.web.app",
    "http://localhost:4200"
})
```

### Email Consumer Service
- Internal service, not exposed to external traffic
- Communicates via Kafka and SMTP only
- No CORS configuration needed

## 📈 Monitoring & Logs

### View Application Logs
```bash
# Message Producer logs
gcloud logs read --service=message-producer --limit=50

# Email Consumer logs  
gcloud logs read --service=email-consumer --limit=50

# Cloud SQL logs
gcloud sql operations list --instance=messages-db
```

### Monitor Kafka Topics
```bash
# List topics
kafka-topics.sh --bootstrap-server localhost:9092 --list

# Monitor message-events topic
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic message-events --from-beginning
```

### Database Access
```bash
# Connect to Cloud SQL
gcloud sql connect messages-db --user=root

# View message records
USE messages_db;
SELECT * FROM message_record ORDER BY id DESC LIMIT 10;

# View email logs (if implemented)
SELECT * FROM email_log ORDER BY id DESC LIMIT 10;
```

## 🐛 Troubleshooting

### Common Issues

1. **CORS Errors**
   - Ensure message producer CORS configuration includes your frontend domain
   - Check that both frontend and backend are using HTTPS in production

2. **TypeScript Version Conflicts**
   - Update TypeScript: `npm install typescript@~5.8.0 --save-dev`
   - Clear cache: `rm -rf node_modules package-lock.json && npm install`

3. **Build Failures**
   - Skip tests during deployment: `./gradlew clean build -x test`
   - Check Java version compatibility

4. **Database Connection Issues**
   - Verify Cloud SQL instance is running
   - Check firewall rules and authorized networks
   - Ensure service account has proper permissions

5. **Kafka Connection Issues**
   - Verify Kafka broker is accessible from both services
   - Check topic exists: `message-events`
   - Ensure consumer group ID is unique per service

6. **Email Delivery Issues**
   - Verify SMTP credentials and configuration
   - Check Gmail app password (not regular password)
   - Ensure "Less secure app access" is enabled or use OAuth2

7. **File Upload/Download Issues**
   - Verify GCS bucket permissions
   - Check service account has Storage Admin role
   - Ensure bucket name is correct in both services

### Service Communication Flow

```
Frontend → Message Producer → Kafka → Email Consumer → SMTP → Email Delivery
    ↓            ↓                         ↓
   UI         Database                 Email Logs
             File Storage             File Download
```

### Environment-Specific Configurations

**Development**:
- Frontend: `http://localhost:4200`
- Message Producer: `http://localhost:8080`
- Email Consumer: `http://localhost:8081`
- Database: Local MySQL
- Kafka: Local Kafka broker

**Production**:
- Frontend: `https://message-app-project-464401.web.app`
- Message Producer: `https://message-producer-60630737665.us-central1.run.app`
- Email Consumer: `https://email-consumer-[hash].us-central1.run.app`
- Database: Google Cloud SQL
- Kafka: Managed Kafka service or Confluent Cloud

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section above
- Review Google Cloud documentation for infrastructure-related issues

---

**Live Application**: [https://message-app-project-464401.web.app](https://message-app-project-464401.web.app)

**Backend API**: [https://message-producer-60630737665.us-central1.run.app](https://message-producer-60630737665.us-central1.run.app)
