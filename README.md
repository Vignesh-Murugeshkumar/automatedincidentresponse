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

## Using MySQL instead of H2 (optional)

If you'd like to run the application against a MySQL database so data persists across restarts, follow these steps:

1. Install and start MySQL (or use a managed MySQL instance).
2. Create a database and user, for example (run in MySQL shell):

```sql
CREATE DATABASE incidentdb;
CREATE USER 'incidentuser'@'localhost' IDENTIFIED BY 'strongpassword';
GRANT ALL PRIVILEGES ON incidentdb.* TO 'incidentuser'@'localhost';
FLUSH PRIVILEGES;
```

3. Set environment variables before starting the app (PowerShell example):

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/incidentdb?useSSL=false&serverTimezone=UTC'
$env:SPRING_DATASOURCE_USERNAME = 'incidentuser'
$env:SPRING_DATASOURCE_PASSWORD = 'strongpassword'
$env:SPRING_DATASOURCE_DRIVER = 'com.mysql.cj.jdbc.Driver'
mvn -DskipTests spring-boot:run
```

4. The `mysql-connector-java` dependency is already present in `pom.xml`. Spring Boot will pick up the environment variables and connect to MySQL.

Notes:
- For production, tune connection pool settings and avoid `spring.jpa.hibernate.ddl-auto: update` (consider migrations via Flyway/Liquibase).
- If you prefer a file-backed H2 database instead, set `SPRING_DATASOURCE_URL=jdbc:h2:file:./data/incidentdb`.
