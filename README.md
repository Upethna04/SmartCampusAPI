# Smart Campus API
A RESTful API for managing campus Rooms and Sensors, built using JAX-RS (Jersey) deployed on Apache Tomcat.

## API Overview

The Smart Campus API provides a comprehensive interface for campus facilities managers to manage rooms and sensors across the university. The API follows RESTful principles with a logical resource hierarchy:

- **Rooms** - Manage physical rooms on campus
- **Sensors** - Manage sensors deployed in rooms (Temperature, CO2, Occupancy etc.)
- **Sensor Readings** - Historical readings log for each sensor

### Base URL

http://localhost:8080/SmartCampusAPI/api/v1

### Resource Hierarchy

/api/v1
├── /rooms
│   ├── GET    - List all rooms
│   ├── POST   - Create a room
│   └── /{roomId}
│       ├── GET    - Get a specific room
│       └── DELETE - Delete a room (blocked if sensors exist)
├── /sensors
│   ├── GET    - List all sensors (supports ?type= filter)
│   ├── POST   - Register a new sensor
│   └── /{sensorId}
│       ├── GET - Get a specific sensor
│       └── /readings
│           ├── GET  - Get all readings for sensor
│           └── POST - Add a new reading
└── /          - Discovery endpoint

## Technology Stack

- **Java** - Core language
- **JAX-RS (Jersey)** - RESTful API framework
- **Apache Tomcat** - Servlet container
- **Maven** - Build tool

### Step 1 - Clone the Repository

git clone https://github.com/Upethna04/-SmartCampusAPI.git

### Step 2 - Open in NetBeans

### Step 3 - Build the Project

### Step 4 - Deploy to Tomcat

### Step 5 - Access the API
http://localhost:8080/SmartCampusAPI/api/v1/

## Sample curl Commands

### 1. Discovery Endpoint - Get API info and resource links
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/

### 2. Get All Rooms
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms

### 3. Create a New Room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"CS-101","name":"Computer Science Lab","capacity":40}'

### 4. Get All Sensors Filtered by Type
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"

### 5. Register a New Sensor
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-002","type":"Temperature","status":"ACTIVE","currentValue":21.0,"roomId":"LIB-301"}'

### 6. Post a Sensor Reading
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 23.7}'

### 7. Get Sensor Reading History
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings

### 8. Delete a Room
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/CS-101

## Error Handling
409 Conflict -> Deleting a room that still has sensors
422 Unprocessable Entity -> Creating a sensor with a non-existent roomId
403 Forbidden -> Posting a reading to a sensor in MAINTENANCE status
404 Not Found -> Requesting a room or sensor that doesn't exist 
500 Internal Server Error -> Any unexpected server error

## Pre-loaded Test Data

The API comes with sample data for testing:

**Rooms:**
- `LIB-301` - Library Quiet Study (capacity: 50)
- `LAB-101` - Computer Lab (capacity: 30)

**Sensors:**
- `TEMP-001` - Temperature sensor (ACTIVE) in LIB-301
- `CO2-001` - CO2 sensor (ACTIVE) in LAB-101