# API Documentation - Car Pooling Application

## Base URL
```
http://localhost:8080
```

## Response Format

All API responses follow this format:
```json
{
  "success": true/false,
  "message": "Response message",
  "data": { ... }
}
```

---

## 1. User Management APIs

### 1.1 Register User

Register a new user (passenger or driver).

**Endpoint:** `POST /api/users/register`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210",
  "password": "securePassword123",
  "role": "PASSENGER"
}
```

**Role Values:** `PASSENGER`, `DRIVER`, `BOTH`

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "role": "PASSENGER",
    "token": "JWT_TOKEN_HERE"
  }
}
```

### 1.2 Login

Authenticate user and get token.

**Endpoint:** `POST /api/users/login`

**Request Body:**
```json
{
  "emailOrPhone": "john@example.com",
  "password": "securePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "role": "PASSENGER",
    "token": "JWT_TOKEN_HERE"
  }
}
```

---

## 2. Driver Management APIs

### 2.1 Register as Driver

Register driver profile for an existing user.

**Endpoint:** `POST /api/drivers/register`

**Request Body:**
```json
{
  "userId": 1,
  "licenseNumber": "DL1234567890",
  "licenseExpiryDate": "2025-12-31",
  "experienceYears": 5,
  "additionalInfo": "Professional driver with 5 years experience"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Driver registered successfully",
  "data": {
    "id": 1,
    "user": { ... },
    "licenseNumber": "DL1234567890",
    "licenseExpiryDate": "2025-12-31",
    "experienceYears": 5,
    "isVerified": false,
    "rating": 0.0,
    "totalTrips": 0
  }
}
```

### 2.2 Register Vehicle

Add a vehicle to driver's profile.

**Endpoint:** `POST /api/drivers/vehicles`

**Request Body:**
```json
{
  "driverId": 1,
  "registrationNumber": "MH01AB1234",
  "brand": "Toyota",
  "model": "Innova Crysta",
  "color": "White",
  "manufacturingYear": 2020,
  "vehicleType": "SUV",
  "totalSeats": 7,
  "hasAC": true,
  "features": "GPS Navigation, Premium Sound System, USB Charging"
}
```

**Vehicle Types:** `SEDAN`, `SUV`, `HATCHBACK`, `MUV`, `LUXURY`

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Vehicle registered successfully",
  "data": {
    "id": 1,
    "registrationNumber": "MH01AB1234",
    "brand": "Toyota",
    "model": "Innova Crysta",
    "passengerSeats": 6,
    "isActive": true
  }
}
```

### 2.3 Get Driver Vehicles

Get all vehicles registered by a driver.

**Endpoint:** `GET /api/drivers/{driverId}/vehicles`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Vehicles retrieved successfully",
  "data": [
    {
      "id": 1,
      "registrationNumber": "MH01AB1234",
      "brand": "Toyota",
      "model": "Innova Crysta",
      "totalSeats": 7,
      "passengerSeats": 6
    }
  ]
}
```

### 2.4 Create Route

Create a route with multiple points.

**Endpoint:** `POST /api/drivers/routes`

**Request Body:**
```json
{
  "driverId": 1,
  "routeName": "Mumbai to Pune via Lonavala",
  "totalDistance": 150.5,
  "estimatedDuration": 180,
  "routePoints": [
    {
      "pointName": "Mumbai Central",
      "address": "Mumbai Central Railway Station, Mumbai",
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
      "address": "Lonavala Bus Stand, Lonavala",
      "latitude": 18.7537,
      "longitude": 73.4086,
      "sequenceOrder": 2,
      "distanceFromStart": 83,
      "timeFromStart": 90,
      "isBoardingPoint": true,
      "isDropPoint": true
    },
    {
      "pointName": "Pune Station",
      "address": "Pune Railway Station, Pune",
      "latitude": 18.5204,
      "longitude": 73.8567,
      "sequenceOrder": 3,
      "distanceFromStart": 150,
      "timeFromStart": 180,
      "isBoardingPoint": false,
      "isDropPoint": true
    }
  ]
}
```

