# Automated Incident Response System

A Spring Boot application for automated incident response and monitoring.

## Features

- Real-time incident monitoring and scoring
- WebSocket support for live updates
- YAML-based playbook configuration
- Responsive web dashboard
- Security integration
- Database persistence with PostgreSQL

## Prerequisites

- Java 21 (LTS) or higher
- Maven 3.6+ or Maven wrapper

## Building and Running

### Option 1: Using Maven
```bash
mvn clean compile
mvn spring-boot:run
```

### Option 2: Using Maven Wrapper (if available)
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### Option 3: Build JAR and run
```bash
mvn clean package
java -jar target/automated-incident-response-1.0.0.jar
```

## Configuration

The application uses PostgreSQL database:
- Port: 8080
- Database: PostgreSQL
- Templates: Thymeleaf
- Static resources: CSS, JS
- Security: Basic authentication (admin/admin123)
- WebSocket: Real-time incident updates

## API Endpoints

### Web Interface
- `GET /dashboard` - Main dashboard
- `GET /incidents` - Incidents list
- WebSocket: `/incident-feed` - Real-time updates

### REST API
- `POST /api/webhook` - Create incident from webhook
- `POST /api/web/webhook` - Alternative webhook endpoint
- `GET /api/webhook/test` - Test webhook endpoint
- `GET /api/web/test` - Test web endpoint



## Project Structure

```
src/
├── main/
│   ├── java/com/team/incidentresponse/
│   │   ├── AutomatedIncidentResponseApplication.java
│   │   ├── config/
│   │   │   ├── SchedulerConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── WebSocketConfig.java
│   │   ├── controller/
│   │   │   ├── DashboardController.java
│   │   │   ├── IncidentController.java
│   │   │   ├── WebController.java
│   │   │   └── WebhookController.java
│   │   ├── model/
│   │   │   └── Incident.java
│   │   ├── repository/
│   │   │   └── IncidentRepository.java
│   │   ├── service/
│   │   │   ├── IncidentService.java
│   │   │   ├── FileMonitorService.java
│   │   │   ├── ImapEmailPoller.java
│   │   │   └── AuditLogger.java
│   │   ├── responder/
│   │   │   ├── PlaybookExecutor.java
│   │   │   ├── NotifyUserAction.java
│   │   │   ├── ResponderAction.java
│   │   │   └── RevokeTokenAction.java
│   │   ├── scoring/
│   │   │   └── IncidentScoringEngine.java
│   │   └── util/
│   │       ├── YamlPlaybookLoader.java
│   │       ├── ParameterInjector.java
│   │       └── AuditHashUtil.java
│   └── resources/
│       ├── templates/
│       │   ├── dashboard.html
│       │   ├── layout/base.html
│       │   └── fragments/incident-row.html
│       ├── static/
│       │   ├── css/style.css
│       │   └── js/dashboard.js
│       ├── playbooks/
│       │   ├── suspicious_login.yml
│       │   ├── ransomware_behaviour.yml
│       │   ├── phishing_email.yml
│       │   └── Privilege_escalation.yml
│       └── application.yml
└── test/
    └── java/
```

## Recent Fixes Applied

✅ **Fixed Issues:**
1. Fixed Thymeleaf template expression evaluation errors
2. Corrected enum comparisons in controllers (Status and Severity)
3. Updated package structure from com.vignesh to com.team.incidentresponse
4. Configured PostgreSQL database support
5. Fixed getStatus() method to return Status enum instead of String
6. Resolved compilation errors in DashboardController
7. Added comprehensive project structure with all components

## Testing

The application includes:
- Unit tests with JUnit 5
- Spring Boot Test integration
- PostgreSQL database for testing

Run tests with:
```bash
mvn test
```

## Troubleshooting

If you encounter build errors:
1. Ensure Java 21 is installed and set as your JAVA_HOME
2. Verify Maven installation
3. Check network connectivity for dependency downloads
4. Clean and rebuild: `mvn clean install`

Windows: set JAVA_HOME and update PATH (PowerShell example):

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
java -version
```

If `java -version` doesn't show `21`, point `JAVA_HOME` to a JDK 21 installation or install JDK 21 from Adoptium/Eclipse Temurin or your preferred vendor.

## Development

The application is configured for development with:

## Database Setup

The application uses PostgreSQL as the database:

1. Install and start PostgreSQL (or use a managed PostgreSQL instance).
2. Create a database and user:

```sql
CREATE DATABASE incidentdb;
CREATE USER incidentdb_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE incidentdb TO incidentdb_user;
```

3. Update database credentials in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/incidentdb
    username: incidentdb_user
    password: your_password
```

Notes:
- For production, use environment variables for credentials
- Consider using connection pooling and migrations via Flyway/Liquibase
