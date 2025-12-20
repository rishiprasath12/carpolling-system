# Hierarchical Location System - API Documentation

## 🏙️ Overview

The application now supports **hierarchical locations** where each city can have multiple pickup/drop sub-locations.

### Example Structure:
```
Bangalore
├── Electronic City
├── Silk Board
├── Attibele
└── Chanrapura

Mumbai
├── Central
├── Airport
├── Kurla
└── Dadar

Pune
├── Station
├── Hinjewadi
└── Baner
```

---

## 📊 Database Structure

### RoutePoint Entity (Enhanced)

| Field          | Type    | Description                        | Example                    |
|----------------|---------|------------------------------------|----------------------------|
| id             | Long    | Unique identifier                  | 1                          |
| city           | String  | Main city name                     | "Bangalore"                |
| subLocation    | String  | Specific location within city      | "Electronic City"          |
| pointName      | String  | Combined format                    | "Bangalore - Electronic City"|
| address        | String  | Full address                       | "Electronic City Phase 1..." |
| latitude       | Double  | GPS latitude                       | 12.8456                    |
| longitude      | Double  | GPS longitude                      | 77.6692                    |

---

## 🔌 New API Endpoints

### **1. Get All Cities (with optional search)**

Get list of all available cities.

**Endpoint:** `GET /api/trips/cities?search={optional}`

**Examples:**
```bash
# Get all cities
GET http://localhost:8080/api/trips/cities

# Search cities containing "bang"
GET http://localhost:8080/api/trips/cities?search=bang
```

**Response:**
```json
{
  "success": true,
  "message": "Cities retrieved successfully",
  "data": ["Bangalore", "Mumbai", "Pune"]
}
```

---

### **2. Get Boarding Cities**

Get only cities that have boarding points.

**Endpoint:** `GET /api/trips/cities/boarding`

**Example:**
```bash
GET http://localhost:8080/api/trips/cities/boarding
```

**Response:**
```json
{
  "success": true,
  "message": "Boarding cities retrieved successfully",
  "data": ["Bangalore", "Mumbai"]
}
```

---

### **3. Get Drop Cities**

Get only cities that have drop points.

**Endpoint:** `GET /api/trips/cities/drop`

**Example:**
```bash
GET http://localhost:8080/api/trips/cities/drop
```

**Response:**
```json
{
  "success": true,
  "message": "Drop cities retrieved successfully",
  "data": ["Pune", "Bangalore"]
}
```

---

### **4. Get Boarding Sub-Locations for a City**

Get all boarding sub-locations within a specific city.

**Endpoint:** `GET /api/trips/locations/boarding?city={cityName}`

**Examples:**
```bash
# Get all boarding locations in Bangalore
GET http://localhost:8080/api/trips/locations/boarding?city=Bangalore

# Get all boarding locations in Mumbai
GET http://localhost:8080/api/trips/locations/boarding?city=Mumbai
```

**Response:**
```json
{
  "success": true,
  "message": "Boarding locations retrieved successfully",
  "data": [
    "Electronic City",
    "Silk Board",
    "Attibele",
    "Chanrapura"
  ]
}
```

---

### **5. Get Drop Sub-Locations for a City**

Get all drop sub-locations within a specific city.

**Endpoint:** `GET /api/trips/locations/drop?city={cityName}`

**Examples:**
```bash
# Get all drop locations in Pune
GET http://localhost:8080/api/trips/locations/drop?city=Pune
```

**Response:**
```json
{
  "success": true,
  "message": "Drop locations retrieved successfully",
  "data": [
    "Station",
    "Hinjewadi",
    "Baner"
  ]
}
```

---

### **6. Get All Boarding Points (Complete Details with Search)**

Get complete boarding point details including city, sub-location, address, GPS coordinates.

**Endpoint:** `GET /api/trips/boarding-points?search={optional}`

**Examples:**
```bash
# Get all boarding points
GET http://localhost:8080/api/trips/boarding-points

# Search boarding points by city or sub-location
GET http://localhost:8080/api/trips/boarding-points?search=Electronic

# Search by city
GET http://localhost:8080/api/trips/boarding-points?search=Bangalore
```

