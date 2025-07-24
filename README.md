# Getting Started

### Prerequisite
- Intellij (Recommended)
- Java version: 17
- JDK: azul-zulu 17.0.16
- Database: PostgreSQL

### Run locally
Make sure PostgreSQL is running locally (e.g., via pgAdmin, systemctl, or services.msc depending on OS).
Use the built-in Intellij build configurations:
- Run Application: Start the application from the main class
- Run JAR Application: Start the application from JAR

### Run docker locally (alternative)
- `docker login`
- `./mvnw clean package`
- `docker build -t demo/demo:0.0.1 .`
- `docker push demo/demo:0.0.1`
- Update new version to docker-compose file (if any)
- `docker-compose up -d`

### Test
Use the built-in Intellij build configurations:
- Test: Execute all JUnit tests

### Api
- Create Scratch File for HTTP Request 
- Refer the scratch.http to test API
