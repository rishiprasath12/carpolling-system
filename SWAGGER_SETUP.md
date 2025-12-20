# Swagger/OpenAPI Configuration

## Overview
Swagger (OpenAPI 3.0) has been successfully integrated into the Carpooling application using SpringDoc OpenAPI.

## What Was Done

### 1. Dependencies Added
- **springdoc-openapi-starter-webmvc-ui** (v2.3.0) - Added to `pom.xml`
  - Provides Swagger UI and OpenAPI documentation generation

### 2. Configuration Files

#### SwaggerConfig.java
Created a comprehensive Swagger configuration with:
- API title, description, and version
- JWT Bearer authentication setup
- Contact and license information
- Custom ObjectMapper to prevent circular reference issues

#### Updated AppConfig.java
- Configured Jackson ObjectMapper to handle:
  - Java 8 date/time types (LocalDateTime)
  - Null value exclusion
  - Prevention of empty bean serialization failures

#### Updated SecurityConfig.java
- Added explicit permissions for Swagger endpoints:
  - `/swagger-ui/**`
  - `/swagger-ui.html`
  - `/v3/api-docs/**`
  - `/api-docs/**`

### 3. Entity Updates
Added `@JsonIgnoreProperties` to all JPA entities to prevent circular reference issues:
- Trip, Booking, User, Driver, Vehicle
- Route, RoutePoint, Payment, TripSeat

This prevents Swagger from failing when trying to serialize entities with bidirectional relationships.

### 4. Application Properties
Enhanced Swagger configuration with:
- Custom API docs path: `/api-docs`
- Custom Swagger UI path: `/swagger-ui.html`
- Operations sorted by HTTP method
- Tags sorted alphabetically
- Try-it-out feature enabled

### 5. Fixed Java Version
Updated `pom.xml` from Java 21 to Java 17 to match your system's Java version.

## How to Access

1. **Start the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Access Swagger UI:**
   - URL: http://localhost:8010/swagger-ui.html
   - You'll see an interactive API documentation interface

3. **Access OpenAPI JSON:**
   - URL: http://localhost:8010/api-docs
   - Raw OpenAPI 3.0 specification in JSON format

## Using Swagger UI

### Testing Public Endpoints
1. Expand any endpoint (e.g., `/api/users/register`)
2. Click "Try it out"
3. Fill in the request body
4. Click "Execute"
5. View the response

### Testing Protected Endpoints (with JWT)
1. First, login via `/api/users/login` to get a JWT token
2. Copy the token from the response
3. Click the **"Authorize"** button at the top right
4. In the dialog, paste your JWT token (without "Bearer " prefix)
5. Click "Authorize"
6. Now you can test protected endpoints

## Available API Groups

The Swagger UI organizes endpoints by controller:
- **Admin Controller** - Admin dashboard and management
- **User Controller** - User registration and authentication
- **Driver Controller** - Driver registration and management
- **Trip Controller** - Trip creation and search
- **Booking Controller** - Booking creation and management
- **Payment Controller** - Payment processing

## Troubleshooting

### If you get a 500 error on /api-docs:
- Ensure the application compiled successfully
- Check that no circular references exist in entities
- Verify all @JsonIgnoreProperties annotations are in place

### If Swagger UI doesn't load:
- Check that the application is running on port 8010
- Verify Security Config allows Swagger endpoints
- Clear browser cache and try again

### If authentication doesn't work:
- Make sure to use the JWT token without the "Bearer " prefix
- Token should be just the encoded JWT string
- Click "Authorize" button before testing protected endpoints

## Benefits

✅ **Interactive Documentation** - Test APIs directly from the browser
✅ **Auto-Generated** - Documentation updates automatically with code changes
✅ **JWT Integration** - Built-in authentication support
✅ **Request/Response Models** - See all DTO structures
✅ **No Manual Maintenance** - Annotations drive the documentation

## Next Steps

Consider adding:
1. `@Operation` annotations on controller methods for detailed descriptions
2. `@ApiResponse` annotations for different response codes
3. `@Schema` annotations on DTOs for field-level documentation
4. Example values using `@Schema(example = "...")`

Example:
```java
@Operation(summary = "Create a new trip", description = "Creates a new carpooling trip for a driver")
@ApiResponse(responseCode = "201", description = "Trip created successfully")
@ApiResponse(responseCode = "400", description = "Invalid request data")
@PostMapping
public ResponseEntity<ApiResponse<Trip>> createTrip(@Valid @RequestBody TripCreationRequest request) {
    // implementation
}
```