**Response:**
```json
{
  "success": true,
  "message": "Boarding points retrieved successfully",
  "data": [
    {
      "id": 1,
      "city": "Bangalore",
      "subLocation": "Electronic City",
      "pointName": "Bangalore - Electronic City",
      "address": "Electronic City Phase 1, Bangalore, Karnataka 560100",
      "latitude": 12.8456,
      "longitude": 77.6692,
      "isBoardingPoint": true,
      "isDropPoint": false
    },
    {
      "id": 2,
      "city": "Bangalore",
      "subLocation": "Silk Board",
      "pointName": "Bangalore - Silk Board",
      "address": "Silk Board Junction, Bangalore, Karnataka",
      "latitude": 12.9176,
      "longitude": 77.6226,
      "isBoardingPoint": true,
      "isDropPoint": true
    }
  ]
}
```

---

### **7. Get All Drop Points (Complete Details with Search)**

Get complete drop point details.

**Endpoint:** `GET /api/trips/drop-points?search={optional}`

**Examples:**
```bash
# Get all drop points
GET http://localhost:8080/api/trips/drop-points

# Search drop points
GET http://localhost:8080/api/trips/drop-points?search=Hinjewadi
```

**Response:**
```json
{
  "success": true,
  "message": "Drop points retrieved successfully",
  "data": [
    {
      "id": 5,
      "city": "Pune",
      "subLocation": "Hinjewadi",
      "pointName": "Pune - Hinjewadi",
      "address": "Hinjewadi Phase 1, Pune, Maharashtra",
      "latitude": 18.5912,
      "longitude": 73.7389,
      "isBoardingPoint": true,
      "isDropPoint": true
    }
  ]
}
```

---

## 📱 Frontend Implementation Examples

### **Two-Step Selection (Recommended)**

#### Step 1: User selects City
#### Step 2: User selects Sub-Location within that city

```javascript
// React Example
const [boardingCity, setBoardingCity] = useState('');
const [boardingSubLocations, setBoardingSubLocations] = useState([]);
const [selectedBoardingLocation, setSelectedBoardingLocation] = useState('');

// Load boarding cities on component mount
useEffect(() => {
  fetch('http://localhost:8080/api/trips/cities/boarding')
    .then(res => res.json())
    .then(data => setCities(data.data));
}, []);

// When user selects a city, load sub-locations
const handleCitySelect = async (city) => {
  setBoardingCity(city);
  const response = await fetch(
    `http://localhost:8080/api/trips/locations/boarding?city=${city}`
  );
  const data = await response.json();
  setBoardingSubLocations(data.data);
};

// Render
<>
  <Select 
    value={boardingCity}
    onChange={(e) => handleCitySelect(e.target.value)}
    label="Select City"
  >
    {cities.map(city => (
      <option key={city} value={city}>{city}</option>
    ))}
  </Select>

  {boardingCity && (
    <Select
      value={selectedBoardingLocation}
      onChange={(e) => setSelectedBoardingLocation(e.target.value)}
      label="Select Location"
    >
      {boardingSubLocations.map(loc => (
        <option key={loc} value={loc}>{loc}</option>
      ))}
    </Select>
  )}
</>
```

---

### **Single-Step Search with Autocomplete**

Allow users to search across all cities and sub-locations at once.

```javascript
// React Example with Autocomplete
const [searchTerm, setSearchTerm] = useState('');
const [suggestions, setSuggestions] = useState([]);

const handleSearch = async (term) => {
  setSearchTerm(term);
  
  if (term.length >= 2) {
    const response = await fetch(
      `http://localhost:8080/api/trips/boarding-points?search=${term}`
    );
    const data = await response.json();
    setSuggestions(data.data);
  }
};

// Render
<Autocomplete
  options={suggestions}
  getOptionLabel={(option) => option.pointName} // "Bangalore - Electronic City"
  onInputChange={(e, value) => handleSearch(value)}
  renderOption={(props, option) => (
    <li {...props}>
      <div>
        <strong>{option.city}</strong> - {option.subLocation}
        <br />
        <small>{option.address}</small>
      </div>
    </li>
  )}
  renderInput={(params) => (
    <TextField {...params} label="Search Boarding Point" />
  )}
