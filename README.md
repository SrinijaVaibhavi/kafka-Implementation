# Message Application

A full-stack message processing application built with Angular frontend and Spring Boot backend, deployed on Google Cloud Platform with event-driven architecture using Apache Kafka.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Angular UI    │────│  Spring Boot API │────│   MySQL DB      │
│  (Firebase)     │    │   (Cloud Run)    │    │  (Cloud SQL)    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌──────────────────┐
                       │  Apache Kafka    │
                       │   (Pub/Sub)      │
                       └──────────────────┘
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
- **Data Persistence**: All submissions stored in MySQL database
- **Real-time Status Tracking**: Monitor message processing status
- **Responsive Design**: Mobile-friendly Angular frontend
- **Cloud-Native**: Fully deployed on Google Cloud Platform

## 🛠️ Technology Stack

### Frontend
- **Framework**: Angular 18+
- **Language**: TypeScript
- **Styling**: CSS3
- **Deployment**: Firebase Hosting
- **URL**: https://message-app-project-464401.web.app

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Build Tool**: Gradle
- **Deployment**: Google Cloud Run
- **URL**: https://message-producer-60630737665.us-central1.run.app

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
message-app/
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
├── backend/                  # Spring Boot application
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

#### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd message-app/backend
   ```

2. **Configure application properties**
   ```properties
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/messages_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   
   # Kafka configuration
   spring.kafka.bootstrap-servers=localhost:9092
   
   # GCS configuration
   spring.cloud.gcp.storage.bucket-name=your-bucket-name
   ```

3. **Build and run**
   ```bash
   ./gradlew bootRun
   ```
   API available at: http://localhost:8080

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

### Backend Deployment (Cloud Run)

1. **Build with Gradle**
   ```bash
   ./gradlew clean build -x test
   ```

2. **Deploy to Cloud Run**
   ```bash
   gcloud run deploy message-producer --source . \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated
   ```

## 📊 Database Schema

### MessageRecord Entity

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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 API Endpoints

### POST /api/messages
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

## 🔐 Security & CORS

The backend is configured to accept requests from:
- Production: `https://message-app-project-464401.web.app`
- Development: `http://localhost:4200`

CORS configuration in `MessageController`:
```java
@CrossOrigin(origins = {
    "https://message-app-project-464401.web.app",
    "http://localhost:4200"
})
```

## 📈 Monitoring & Logs

### View Application Logs
```bash
# Cloud Run logs
gcloud logs read --service=message-producer --limit=50

# Cloud SQL logs
gcloud sql operations list --instance=messages-db
```

### Database Access
```bash
# Connect to Cloud SQL
gcloud sql connect messages-db --user=root

# View message records
USE messages_db;
SELECT * FROM message_record ORDER BY id DESC LIMIT 10;
```

## 🐛 Troubleshooting

### Common Issues

1. **CORS Errors**
   - Ensure backend CORS configuration includes your frontend domain
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

### Environment-Specific Configurations

**Development**:
- Frontend: `http://localhost:4200`
- Backend: `http://localhost:8080`
- Database: Local MySQL

**Production**:
- Frontend: `https://message-app-project-464401.web.app`
- Backend: `https://message-producer-60630737665.us-central1.run.app`
- Database: Google Cloud SQL

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
