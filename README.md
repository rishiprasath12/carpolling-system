# Car Pooling Ticket Booking Application

A comprehensive Spring Boot application for car pooling/ticket booking similar to RedBus but for cars. Users can search for available cars, book seats, and make payments. Drivers can enroll, add vehicles, and create routes.

## Features

### Passenger Features
- User registration and login
- Search trips by boarding and destination points
- View available cars with details (driver rating, vehicle info, price)
- View seat availability (excluding driver seat)
- Book multiple seats
- Make payment and confirm booking
- View booking history
- Cancel bookings

### Driver Features
- Register as a driver with license details
- Add vehicle information
- Create routes with multiple points (A → B → C → D)
- Create trips with pricing
- View bookings

## Technology Stack

- **Backend**: Spring Boot 3.5.7
- **Database**: PostgreSQL
- **Security**: Spring Security with BCrypt password encoding
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Java Version**: 17
- **Containerization**: Docker & Docker Compose

## 🚀 Quick Start with Docker (Recommended)

The easiest way to run this application is using Docker. This will automatically set up both the application and the PostgreSQL database.

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed

### Start the Application

**Option 1: Using the startup script**

For Mac/Linux:
```bash
chmod +x start-docker.sh
./start-docker.sh
```

For Windows:
```bash
start-docker.bat
```

**Option 2: Using Docker Compose directly**
```bash
docker-compose up --build -d
```

### Access the Application

Once started, you can access:
- **API**: http://localhost:8010
- **Swagger UI**: http://localhost:8010/swagger-ui.html
- **API Documentation**: http://localhost:8010/api-docs
- **Database**: localhost:5432

### Stop the Application

```bash
docker-compose down
```

📖 For detailed Docker instructions, see [DOCKER_GUIDE.md](DOCKER_GUIDE.md)

## 🛠️ Manual Setup (Without Docker)

## Database Schema

### Core Entities

1. **Users**: Store user information (passengers and drivers)
2. **Drivers**: Driver-specific information (license, rating, etc.)
3. **Vehicles**: Car details registered by drivers
4. **Routes**: Routes created by drivers with multiple points
5. **RoutePoints**: Sequential points in a route
6. **Trips**: Scheduled instances of routes
7. **TripSeats**: Seat availability for each trip
8. **Bookings**: Booking information
9. **Payments**: Payment transactions

### Database Relationships

- User (1) ↔ (1) Driver
- Driver (1) ↔ (M) Vehicles
- Driver (1) ↔ (M) Routes
- Route (1) ↔ (M) RoutePoints
- Route (1) ↔ (M) Trips
- Trip (1) ↔ (M) TripSeats
- Trip (1) ↔ (M) Bookings
- Booking (1) ↔ (1) Payment

## API Endpoints

### User Management

#### Register User
```
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "password": "password123",
  "role": "PASSENGER"
}
```

#### Login
```
POST /api/users/login
Content-Type: application/json

{
  "emailOrPhone": "john@example.com",
  "password": "password123"
}
```

### Driver Management

#### Register as Driver
```
POST /api/drivers/register
Content-Type: application/json

{
  "userId": 1,
  "licenseNumber": "DL1234567890",
  "licenseExpiryDate": "2025-12-31",
  "experienceYears": 5,
  "additionalInfo": "Experienced driver"
}
```

#### Register Vehicle
```
POST /api/drivers/vehicles
Content-Type: application/json

{
  "driverId": 1,
  "registrationNumber": "MH01AB1234",
  "brand": "Toyota",
  "model": "Innova",
  "color": "White",
  "manufacturingYear": 2020,
  "vehicleType": "SUV",
  "totalSeats": 7,
  "hasAC": true,
  "features": "GPS, Music System"
}
```

#### Get Driver Vehicles
```
GET /api/drivers/{driverId}/vehicles
```

#### Create Route
```
POST /api/drivers/routes
Content-Type: application/json

{
  "driverId": 1,
  "routeName": "Mumbai to Pune via Lonavala",
  "totalDistance": 150000,
  "estimatedDuration": 180,
  "routePoints": [
    {
      "pointName": "Mumbai Central",
      "address": "Mumbai Central Station, Mumbai",
      "latitude": 18.9682,
      "longitude": 72.8209,
      "sequenceOrder": 1,
      "distanceFromStart": 0,
      "timeFromStart": 0,
      "isBoardingPoint": true,
      "isDropPoint": false
    },
    {
      "pointName": "Lonavala",
      "address": "Lonavala Bus Stand",
      "latitude": 18.7537,
      "longitude": 73.4086,
      "sequenceOrder": 2,
      "distanceFromStart": 83000,
      "timeFromStart": 90,
      "isBoardingPoint": true,
      "isDropPoint": true
    },
    {
      "pointName": "Pune",
      "address": "Pune Railway Station",
      "latitude": 18.5204,
      "longitude": 73.8567,
      "sequenceOrder": 3,
      "distanceFromStart": 150000,
      "timeFromStart": 180,
      "isBoardingPoint": false,
      "isDropPoint": true
    }
  ]
}
```

### Trip Management

#### Create Trip
```
POST /api/trips
Content-Type: application/json

{
  "routeId": 1,
  "vehicleId": 1,
  "driverId": 1,
  "departureTime": "2024-12-25T08:00:00",
  "basePricePerKm": 10.0,
  "specialInstructions": "Please be on time"
}
```

