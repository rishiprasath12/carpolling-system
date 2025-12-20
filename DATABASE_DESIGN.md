# Database Design Documentation

## Overview

The Car Pooling Application database is designed for PostgreSQL with a focus on:
- **Normalization**: Eliminating redundancy while maintaining performance
- **Scalability**: Support for growing data with proper indexing
- **Integrity**: Foreign key constraints and data validation
- **Performance**: Strategic indexes on frequently queried columns

---

## Entity Relationship Diagram

```
┌─────────┐        ┌─────────┐        ┌──────────┐
│  Users  │◄──────►│ Drivers │◄──────►│ Vehicles │
└────┬────┘        └────┬────┘        └────┬─────┘
     │                  │                   │
     │                  │                   │
     │                  ▼                   │
     │            ┌─────────┐               │
     │            │  Routes │               │
     │            └────┬────┘               │
     │                 │                    │
     │                 ▼                    │
     │          ┌─────────────┐             │
     │          │ RoutePoints │             │
     │          └─────────────┘             │
     │                 │                    │
     │                 ▼                    │
     │            ┌─────────┐◄──────────────┘
     │            │  Trips  │
     │            └────┬────┘
     │                 │
     │                 ├──────────┐
     │                 ▼          ▼
     │          ┌──────────┐  ┌──────────┐
     │          │TripSeats │  │ Bookings │◄───┐
     │          └──────────┘  └────┬─────┘    │
     │                              │          │
     └──────────────────────────────┘          │
                                               │
                                        ┌──────┴────┐
                                        │  Payments │
                                        └───────────┘
```

---

## Tables

### 1. users

**Purpose**: Store all user information (passengers and drivers)

| Column     | Type         | Constraints           | Description                    |
|------------|--------------|-----------------------|--------------------------------|
| id         | BIGSERIAL    | PRIMARY KEY           | Unique user identifier         |
| name       | VARCHAR(255) | NOT NULL              | User's full name               |
| email      | VARCHAR(255) | NOT NULL, UNIQUE      | Email address                  |
| phone      | VARCHAR(20)  | NOT NULL, UNIQUE      | Phone number                   |
| password   | VARCHAR(255) | NOT NULL              | Encrypted password             |
| role       | VARCHAR(50)  | NOT NULL, CHECK       | PASSENGER/DRIVER/BOTH          |
| is_active  | BOOLEAN      | NOT NULL, DEFAULT true| Account status                 |
| created_at | TIMESTAMP    | NOT NULL, DEFAULT NOW | Account creation time          |
| updated_at | TIMESTAMP    | NOT NULL, DEFAULT NOW | Last update time               |

**Indexes:**
- Primary key on `id`
- Unique index on `email`
- Unique index on `phone`

**Business Rules:**
- Email and phone must be unique across system
- Password stored as BCrypt hash
- Soft delete using `is_active` flag

---

### 2. drivers

**Purpose**: Store driver-specific information

| Column              | Type         | Constraints              | Description                |
|---------------------|--------------|--------------------------|----------------------------|
| id                  | BIGSERIAL    | PRIMARY KEY              | Unique driver identifier   |
| user_id             | BIGINT       | NOT NULL, UNIQUE, FK     | Reference to users table   |
| license_number      | VARCHAR(50)  | NOT NULL, UNIQUE         | Driving license number     |
| license_expiry_date | VARCHAR(50)  | NOT NULL                 | License expiry date        |
| experience_years    | INTEGER      | NOT NULL                 | Years of driving experience|
| additional_info     | TEXT         |                          | Additional information     |
| is_verified         | BOOLEAN      | NOT NULL, DEFAULT false  | Verification status        |
| rating              | DOUBLE       | NOT NULL, DEFAULT 0.0    | Average driver rating      |
| total_trips         | INTEGER      | NOT NULL, DEFAULT 0      | Number of trips completed  |
| created_at          | TIMESTAMP    | NOT NULL, DEFAULT NOW    | Registration time          |

**Indexes:**
- Primary key on `id`
- Unique index on `user_id`
- Unique index on `license_number`

