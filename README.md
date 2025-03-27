 # Aviation Routes

Aim of the project is to implement a system for aviation industry. This system should be able to calculate all the possible routes from point A to point B to provide users better experience while booking their flights. 

## Contents
- [Technologies](#technologies)
- [Installation](#installation)
   - [Back-end (Spring Boot)](#back-end-spring-boot)
   - [Front-end (React)](#front-end-react)
- [API Endpoints](#api-endpoints)
   - [Locations](#locations)
   - [Transportations](#transportations)
   - [Routes](#routes)

## Technologies
- Java 11+ and Spring Boot (REST API)
- Hibernate (ORM)
- H2 database
- React (Front-end), Axios (API requests), CSS
- JUnit 5, Mockito
- Swagger (API documentation)

## Installation
### Back-End (Spring Boot)
1. Clone the project
    ```
    git clone https://github.com/furkaano/aviation-routes.git
    cd aviation-routes
    ```
2. Install maven dependencies
    ```
    mvn clean install
    ```
3. Start the application
    ```
    mvn spring-boot:run
    ```
4. Running check
   - By default, the application runs on http://localhost:8080.
   - For the **Swagger UI**, visit the http://localhost:8080/swagger-ui/index.html
### Front-End (React)
1. Go to the frontend folder
    ```
    cd frontend
    ```
2. Install dependencies
    ```
    npm install
    ```
3. Start the application
    ```
    npm start
    ```
4. Running check
   - By default, the application runs on http://localhost:3000.

## API Endpoints
### Locations
- GET /locations: List all locations.
- POST /locations: Create a new location.
- POST /locations/bulkInsert: Create multiple location at once
- PUT /locations/{id}: Update an existing location.
- DELETE /locations/{id}: Delete a location by ID.
### Transportations
- GET /transportations: List all transportation records.
- POST /transportations: Create a new transportation record.
- POST /transportations/bulkInsert: Create multiple transportations at once
- PUT /transportations/{id}: Update an existing transportation record.
- DELETE /transportations/{id}: Delete a transportation record by ID.
### Routes
- GET /routes?origin={originId}&destination={destinationId}
  - Returns all valid routes from origin to destination
  - Each route can have up to 3 segments and exactly 1 flight