**Note:** 
- `distanceFromStart` is in kilometers
- `timeFromStart` is in minutes
- Points must be ordered by `sequenceOrder`

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Route created successfully",
  "data": {
    "id": 1,
    "routeName": "Mumbai to Pune via Lonavala",
    "totalDistance": 150.5,
    "estimatedDuration": 180,
    "routePoints": [ ... ]
  }
}
```

---

## 3. Trip Management APIs

### 3.1 Create Trip

Schedule a trip on a route.

**Endpoint:** `POST /api/trips`

**Request Body:**
```json
{
  "routeId": 1,
  "vehicleId": 1,
  "driverId": 1,
  "departureTime": "2024-12-25T08:00:00",
  "basePricePerKm": 10.0,
  "specialInstructions": "Please arrive 10 minutes before departure"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Trip created successfully",
  "data": {
    "id": 1,
    "departureTime": "2024-12-25T08:00:00",
    "estimatedArrivalTime": "2024-12-25T11:00:00",
    "availableSeats": 6,
    "status": "SCHEDULED"
  }
}
```

### 3.2 Search Trips

Search for available trips between two points on a date.

**Endpoint:** `POST /api/trips/search`

**Request Body:**
```json
{
  "boardingPoint": "Mumbai Central",
  "dropPoint": "Pune Station",
  "travelDate": "2024-12-25",
  "requiredSeats": 2
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Trips retrieved successfully",
  "data": [
    {
      "tripId": 1,
      "driverName": "Jane Smith",
      "driverPhone": "9876543211",
      "driverRating": 4.5,
      "vehicleBrand": "Toyota",
      "vehicleModel": "Innova Crysta",
      "vehicleColor": "White",
      "registrationNumber": "MH01AB1234",
      "vehicleType": "SUV",
      "hasAC": true,
      "departureTime": "2024-12-25T08:00:00",
      "arrivalTime": "2024-12-25T11:00:00",
      "availableSeats": 6,
      "pricePerKm": 10.0,
      "totalPrice": 1500.0,
      "distance": 150.0,
      "duration": 180,
      "routeName": "Mumbai to Pune via Lonavala"
    }
  ]
}
```

### 3.3 Get Seat Availability

Get seat layout and availability for a trip.

**Endpoint:** `GET /api/trips/{tripId}/seats`

**Success Response (200 OK):**
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
      },
      {
        "seatNumber": "S3",
        "isAvailable": true,
        "isDriverSeat": false
      }
    ]
  }
}
```

### 3.4 Get All Boarding Points

Get list of all boarding points available in the system.

**Endpoint:** `GET /api/trips/boarding-points`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Boarding points retrieved successfully",
  "data": [
    "Mumbai Central",
    "Lonavala",
    "Thane",
    "Panvel"
  ]
}
```

### 3.5 Get All Drop Points

Get list of all drop points available in the system.

**Endpoint:** `GET /api/trips/drop-points`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Drop points retrieved successfully",
  "data": [
    "Pune Station",
    "Lonavala",
    "Khandala",
    "Talegaon"
  ]
}
```

---

## 4. Booking Management APIs

### 4.1 Create Booking

Book seats on a trip.

**Endpoint:** `POST /api/bookings`

**Request Body:**
```json
{
  "userId": 1,
  "tripId": 1,
  "boardingPointId": 1,
  "dropPointId": 3,
  "seatNumbers": ["S1", "S2"],
  "passengerNames": "John Doe, Jane Doe",
  "passengerContacts": "9876543210, 9876543211"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "id": 1,
    "bookingReference": "BK20241125093045",
    "numberOfSeats": 2,
    "seatNumbers": ["S1", "S2"],
    "totalAmount": 1500.0,
    "status": "PENDING"
  }
}
```

### 4.2 Get User Bookings

Get all bookings for a user.

**Endpoint:** `GET /api/bookings/user/{userId}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Bookings retrieved successfully",
  "data": [
    {
      "bookingId": 1,
      "bookingReference": "BK20241125093045",
      "tripId": 1,
      "boardingPoint": "Mumbai Central",
      "dropPoint": "Pune Station",
      "departureTime": "2024-12-25T08:00:00",
      "seatNumbers": ["S1", "S2"],
      "totalAmount": 1500.0,
      "status": "CONFIRMED",
      "driverName": "Jane Smith",
      "driverPhone": "9876543211",
      "vehicleDetails": "Toyota Innova Crysta (White) - MH01AB1234",
      "bookedAt": "2024-11-25T09:30:45"
    }
  ]
}
```