**Relationships:**
- One-to-One with `users` (user_id)
- One-to-Many with `vehicles`
- One-to-Many with `routes`
- One-to-Many with `trips`

**Business Rules:**
- Each user can have only one driver profile
- License number must be unique
- Rating calculated from passenger reviews (0.0 to 5.0)

---

### 3. vehicles

**Purpose**: Store vehicle information registered by drivers

| Column              | Type         | Constraints           | Description                    |
|---------------------|--------------|-----------------------|--------------------------------|
| id                  | BIGSERIAL    | PRIMARY KEY           | Unique vehicle identifier      |
| driver_id           | BIGINT       | NOT NULL, FK          | Reference to drivers table     |
| registration_number | VARCHAR(50)  | NOT NULL, UNIQUE      | Vehicle registration number    |
| brand               | VARCHAR(100) | NOT NULL              | Vehicle brand (Toyota, Honda)  |
| model               | VARCHAR(100) | NOT NULL              | Vehicle model (Innova, City)   |
| color               | VARCHAR(50)  | NOT NULL              | Vehicle color                  |
| manufacturing_year  | INTEGER      | NOT NULL              | Year of manufacture            |
| vehicle_type        | VARCHAR(50)  | NOT NULL, CHECK       | SEDAN/SUV/HATCHBACK/MUV/LUXURY |
| total_seats         | INTEGER      | NOT NULL              | Total seats including driver   |
| passenger_seats     | INTEGER      | NOT NULL              | Seats available for passengers |
| has_ac              | BOOLEAN      | NOT NULL, DEFAULT true| Air conditioning availability  |
| features            | TEXT         |                       | Additional features (JSON/CSV) |
| is_active           | BOOLEAN      | NOT NULL, DEFAULT true| Vehicle active status          |
| created_at          | TIMESTAMP    | NOT NULL, DEFAULT NOW | Registration time              |

**Indexes:**
- Primary key on `id`
- Unique index on `registration_number`
- Index on `driver_id`

**Relationships:**
- Many-to-One with `drivers` (driver_id)
- One-to-Many with `trips`

**Business Rules:**
- Registration number must be unique
- `passenger_seats = total_seats - 1` (excluding driver)
- One driver can have multiple vehicles

---

### 4. routes

**Purpose**: Store route information created by drivers

| Column             | Type         | Constraints           | Description                |
|--------------------|--------------|-----------------------|----------------------------|
| id                 | BIGSERIAL    | PRIMARY KEY           | Unique route identifier    |
| driver_id          | BIGINT       | NOT NULL, FK          | Reference to drivers table |
| route_name         | VARCHAR(255) | NOT NULL              | Route display name         |
| total_distance     | DOUBLE       | NOT NULL              | Total distance in km       |
| estimated_duration | INTEGER      | NOT NULL              | Duration in minutes        |
| is_active          | BOOLEAN      | NOT NULL, DEFAULT true| Route active status        |
| created_at         | TIMESTAMP    | NOT NULL, DEFAULT NOW | Route creation time        |

**Indexes:**
- Primary key on `id`
- Index on `driver_id`

**Relationships:**
- Many-to-One with `drivers` (driver_id)
- One-to-Many with `route_points`
- One-to-Many with `trips`

**Business Rules:**
- One driver can create multiple routes
- Routes are reusable for multiple trips

---

### 5. route_points

**Purpose**: Store sequential points in a route (A → B → C → D)

| Column             | Type         | Constraints           | Description                    |
|--------------------|--------------|-----------------------|--------------------------------|
| id                 | BIGSERIAL    | PRIMARY KEY           | Unique point identifier        |
| route_id           | BIGINT       | NOT NULL, FK          | Reference to routes table      |
| point_name         | VARCHAR(255) | NOT NULL              | Point display name             |
| address            | VARCHAR(500) | NOT NULL              | Full address                   |
| latitude           | DOUBLE       | NOT NULL              | GPS latitude                   |
| longitude          | DOUBLE       | NOT NULL              | GPS longitude                  |
| sequence_order     | INTEGER      | NOT NULL              | Order in route (1, 2, 3...)    |
| distance_from_start| INTEGER      | NOT NULL              | Distance from start (meters)   |
| time_from_start    | INTEGER      | NOT NULL              | Time from start (minutes)      |
| is_boarding_point  | BOOLEAN      | NOT NULL, DEFAULT true| Can passengers board here?     |
| is_drop_point      | BOOLEAN      | NOT NULL, DEFAULT true| Can passengers drop here?      |