#### Search Trips
```
POST /api/trips/search
Content-Type: application/json

{
  "boardingPoint": "Mumbai Central",
  "dropPoint": "Pune",
  "travelDate": "2024-12-25",
  "requiredSeats": 2
}
```

Response includes:
- Driver details (name, phone, rating)
- Vehicle details (brand, model, color, type)
- Timing (departure, arrival)
- Price calculation
- Available seats

#### Get Seat Availability
```
GET /api/trips/{tripId}/seats
```

Response:
```json
{
  "success": true,
  "message": "Seat availability retrieved successfully",
  "data": {
    "tripId": 1,
    "totalSeats": 6,
    "availableSeats": 4,
    "seats": [
      {
        "seatNumber": "S1",
        "isAvailable": true,
        "isDriverSeat": false
      },
      {
        "seatNumber": "S2",
        "isAvailable": false,
        "isDriverSeat": false
      }
    ]
  }
}
```

#### Get All Boarding Points
```
GET /api/trips/boarding-points
```

#### Get All Drop Points
```
GET /api/trips/drop-points
```

### Booking Management

#### Create Booking
```
POST /api/bookings
Content-Type: application/json

{
  "userId": 2,
  "tripId": 1,
  "boardingPointId": 1,
  "dropPointId": 3,
  "seatNumbers": ["S1", "S2"],
  "passengerNames": "John Doe, Jane Doe",
  "passengerContacts": "9876543210, 9876543211"
}
```

Response includes:
- Booking reference number
- Total amount calculated based on distance
- Booking status (PENDING initially)

#### Get User Bookings
```
GET /api/bookings/user/{userId}
```

#### Get Booking by Reference
```
GET /api/bookings/reference/{bookingReference}
```

#### Cancel Booking
```
PUT /api/bookings/{bookingId}/cancel
```

### Payment Management

#### Process Payment
```
POST /api/payments/process
Content-Type: application/json

{
  "bookingId": 1,
  "paymentMethod": "UPI",
  "cardDetails": "encrypted_card_details"
}
```

Response:
- Transaction ID
- Payment status (SUCCESS/FAILED)
- On success, booking status changes to CONFIRMED

#### Get Payment by Booking ID
```
GET /api/payments/booking/{bookingId}
```

#### Get Payment by Transaction ID
```
GET /api/payments/transaction/{transactionId}
```

### Prerequisites
- Java 17 or higher
- PostgreSQL 12+
- Maven 3.8+

### Database Setup

1. Create PostgreSQL database:
```sql
CREATE DATABASE carpolling_db;
```

2. Update database credentials in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carpolling_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Clone the repository
2. Navigate to project directory
3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8010`

## 📦 For UI/Frontend Developers

If you're developing a frontend application that consumes this API:

1. **Start the backend using Docker:**
   ```bash
   docker-compose up -d
   ```

2. **API Base URL:** `http://localhost:8010`

3. **View API Documentation:** 
   - Swagger UI: http://localhost:8010/swagger-ui.html
   - OpenAPI JSON: http://localhost:8010/api-docs

4. **CORS:** The application is configured to allow cross-origin requests for development

5. **Test the API:**
   ```bash
   # Health check
   curl http://localhost:8010/actuator/health
   ```

6. **Stop the backend:**
   ```bash
   docker-compose down
   ```

## Database Indexes

The application includes optimized indexes for:
- Trip search by date and status
- Booking queries by user and trip
- Payment queries by booking and transaction

## Key Features Implemented

### Efficient Search
- Route-based search with boarding and drop point filtering
- Optimized queries with proper indexing
- Date-range based trip availability

### Seat Management
- Automatic seat generation for trips
- Driver seat excluded from passenger booking
- Real-time seat availability updates
- Concurrent booking handling with transactions

### Pricing Calculation
- Dynamic pricing based on distance traveled
- Price per kilometer configured by driver
- Automatic calculation based on boarding and drop points

### Booking Flow
1. User searches trips
2. Selects trip and views available seats
3. Books seats (status: PENDING)
4. Makes payment
5. Booking confirmed (status: CONFIRMED)

### Payment Integration
- Multiple payment methods support
- Transaction tracking
- Payment gateway simulation (ready for real gateway integration)
- Automatic booking confirmation on successful payment

## Security

- Password encryption using BCrypt
- CORS enabled for cross-origin requests
- JWT token support (placeholder for implementation)
- Input validation on all endpoints

## Error Handling

- Global exception handler
- Validation error responses
- Meaningful error messages
- Proper HTTP status codes

## Future Enhancements

1. **JWT Authentication**: Full JWT implementation for secure API access
2. **Real Payment Gateway**: Integration with Razorpay/Stripe
3. **Real-time Updates**: WebSocket for live seat availability
4. **Notifications**: Email/SMS notifications for bookings
5. **Reviews & Ratings**: Passenger can rate drivers
6. **Trip Tracking**: GPS-based live trip tracking
7. **Cancellation Policies**: Refund rules based on time
8. **Admin Panel**: Manage drivers, users, and verify documents
9. **Analytics**: Dashboard for drivers and admin
10. **Search Filters**: Filter by price, rating, vehicle type

## Testing

To test the APIs, you can use:
- Postman
- curl commands
- Swagger UI (if enabled): http://localhost:8080/swagger-ui.html

## Support

For issues or questions, please contact the development team.








# carpolling-system