### 4.3 Get Booking by Reference

Get booking details by booking reference number.

**Endpoint:** `GET /api/bookings/reference/{bookingReference}`

**Example:** `GET /api/bookings/reference/BK20241125093045`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Booking retrieved successfully",
  "data": {
    "bookingId": 1,
    "bookingReference": "BK20241125093045",
    "tripId": 1,
    "boardingPoint": "Mumbai Central",
    "dropPoint": "Pune Station",
    "departureTime": "2024-12-25T08:00:00",
    "seatNumbers": ["S1", "S2"],
    "totalAmount": 1500.0,
    "status": "CONFIRMED"
  }
}
```

### 4.4 Cancel Booking

Cancel an existing booking.

**Endpoint:** `PUT /api/bookings/{bookingId}/cancel`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Booking cancelled successfully",
  "data": {
    "id": 1,
    "bookingReference": "BK20241125093045",
    "status": "CANCELLED"
  }
}
```

---

## 5. Payment Management APIs

### 5.1 Process Payment

Process payment for a booking.

**Endpoint:** `POST /api/payments/process`

**Request Body:**
```json
{
  "bookingId": 1,
  "paymentMethod": "UPI",
  "cardDetails": "encrypted_payment_details"
}
```

**Payment Methods:** `CREDIT_CARD`, `DEBIT_CARD`, `UPI`, `NET_BANKING`, `WALLET`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Payment processed successfully",
  "data": {
    "id": 1,
    "transactionId": "TXN20241125093212ABC123",
    "amount": 1500.0,
    "paymentMethod": "UPI",
    "status": "SUCCESS",
    "paidAt": "2024-11-25T09:32:12"
  }
}
```

**Note:** On successful payment, booking status automatically changes to `CONFIRMED`.

### 5.2 Get Payment by Booking ID

Get payment details for a booking.

**Endpoint:** `GET /api/payments/booking/{bookingId}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Payment retrieved successfully",
  "data": {
    "id": 1,
    "transactionId": "TXN20241125093212ABC123",
    "amount": 1500.0,
    "paymentMethod": "UPI",
    "status": "SUCCESS",
    "paidAt": "2024-11-25T09:32:12"
  }
}
```

### 5.3 Get Payment by Transaction ID

Get payment details by transaction ID.

**Endpoint:** `GET /api/payments/transaction/{transactionId}`

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Payment retrieved successfully",
  "data": {
    "id": 1,
    "transactionId": "TXN20241125093212ABC123",
    "bookingId": 1,
    "amount": 1500.0,
    "status": "SUCCESS"
  }
}
```

---

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null
}
```

### Unauthorized (401 Unauthorized)
```json
{
  "success": false,
  "message": "Invalid credentials",
  "data": null
}
```

### Not Found (404 Not Found)
```json
{
  "success": false,
  "message": "Resource not found",
  "data": null
}
```

### Server Error (500 Internal Server Error)
```json
{
  "success": false,
  "message": "An error occurred: [error details]",
  "data": null
}
```

---

## Workflow Examples

### Complete Passenger Booking Flow

1. **Register/Login**
   ```
   POST /api/users/register
   or
   POST /api/users/login
   ```

2. **Search Trips**
   ```
   POST /api/trips/search
   ```

3. **View Seat Availability**
   ```
   GET /api/trips/{tripId}/seats
   ```

4. **Create Booking**
   ```
   POST /api/bookings
   ```

5. **Process Payment**
   ```
   POST /api/payments/process
   ```

6. **View Booking Details**
   ```
   GET /api/bookings/reference/{bookingReference}
   ```

### Complete Driver Onboarding Flow

1. **Register as User**
   ```
   POST /api/users/register (role: DRIVER)
   ```

2. **Register as Driver**
   ```
   POST /api/drivers/register
   ```

3. **Register Vehicle**
   ```
   POST /api/drivers/vehicles
   ```

4. **Create Route**
   ```
   POST /api/drivers/routes
   ```

5. **Create Trip**
   ```
   POST /api/trips
   ```

---

## Notes

- All timestamps are in ISO 8601 format
- Prices are in the base currency (e.g., INR)
- Distances are in kilometers
- Durations are in minutes
- All endpoints support CORS for cross-origin requests
- Authentication tokens should be included in headers (when JWT is fully implemented)









