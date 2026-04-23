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

## Answers to the given questions

**Q1.1 – JAX-RS Resource Class Lifecycle**

By default, JAX-RS creates a new resource instance per request. In this project, shared data lives in a singleton DataStore using ConcurrentHashMap, which is thread-safe, preventing race conditions across concurrent requests.

**Q1.2 – Why HATEOAS HATEOAS**

embeds navigational links in API responses so clients discover actions at runtime without relying on static documentation. This reduces hardcoded URLs on the client side and makes the API self-descriptive.

**Q2.1 – Full Objects vs IDs**

Returning full objects eliminates extra round trips but increases payload size. Returning only IDs is bandwidth-efficient but forces N additional requests per item (N+1 problem).

**Q2.2 – Is DELETE Idempotent?**

Yes. The first call removes the room and returns 204, subsequent calls return 404, but the server state (room absent) remains identical each time, satisfying idempotency.

**Q3.1 – Non-JSON Content to @Consumes(APPLICATION_JSON) JAX-RS**

It  intercepts the request before the method executes and immediately returns 415 Unsupported Media Type, without ever reaching the resource method.

**Q3.2 – @QueryParam vs Path Parameter**

These Path segments identify a specific resource, while query parameters are designed for optional filtering. @QueryParam supports composable criteria (e.g. ?type=CO2&status=ACTIVE) and degrades gracefully when omitted, returning the full collection.

**Q4.1 – Sub-Resource Locator Pattern**

It delegates nested path handling to dedicated classes, keeping each class small and focused. This improves maintainability, enables independent unit testing, and mirrors logical resource ownership.

**Q5.2 – HTTP 422 vs 404**

404 implies the URL is invalid, but POST /sensors is a valid endpoint. 422 correctly signals that the JSON is syntactically valid but semantically unprocessable due to a non-existent roomId reference.

**Q5.4 – Security Risks of Stack Traces**

Stack traces expose class names, library versions, and file paths, allowing attackers to identify known CVE vulnerabilities and internal architecture. The GlobalExceptionMapper prevents this by returning only a generic 500 response.

**Q5.5 – Filters vs Manual Logging**

Manual logging violates DRY and risks being forgotten in new methods. A single @Provider filter class handles all requests and responses automatically, keeping resource classes focused on business logic.