**Indexes:**
- Primary key on `id`
- Index on `route_id`
- Composite index on `(route_id, sequence_order)`

**Relationships:**
- Many-to-One with `routes` (route_id)
- Referenced by `bookings` for boarding/drop points

**Business Rules:**
- Points ordered by `sequence_order`
- First point: boarding only, Last point: drop only
- Middle points: both boarding and drop

**Example:**
```
Point 1: Mumbai (0 km, 0 min) - Boarding only
Point 2: Lonavala (83 km, 90 min) - Boarding & Drop
Point 3: Pune (150 km, 180 min) - Drop only
```

---

### 6. trips

**Purpose**: Store scheduled trip instances

| Column                | Type         | Constraints           | Description                    |
|-----------------------|--------------|-----------------------|--------------------------------|
| id                    | BIGSERIAL    | PRIMARY KEY           | Unique trip identifier         |
| route_id              | BIGINT       | NOT NULL, FK          | Reference to routes table      |
| vehicle_id            | BIGINT       | NOT NULL, FK          | Reference to vehicles table    |
| driver_id             | BIGINT       | NOT NULL, FK          | Reference to drivers table     |
| departure_time        | TIMESTAMP    | NOT NULL              | Scheduled departure time       |
| estimated_arrival_time| TIMESTAMP    | NOT NULL              | Estimated arrival time         |
| base_price_per_km     | DOUBLE       | NOT NULL              | Pricing per kilometer          |
| available_seats       | INTEGER      | NOT NULL              | Currently available seats      |
| booked_seats          | INTEGER      | NOT NULL, DEFAULT 0   | Number of booked seats         |
| status                | VARCHAR(50)  | NOT NULL, DEFAULT 'SCHEDULED' | Trip status          |
| special_instructions  | TEXT         |                       | Driver instructions            |
| created_at            | TIMESTAMP    | NOT NULL, DEFAULT NOW | Trip creation time             |
| updated_at            | TIMESTAMP    | NOT NULL, DEFAULT NOW | Last update time               |

**Status Values:** `SCHEDULED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`

**Indexes:**
- Primary key on `id`
- Index on `departure_time` (for date searches)
- Index on `status` (for filtering)
- Composite index on `(route_id, departure_time, status)`

**Relationships:**
- Many-to-One with `routes` (route_id)
- Many-to-One with `vehicles` (vehicle_id)
- Many-to-One with `drivers` (driver_id)
- One-to-Many with `trip_seats`
- One-to-Many with `bookings`

**Business Rules:**
- `available_seats + booked_seats ≤ vehicle.passenger_seats`
- Vehicle must belong to the driver
- Cannot book if `available_seats = 0`

---

### 7. trip_seats

**Purpose**: Track seat-level availability for each trip

| Column        | Type        | Constraints              | Description                |
|---------------|-------------|--------------------------|----------------------------|
| id            | BIGSERIAL   | PRIMARY KEY              | Unique seat identifier     |
| trip_id       | BIGINT      | NOT NULL, FK             | Reference to trips table   |
| seat_number   | VARCHAR(10) | NOT NULL                 | Seat identifier (S1, S2)   |
| is_available  | BOOLEAN     | NOT NULL, DEFAULT true   | Seat availability status   |
| is_driver_seat| BOOLEAN     | NOT NULL, DEFAULT false  | Is this driver's seat?     |

**Constraints:**
- UNIQUE (trip_id, seat_number)

**Indexes:**
- Primary key on `id`
- Unique index on `(trip_id, seat_number)`

**Relationships:**
- Many-to-One with `trips` (trip_id)

**Business Rules:**
- Driver seat (D1) always unavailable
- Passenger seats (S1, S2, ...) initially available
- Seat marked unavailable when booked