/>
```

---

## 🎯 Complete User Flow

### **Scenario: Booking from Bangalore Electronic City to Pune Hinjewadi**

```
STEP 1: User opens booking page

STEP 2: Select Boarding Location
│
├─ Option A: Two-Step Selection
│  ├─ User selects city: "Bangalore"
│  │  → API: GET /api/trips/cities/boarding
│  │  → Shows: ["Bangalore", "Mumbai", ...]
│  │
│  └─ User selects sub-location: "Electronic City"
│     → API: GET /api/trips/locations/boarding?city=Bangalore
│     → Shows: ["Electronic City", "Silk Board", "Attibele", ...]
│
└─ Option B: Single Search
   ├─ User types: "elec"
   │  → API: GET /api/trips/boarding-points?search=elec
   │  → Shows: "Bangalore - Electronic City"
   │
   └─ User selects from dropdown

STEP 3: Select Drop Location (same as Step 2)
   ├─ User selects city: "Pune"
   │  → API: GET /api/trips/cities/drop
   │
   └─ User selects sub-location: "Hinjewadi"
      → API: GET /api/trips/locations/drop?city=Pune

STEP 4: Select Date

STEP 5: Search Trips
   → API: POST /api/trips/search
   Body: {
     "boardingPoint": "Bangalore - Electronic City",
     "dropPoint": "Pune - Hinjewadi",
     "travelDate": "2024-12-25"
   }
```

---

## 🛠️ Creating Routes with Hierarchical Locations

### **Example: Route from Bangalore to Pune**

**POST** `/api/drivers/routes`

```json
{
  "driverId": 1,
  "routeName": "Bangalore to Pune Express",
  "totalDistance": 850000,
  "estimatedDuration": 720,
  "routePoints": [
    {
      "city": "Bangalore",
      "subLocation": "Electronic City",
      "address": "Electronic City Phase 1, Bangalore, Karnataka 560100",
      "latitude": 12.8456,
      "longitude": 77.6692,
      "sequenceOrder": 1,
      "distanceFromStart": 0,
      "timeFromStart": 0,
      "isBoardingPoint": true,
      "isDropPoint": false
    },
    {
      "city": "Bangalore",
      "subLocation": "Silk Board",
      "address": "Silk Board Junction, Bangalore, Karnataka",
      "latitude": 12.9176,
      "longitude": 77.6226,
      "sequenceOrder": 2,
      "distanceFromStart": 8000,
      "timeFromStart": 20,
      "isBoardingPoint": true,
      "isDropPoint": false
    },
    {
      "city": "Bangalore",
      "subLocation": "Attibele",
      "address": "Attibele Town, Bangalore Rural",
      "latitude": 12.7746,
      "longitude": 77.7669,
      "sequenceOrder": 3,
      "distanceFromStart": 35000,
      "timeFromStart": 60,
      "isBoardingPoint": true,
      "isDropPoint": true
    },
    {
      "city": "Pune",
      "subLocation": "Hinjewadi",
      "address": "Hinjewadi Phase 1, Pune, Maharashtra",
      "latitude": 18.5912,
      "longitude": 73.7389,
      "sequenceOrder": 4,
      "distanceFromStart": 820000,
      "timeFromStart": 700,
      "isBoardingPoint": false,
      "isDropPoint": true
    },
    {
      "city": "Pune",
      "subLocation": "Station",
      "address": "Pune Railway Station, Pune",
      "latitude": 18.5204,
      "longitude": 73.8567,
      "sequenceOrder": 5,
      "distanceFromStart": 850000,
      "timeFromStart": 720,
      "isBoardingPoint": false,
      "isDropPoint": true
    }
  ]
}
```

**Note:** The system automatically generates `pointName` as `"City - SubLocation"` format.

---

## 🎨 UI/UX Best Practices

### **1. City + Location Dropdown (Two-Step)**

**Best for:** Desktop/Web Applications

```
┌─────────────────────────────────┐
│ Select Boarding City            │
│ ┌─────────────────────────────┐ │
│ │ Bangalore                  ▼│ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│ Select Location in Bangalore    │
│ ┌─────────────────────────────┐ │
│ │ Electronic City            ▼│ │
│ └─────────────────────────────┘ │
│ • Electronic City                │
│ • Silk Board                     │
│ • Attibele                       │
│ • Chanrapura                     │
└─────────────────────────────────┘
```

### **2. Search with Autocomplete (Single Step)**

**Best for:** Mobile Apps / Quick Search

```
┌─────────────────────────────────┐
│ 🔍 Search Boarding Point        │
│ ┌─────────────────────────────┐ │
│ │ elec_                       │ │
│ └─────────────────────────────┘ │
│                                  │
│ Suggestions:                     │
│ ┌─────────────────────────────┐ │
│ │ Bangalore - Electronic City │ │
│ │ Electronic City Phase 1...  │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
```

### **3. Grouped Dropdown**

**Best for:** All platforms

```
┌─────────────────────────────────┐
│ Select Boarding Point          ▼│
├─────────────────────────────────┤
│ Bangalore                        │
│   ├ Electronic City              │
│   ├ Silk Board                   │
│   ├ Attibele                     │
│   └ Chanrapura                   │
│                                  │
│ Mumbai                           │
│   ├ Central                      │
│   ├ Airport                      │
│   └ Kurla                        │
└─────────────────────────────────┘
```

---

## 📊 API Summary Table

| Endpoint | Method | Parameter | Purpose |
|----------|--------|-----------|---------|
| `/api/trips/cities` | GET | `search` (optional) | Get/search all cities |
| `/api/trips/cities/boarding` | GET | - | Get boarding cities only |
| `/api/trips/cities/drop` | GET | - | Get drop cities only |
| `/api/trips/locations/boarding` | GET | `city` (required) | Get boarding sub-locations for a city |
| `/api/trips/locations/drop` | GET | `city` (required) | Get drop sub-locations for a city |
| `/api/trips/boarding-points` | GET | `search` (optional) | Get all boarding points with details |
| `/api/trips/drop-points` | GET | `search` (optional) | Get all drop points with details |

---

## 🚀 Benefits of Hierarchical System

1. **Better UX** - Users can easily find exact pickup location
2. **Scalability** - Add multiple locations per city without cluttering
3. **Flexibility** - Search by city OR sub-location
4. **Clarity** - Clear hierarchy: "Bangalore - Electronic City"
5. **Performance** - Efficient queries with indexed searches
6. **Real-World** - Matches how users think about locations

---

## 🧪 Testing Examples

### Test 1: Get cities and sub-locations
```bash
# Get all boarding cities
curl http://localhost:8080/api/trips/cities/boarding

