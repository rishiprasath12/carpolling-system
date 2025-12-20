# Quick Start Guide for UI Developers

This guide will help you quickly set up the backend API so you can focus on building the frontend.

## What You Need

1. **Docker Desktop** - [Download here](https://www.docker.com/products/docker-desktop)
   - For Mac: Download Docker Desktop for Mac
   - For Windows: Download Docker Desktop for Windows
   - For Linux: Install Docker and Docker Compose

That's it! No need to install Java, PostgreSQL, Maven, or anything else.

## Starting the Backend (3 Simple Steps)

### Step 1: Install Docker Desktop
Download and install Docker Desktop from the link above. Make sure it's running (you'll see a whale icon in your system tray/menu bar).

### Step 2: Navigate to the Project Directory
```bash
cd /path/to/carpolling
```

### Step 3: Start Everything
Run this single command:
```bash
docker-compose up -d
```

Wait about 30 seconds for everything to start up. That's it! 🎉

## What Just Happened?

Docker automatically:
- ✅ Downloaded PostgreSQL database
- ✅ Created the database with all tables
- ✅ Built the Spring Boot application
- ✅ Started both services
- ✅ Connected them together

## Testing the API

Open your browser and go to:
- **http://localhost:8010/swagger-ui.html**

You'll see an interactive API documentation where you can test all endpoints!

## Important Endpoints for Your Frontend

### Base URL
```
http://localhost:8010
```

### Key API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/users/register` | POST | Register a new user |
| `/api/users/login` | POST | User login |
| `/api/trips/search` | POST | Search for available trips |
| `/api/trips/{tripId}/seats` | GET | Get seat availability |
| `/api/bookings` | POST | Create a booking |
| `/api/bookings/user/{userId}` | GET | Get user's bookings |
| `/api/payments/process` | POST | Process payment |

### Full API Documentation
- **Swagger UI**: http://localhost:8010/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8010/api-docs

## Example API Call

```javascript
// Example: Search for trips
fetch('http://localhost:8010/api/trips/search', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    boardingPoint: "Mumbai Central",
    dropPoint: "Pune",
    travelDate: "2024-12-25",
    requiredSeats: 2
  })
})
  .then(response => response.json())
  .then(data => console.log(data));
```

## CORS (Cross-Origin Requests)

Don't worry about CORS! The backend is already configured to accept requests from your frontend development server (like React on port 3000, Vue on port 8080, etc.).

## Stopping the Backend

When you're done for the day:
```bash
docker-compose down
```

## Restarting the Backend

Next time you want to start again:
```bash
docker-compose up -d
```

It will start much faster because everything is already set up!

## Useful Commands

```bash
# View backend logs (useful for debugging)
docker-compose logs -f app

# View database logs
docker-compose logs -f postgres

# Restart if something goes wrong
docker-compose restart

# Stop everything
docker-compose down

# Stop and delete all data (fresh start)
docker-compose down -v
```

## Default Database Credentials

If you need to connect to the database directly:
- **Host**: localhost
- **Port**: 5432
- **Database**: carpolling_db
- **Username**: admin
- **Password**: admin

You can use tools like pgAdmin, DBeaver, or TablePlus to view the data.

## Testing with Sample Data

The database starts empty. You'll need to:
1. Register a user via API
2. Register as a driver
3. Add a vehicle
4. Create a route
5. Create a trip

Or use the Swagger UI to quickly create test data!

## Troubleshooting

### "Docker is not running"
Make sure Docker Desktop is open and running.

### "Port 8010 is already in use"
Something else is using port 8010. Either:
- Stop the other service
- Or edit `docker-compose.yml` to change the port:
  ```yaml
  ports:
    - "8011:8010"  # Use 8011 instead
  ```

### "Connection refused"
Wait a bit longer (30-60 seconds) for the services to fully start.

### "Database connection error"
Run:
```bash
docker-compose restart
```

### Still having issues?
```bash
# Stop everything
docker-compose down

# Start with logs visible
docker-compose up
```

Watch the logs for any error messages.

## API Response Format

All API responses follow this format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    // Your data here
  },
  "timestamp": "2024-12-16T10:30:00"
}
```

Error responses:
```json
{
  "success": false,
  "message": "Error description",
  "error": "Detailed error message",
  "timestamp": "2024-12-16T10:30:00"
}
```

## Next Steps

1. ✅ Start the backend with Docker
2. ✅ Open Swagger UI to explore the API
3. ✅ Create some test data using Swagger
4. ✅ Start building your frontend!
5. ✅ Test your frontend by calling the API

## Need Help?

- Check the [Full Documentation](README.md)
- See [Docker Guide](DOCKER_GUIDE.md) for advanced Docker usage
- View [API Documentation](API_DOCUMENTATION.md)
- All API details are in Swagger UI: http://localhost:8010/swagger-ui.html

## Architecture Overview

```
Your Frontend (React/Vue/Angular)
         ↓
    http://localhost:8010/api/*
         ↓
  Spring Boot Backend
         ↓
  PostgreSQL Database
```

Everything is connected automatically via Docker!

## Sample Workflow for Testing

1. **Register a User** (Passenger)
2. **Register another User** (Driver)
3. **Register as Driver** (using driver's user ID)
4. **Add a Vehicle** (for the driver)
5. **Create a Route** (with multiple points)
6. **Create a Trip** (schedule a trip on the route)
7. **Search for Trips** (as passenger)
8. **Book Seats** (select seats)
9. **Make Payment** (confirm booking)

You can do all of this via Swagger UI to create test data for your frontend!

Happy Coding! 🚀