---

### 8. bookings

**Purpose**: Store booking information

| Column            | Type         | Constraints           | Description                    |
|-------------------|--------------|-----------------------|--------------------------------|
| id                | BIGSERIAL    | PRIMARY KEY           | Unique booking identifier      |
| booking_reference | VARCHAR(50)  | NOT NULL, UNIQUE      | Booking reference number       |
| user_id           | BIGINT       | NOT NULL, FK          | Reference to users table       |
| trip_id           | BIGINT       | NOT NULL, FK          | Reference to trips table       |
| boarding_point_id | BIGINT       | NOT NULL, FK          | Reference to route_points      |
| drop_point_id     | BIGINT       | NOT NULL, FK          | Reference to route_points      |
| number_of_seats   | INTEGER      | NOT NULL              | Number of seats booked         |
| total_amount      | DOUBLE       | NOT NULL              | Total booking amount           |
| distance          | DOUBLE       | NOT NULL              | Distance traveled (km)         |
| status            | VARCHAR(50)  | NOT NULL, DEFAULT 'PENDING' | Booking status           |
| passenger_names   | TEXT         |                       | Names of passengers            |
| passenger_contacts| TEXT         |                       | Contact numbers of passengers  |
| created_at        | TIMESTAMP    | NOT NULL, DEFAULT NOW | Booking creation time          |
| updated_at        | TIMESTAMP    | NOT NULL, DEFAULT NOW | Last update time               |

**Status Values:** `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED`, `REFUNDED`

**Indexes:**
- Primary key on `id`
- Unique index on `booking_reference`
- Index on `user_id`
- Index on `trip_id`
- Index on `status`

**Relationships:**
- Many-to-One with `users` (user_id)
- Many-to-One with `trips` (trip_id)
- Many-to-One with `route_points` (boarding_point_id)
- Many-to-One with `route_points` (drop_point_id)
- One-to-One with `payments`
- One-to-Many with `booking_seats`

**Business Rules:**
- `boarding_point.sequence_order < drop_point.sequence_order`
- `total_amount = distance * trip.base_price_per_km * number_of_seats`
- Status changes: PENDING → CONFIRMED (on payment success)

---

### 9. booking_seats

**Purpose**: Store which specific seats are booked

| Column       | Type        | Constraints | Description                |
|--------------|-------------|-------------|----------------------------|
| booking_id   | BIGINT      | NOT NULL, FK| Reference to bookings table|
| seat_number  | VARCHAR(10) | NOT NULL    | Seat identifier (S1, S2)   |

**Indexes:**
- Index on `booking_id`

**Relationships:**
- Many-to-One with `bookings` (booking_id)

**Business Rules:**
- Stored as collection for easy retrieval
- Cross-checked with `trip_seats` for availability

---

### 10. payments

**Purpose**: Store payment transaction information

| Column                 | Type         | Constraints              | Description                    |
|------------------------|--------------|--------------------------|--------------------------------|
| id                     | BIGSERIAL    | PRIMARY KEY              | Unique payment identifier      |
| transaction_id         | VARCHAR(100) | NOT NULL, UNIQUE         | Payment gateway transaction ID |
| booking_id             | BIGINT       | NOT NULL, UNIQUE, FK     | Reference to bookings table    |
| amount                 | DOUBLE       | NOT NULL                 | Payment amount                 |
| payment_method         | VARCHAR(50)  | NOT NULL, CHECK          | Payment method used            |
| status                 | VARCHAR(50)  | NOT NULL, DEFAULT 'INITIATED' | Payment status            |
| payment_gateway_response| TEXT         |                          | Gateway response (JSON)        |
| paid_at                | TIMESTAMP    |                          | Payment completion time        |
| failure_reason         | TEXT         |                          | Reason if payment failed       |
| created_at             | TIMESTAMP    | NOT NULL, DEFAULT NOW    | Payment initiation time        |
| updated_at             | TIMESTAMP    | NOT NULL, DEFAULT NOW    | Last update time               |

