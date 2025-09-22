# Automated Incident Response System

A Spring Boot application for automated incident response and monitoring.

## Features

- Real-time incident monitoring and scoring
- WebSocket support for live updates
- YAML-based playbook configuration
- Responsive web dashboard
- Security integration
- Database persistence with H2

## Prerequisites

- Java 17 or higher
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

The application uses the following default configuration:
- Port: 8080
- Database: H2 (in-memory)
- Templates: Thymeleaf
- Static resources: CSS, JS

## API Endpoints

- `GET /` - Dashboard
- `GET /incidents` - Incidents list
- WebSocket: `/ws` - Real-time updates

## Project Structure

```
src/
├── main/
│   ├── java/com/vignesh/incidentresponse/
│   │   ├── AutomatedIncidentResponseApplication.java
│   │   ├── Incident.java
│   │   ├── IncidentController.java
│   │   ├── IncidentService.java
│   │   ├── IncidentRepository.java
│   │   └── ... (other components)
│   └── resources/
│       ├── templates/
│       │   ├── dashboard.html
│       │   ├── layout/base.html
│       │   └── fragments/incident-row.html
│       ├── static/
│       │   ├── css/style.css
│       │   └── js/dashboard.js
│       └── playbooks/
│           ├── suspicious_login.yml
│           ├── ransomware_behaviour.yml
│           └── ...
└── test/
    └── java/
```

## Recent Fixes Applied

✅ **Fixed Issues:**
1. Created missing main Spring Boot application class
2. Added comprehensive pom.xml with all dependencies
3. Created missing Thymeleaf templates (base.html, dashboard.html, incident-row.html)
4. Created missing JPA entity (Incident.java)
5. Fixed project structure and package declarations

## Testing

The application includes:
- Unit tests with JUnit 5
- Spring Boot Test integration
- H2 in-memory database for testing

Run tests with:
```bash
mvn test
```

## Troubleshooting

If you encounter build errors:
1. Ensure Java 17+ is installed
2. Verify Maven installation
3. Check network connectivity for dependency downloads
4. Clean and rebuild: `mvn clean install`

## Development

The application is configured for development with:
- Hot reload support
- H2 console available at `/h2-console`
- Debug logging enabled
- Test profile configuration
