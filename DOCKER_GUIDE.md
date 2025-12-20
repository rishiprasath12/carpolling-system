# Docker Setup Guide for Carpooling Application

This guide explains how to run the Carpooling Application using Docker and Docker Compose.

## Prerequisites

- Docker Desktop installed ([Download Docker](https://www.docker.com/products/docker-desktop))
- Docker Compose (included with Docker Desktop)

## Quick Start

### Option 1: Using Docker Compose (Recommended)

This will start both the PostgreSQL database and the Spring Boot application:

```bash
# Build and start all services
docker-compose up --build

# Or run in detached mode (background)
docker-compose up -d --build
```

### Option 2: Using Docker Only (Manual)

If you want more control over individual containers:

```bash
# 1. Create a network
docker network create carpolling-network

# 2. Start PostgreSQL
docker run -d \
  --name carpolling-postgres \
  --network carpolling-network \
  -e POSTGRES_DB=carpolling_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin \
  -p 5432:5432 \
  -v $(pwd)/init-db.sql:/docker-entrypoint-initdb.d/init-db.sql \
  postgres:16-alpine

# 3. Build the application image
docker build -t carpolling-app .

# 4. Run the application
docker run -d \
  --name carpolling-app \
  --network carpolling-network \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://carpolling-postgres:5432/carpolling_db \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  -p 8010:8010 \
  carpolling-app
```

## Accessing the Application

Once the containers are running:

- **Application API**: http://localhost:8010
- **Swagger UI**: http://localhost:8010/swagger-ui.html
- **API Documentation**: http://localhost:8010/api-docs
- **PostgreSQL Database**: localhost:5432

## Useful Docker Commands

### Managing Containers

```bash
# View running containers
docker-compose ps

# View logs
docker-compose logs -f

# View logs for specific service
docker-compose logs -f app
docker-compose logs -f postgres

# Stop all services
docker-compose stop

# Start services
docker-compose start

# Restart services
docker-compose restart

# Stop and remove containers
docker-compose down

# Stop and remove containers with volumes (⚠️ deletes database data)
docker-compose down -v
```

### Rebuilding After Code Changes

```bash
# Rebuild and restart the application
docker-compose up --build -d app

# Or rebuild everything
docker-compose up --build -d
```

### Database Management

```bash
# Access PostgreSQL CLI
docker exec -it carpolling-postgres psql -U admin -d carpolling_db

# Backup database
docker exec carpolling-postgres pg_dump -U admin carpolling_db > backup.sql

# Restore database
docker exec -i carpolling-postgres psql -U admin carpolling_db < backup.sql
```

### Troubleshooting

```bash
# Check application logs
docker logs carpolling-app

# Check database logs
docker logs carpolling-postgres

# Enter container shell
docker exec -it carpolling-app sh
docker exec -it carpolling-postgres sh

# Check network connectivity
docker network inspect carpolling-network
```

## Environment Variables

You can customize the application by setting environment variables in `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/carpolling_db
  SPRING_DATASOURCE_USERNAME: admin
  SPRING_DATASOURCE_PASSWORD: admin
  JWT_SECRET: yourSecretKeyForJWTTokenGenerationShouldBeLongAndSecure
  JWT_EXPIRATION: 86400000
```

## For UI Developers

### Connecting Your Frontend to the API

When your friend is developing the UI, they can:

1. **Run the backend using Docker Compose:**
   ```bash
   docker-compose up -d
   ```

2. **Access the API at:** `http://localhost:8010`

3. **View API documentation at:** `http://localhost:8010/swagger-ui.html`

### CORS Configuration

If you need to allow requests from the UI running on a different port (e.g., React on port 3000), you can add CORS configuration. The application is configured to allow cross-origin requests in development.

### API Testing

Use tools like:
- **Swagger UI**: Built-in at http://localhost:8010/swagger-ui.html
- **Postman**: Import the OpenAPI spec from http://localhost:8010/api-docs
- **curl**: Command-line testing

Example:
```bash
# Health check
curl http://localhost:8010/actuator/health

# Test endpoint (adjust based on your actual endpoints)
curl http://localhost:8010/api/trips
```

## Production Considerations

For production deployment:

1. **Update security settings:**
   - Change default database credentials
   - Update JWT secret key
   - Enable HTTPS
   - Configure proper CORS origins

2. **Use Docker secrets or environment files:**
   ```bash
   # Create .env file
   echo "POSTGRES_PASSWORD=strong_password_here" > .env
   echo "JWT_SECRET=your_production_jwt_secret" >> .env
   ```

3. **Use production-ready database:**
   - Consider using managed PostgreSQL (AWS RDS, Azure Database, etc.)
   - Set up proper backups
   - Configure connection pooling

4. **Add health checks:**
   Already configured in docker-compose.yml

5. **Use docker-compose production override:**
   ```bash
   docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
   ```

## Building for Different Platforms

If sharing the image across different architectures (e.g., ARM Mac to Intel server):

```bash
# Build for multiple platforms
docker buildx build --platform linux/amd64,linux/arm64 -t carpolling-app .

# Push to Docker Hub (optional)
docker tag carpolling-app yourusername/carpolling-app:latest
docker push yourusername/carpolling-app:latest
```

## Sharing the Application

### Option 1: Share Docker Compose Files (Recommended)
Send your friend:
- `Dockerfile`
- `docker-compose.yml`
- `init-db.sql`
- `.dockerignore`

They can run: `docker-compose up --build`

### Option 2: Share Docker Image
```bash
# Save image to file
docker save carpolling-app > carpolling-app.tar

# Load on another machine
docker load < carpolling-app.tar
```

### Option 3: Push to Docker Hub
```bash
# Login to Docker Hub
docker login

# Tag and push
docker tag carpolling-app yourusername/carpolling-app:latest
docker push yourusername/carpolling-app:latest

# Your friend can pull and run
docker pull yourusername/carpolling-app:latest
```

## Clean Up

Remove all containers, networks, and volumes:

```bash
# Stop and remove everything
docker-compose down -v

# Remove dangling images
docker image prune

# Remove all unused containers, networks, images
docker system prune -a
```

## Support

For issues or questions:
- Check logs: `docker-compose logs -f`
- Verify database connection: `docker exec -it carpolling-postgres psql -U admin -d carpolling_db`
- Check API documentation: http://localhost:8010/swagger-ui.html