**Payment Methods:** `CREDIT_CARD`, `DEBIT_CARD`, `UPI`, `NET_BANKING`, `WALLET`

**Status Values:** `INITIATED`, `SUCCESS`, `FAILED`, `REFUNDED`, `PENDING`

**Indexes:**
- Primary key on `id`
- Unique index on `transaction_id`
- Unique index on `booking_id`
- Index on `status`

**Relationships:**
- One-to-One with `bookings` (booking_id)

**Business Rules:**
- One payment per booking
- On SUCCESS: booking.status → CONFIRMED
- On FAILED: booking remains PENDING (can retry)

---

## Query Optimization Strategies

### 1. Trip Search Query

**Requirement:** Find trips between two points on a date

**Optimized Query:**
```sql
SELECT DISTINCT t.*
FROM trips t
JOIN routes r ON t.route_id = r.id
JOIN route_points rp1 ON r.id = rp1.route_id
JOIN route_points rp2 ON r.id = rp2.route_id
WHERE t.departure_time::date = '2024-12-25'
  AND t.status = 'SCHEDULED'
  AND t.available_seats > 0
  AND rp1.point_name = 'Mumbai Central'
  AND rp2.point_name = 'Pune'
  AND rp1.sequence_order < rp2.sequence_order;
```

**Indexes Used:**
- `idx_trip_date` on trips(departure_time)
- `idx_trip_status` on trips(status)

---

### 2. User Booking History

**Query:**
```sql
SELECT b.*, t.departure_time, p.status as payment_status
FROM bookings b
JOIN trips t ON b.trip_id = t.id
LEFT JOIN payments p ON b.id = p.booking_id
WHERE b.user_id = 1
ORDER BY b.created_at DESC;
```

**Indexes Used:**
- `idx_booking_user` on bookings(user_id)
- Primary keys for JOINs

---

## Data Integrity Rules

### Foreign Key Cascades

- **Users → Drivers:** ON DELETE CASCADE
- **Drivers → Vehicles:** ON DELETE CASCADE
- **Drivers → Routes:** ON DELETE CASCADE
- **Routes → RoutePoints:** ON DELETE CASCADE
- **Trips → TripSeats:** ON DELETE CASCADE
- **Bookings → Payments:** ON DELETE CASCADE

### Check Constraints

- `users.role` IN ('PASSENGER', 'DRIVER', 'BOTH')
- `vehicles.vehicle_type` IN ('SEDAN', 'SUV', 'HATCHBACK', 'MUV', 'LUXURY')
- `trips.status` IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
- `bookings.status` IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED', 'REFUNDED')
- `payments.status` IN ('INITIATED', 'SUCCESS', 'FAILED', 'REFUNDED', 'PENDING')

---

## Performance Considerations

### Estimated Data Volume (1 year)

- **Users:** 100,000 records
- **Drivers:** 10,000 records
- **Vehicles:** 15,000 records
- **Routes:** 5,000 records
- **RoutePoints:** 15,000 records (3 points per route avg)
- **Trips:** 3,650,000 records (1000 trips/day)
- **Bookings:** 7,300,000 records (2 bookings per trip avg)
- **Payments:** 7,300,000 records

### Storage Estimates

- Total database size: ~2-3 GB (1 year)
- With indexes: ~4-5 GB

### Scaling Strategies

1. **Partitioning:** Partition `trips` and `bookings` by date (monthly)
2. **Archiving:** Move completed trips older than 6 months to archive
3. **Read Replicas:** For search and reporting queries
4. **Caching:** Redis for frequent searches (boarding/drop points, popular routes)

---

## Backup Strategy

- **Full Backup:** Daily at midnight
- **Incremental Backup:** Every 6 hours
- **Point-in-Time Recovery:** Enabled via WAL archiving
- **Retention:** 30 days

---

## Summary

This database design provides:
- ✅ **Normalized structure** (3NF compliance)
- ✅ **Efficient querying** with strategic indexes
- ✅ **Data integrity** with constraints and foreign keys
- ✅ **Scalability** with proper partitioning strategy
- ✅ **Flexibility** for future enhancements