# Get sub-locations in Bangalore
curl "http://localhost:8080/api/trips/locations/boarding?city=Bangalore"
```

### Test 2: Search across everything
```bash
# Search for "electronic" in all boarding points
curl "http://localhost:8080/api/trips/boarding-points?search=electronic"
```

### Test 3: Create route with multiple sub-locations in same city
```bash
curl -X POST http://localhost:8080/api/drivers/routes \
  -H "Content-Type: application/json" \
  -d '{
    "driverId": 1,
    "routeName": "Bangalore Intra-City",
    "totalDistance": 25000,
    "estimatedDuration": 60,
    "routePoints": [
      {
        "city": "Bangalore",
        "subLocation": "Electronic City",
        "address": "...",
        "latitude": 12.8456,
        "longitude": 77.6692,
        "sequenceOrder": 1,
        "distanceFromStart": 0,
        "timeFromStart": 0,
        "isBoardingPoint": true,
        "isDropPoint": false
      },
      {
        "city": "Bangalore",
        "subLocation": "Silk Board",
        "address": "...",
        "latitude": 12.9176,
        "longitude": 77.6226,
        "sequenceOrder": 2,
        "distanceFromStart": 8000,
        "timeFromStart": 20,
        "isBoardingPoint": true,
        "isDropPoint": true
      },
      {
        "city": "Bangalore",
        "subLocation": "Koramangala",
        "address": "...",
        "latitude": 12.9352,
        "longitude": 77.6245,
        "sequenceOrder": 3,
        "distanceFromStart": 15000,
        "timeFromStart": 40,
        "isBoardingPoint": false,
        "isDropPoint": true
      }
    ]
  }'
```

---

This hierarchical system provides a much better user experience and matches real-world scenarios where cities have multiple pickup/drop points! 🎉









