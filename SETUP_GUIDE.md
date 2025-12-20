# Setup Guide - Car Pooling Application

This guide will help you set up and run the Car Pooling application on your local machine.

## Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 21**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Maven 3.8+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **PostgreSQL 12+**
   - Download from: https://www.postgresql.org/download/
   - Verify installation: `psql --version`

4. **Git** (Optional, for version control)
   - Download from: https://git-scm.com/downloads

5. **IDE** (Recommended)
   - IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## Step 1: Clone/Download the Project

If using Git:
```bash
git clone <repository-url>
cd carpolling
```

Or download and extract the ZIP file.

## Step 2: PostgreSQL Database Setup

### 2.1 Start PostgreSQL Service

**On macOS:**
```bash
brew services start postgresql
```

**On Ubuntu/Linux:**
```bash
sudo systemctl start postgresql
```

**On Windows:**
Start PostgreSQL service from Services panel or pgAdmin.

### 2.2 Create Database

Open PostgreSQL command line:
```bash
psql -U postgres
```

Create the database:
```sql
CREATE DATABASE carpolling_db;
```

Verify:
```sql
\l
```

Exit psql:
```sql
\q
```

### 2.3 Run Schema Script (Optional)

If you want to manually create tables:
```bash
psql -U postgres -d carpolling_db -f database-schema.sql
```

**Note:** The application will auto-create tables using Hibernate DDL auto, so this step is optional.

## Step 3: Configure Application Properties

Open `src/main/resources/application.properties` and update database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carpolling_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```

Default PostgreSQL credentials:
- Username: `postgres`
- Password: `postgres` (or the password you set during installation)

## Step 4: Build the Project

Navigate to project root and run:

```bash
./mvnw clean install
```

On Windows:
```bash
mvnw.cmd clean install
```

This will:
- Download all dependencies
- Compile the code
- Run tests
- Create JAR file in `target/` directory

## Step 5: Run the Application

### Option 1: Using Maven

```bash
./mvnw spring-boot:run
```

On Windows:
```bash
mvnw.cmd spring-boot:run
```

### Option 2: Using JAR file

```bash
java -jar target/carpolling-0.0.1-SNAPSHOT.jar
```

### Option 3: From IDE

1. Import project as Maven project
2. Right-click on `CarpollingApplication.java`
3. Select "Run" or "Debug"

## Step 6: Verify Installation

### 6.1 Check Application Startup

You should see logs indicating:
```
Started CarpollingApplication in X.XXX seconds
```

Application runs on: **http://localhost:8080**

### 6.2 Test Database Connection

Check logs for:
```
HikariPool-1 - Start completed
```

This confirms database connection is successful.

### 6.3 Verify Tables Created

Connect to PostgreSQL:
```bash
psql -U postgres -d carpolling_db
```

List tables:
```sql
\dt
```

You should see tables like: `users`, `drivers`, `vehicles`, `routes`, `trips`, `bookings`, `payments`, etc.

## Step 7: Test the APIs

### Using cURL

Test health check:
```bash
curl http://localhost:8080/api/users/login
```

Register a user:
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "9876543210",
    "password": "password123",
    "role": "PASSENGER"
  }'
```

### Using Postman

1. Open Postman
2. Import the collection from `postman_collection.json` (if provided)
3. Test endpoints as documented in `API_DOCUMENTATION.md`

### Using Browser

For GET requests, you can use browser:
```
http://localhost:8080/api/trips/boarding-points
```

## Troubleshooting

### Issue 1: Port 8080 Already in Use

Change port in `application.properties`:
```properties
server.port=8081
```

### Issue 2: Database Connection Failed

- Verify PostgreSQL is running: `pg_isready`
- Check credentials in `application.properties`
- Ensure database `carpolling_db` exists
- Check PostgreSQL logs for errors

### Issue 3: Maven Build Failed

Clear Maven cache:
```bash
./mvnw clean
rm -rf ~/.m2/repository
./mvnw install
```

### Issue 4: Hibernate Schema Creation Failed

Set to create-drop for fresh start:
```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

**Warning:** This will delete all data on restart!

### Issue 5: Out of Memory

Increase heap size:
```bash
export MAVEN_OPTS="-Xmx1024m"
./mvnw spring-boot:run
```

## Development Tips

### Enable Hot Reload

Add Spring Boot DevTools (already included in pom.xml):
- Code changes will auto-reload
- No need to restart application

### View SQL Queries

Already enabled in `application.properties`:
```properties
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### Debug Mode

Run with debug flag:
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

Then attach debugger on port 5005.

## Production Deployment

### Build Production JAR

```bash
./mvnw clean package -DskipTests
```

JAR file will be in `target/` directory.

### Run Production

```bash
java -jar target/carpolling-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=80
```

### Environment Variables

Set environment variables:
```bash
export DB_URL=jdbc:postgresql://prod-host:5432/carpolling_db
export DB_USERNAME=prod_user
export DB_PASSWORD=prod_password

java -jar target/carpolling-0.0.1-SNAPSHOT.jar
```

### Docker Deployment (Optional)

Create `Dockerfile`:
```dockerfile
FROM openjdk:21-slim
COPY target/carpolling-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t carpolling-app .
docker run -p 8080:8080 carpolling-app
```

## Sample Data

To populate with sample data, you can:

1. Use the API endpoints to create data
2. Run SQL INSERT statements
3. Create a data initialization service (DataLoader)

## API Testing Workflow

### Complete Flow Test

1. **Register User (Passenger)**
2. **Register User (Driver)**
3. **Register Driver Profile**
4. **Add Vehicle**
5. **Create Route**
6. **Create Trip**
7. **Search Trips** (as passenger)
8. **View Seats**
9. **Create Booking**
10. **Process Payment**
11. **View Booking**

Detailed examples are in `API_DOCUMENTATION.md`.

## Security Notes

For production:

1. **Change JWT Secret**
   ```properties
   jwt.secret=your_very_long_and_secure_secret_key_here
   ```

2. **Use HTTPS**
3. **Enable CSRF** (if using session-based auth)
4. **Add Rate Limiting**
5. **Use Environment Variables** for sensitive data
6. **Enable Actuator** for monitoring

## Monitoring

Access application metrics (if enabled):
```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
```

## Support

For issues:
1. Check logs in console
2. Check database logs
3. Review `HELP.md` for Spring Boot documentation
4. Check `API_DOCUMENTATION.md` for API details

## Next Steps

After successful setup:

1. Review `README.md` for features overview
2. Read `API_DOCUMENTATION.md` for API details
3. Test all endpoints using Postman
4. Review database schema in `database-schema.sql`
5. Implement additional features as needed

Happy Coding! 🚗💨









