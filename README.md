# Devices Project

This project contains RESTful APIs for managing phones and users. It provides endpoints for retrieving phone information, booking and returning phones, as well as creating users.

## Requirements for the recommendation service
* The mobile software testing team has 10 mobile phones that it needs to share for testing purposes.
* Please create an service that allows a phone to be booked / returned.
* The following information should also be available for each phone:
  - Availability (Yes / No)
  - When it was booked 
  - Who booked the phone
* Please use Fonoapi to expose the following information for each phone and create work-around if it
  isn’t working:
  - Technology
  - 2g bands
  - 3g bands
  - 4g bands

## Phone Controller

The `PhoneController` class provides endpoints for managing phones.

### Endpoints

#### Get Phone Information
- **URL**: `/phones/{model}`
- **Method**: `GET`
- **Description**: Retrieves information about a phone model.
- **Parameters**:
    - `model` (path): The model of the phone to retrieve.
- **Responses**:
    - `200 OK`: Phone info returned successfully.
    - `400 Bad Request`: Invalid request.
    - `404 Not Found`: Phone not supported.
    - `401 Unauthorized`: You are not authorized to access the resource.

#### Book Phone
- **URL**: `/phones/{model}/book`
- **Method**: `POST`
- **Description**: Books a phone for a specific user.
- **Parameters**:
    - `model` (path): The model of the phone to book.
    - `bookedBy` (query): The user who is booking the phone.
- **Responses**:
    - `200 OK`: Phone booked successfully.
    - `400 Bad Request`: Invalid request.
    - `404 Not Found`: Phone not supported.
    - `401 Unauthorized`: You are not authorized to access the resource.

#### Return Phone
- **URL**: `/phones/{model}/return`
- **Method**: `PUT`
- **Description**: Returns a booked phone.
- **Parameters**:
    - `model` (path): The model of the phone to return.
- **Responses**:
    - `200 OK`: Phone returned successfully.
    - `400 Bad Request`: Invalid request.
    - `404 Not Found`: Phone not supported.
    - `401 Unauthorized`: You are not authorized to access the resource.

## User Controller

The `UserController` class provides endpoints for managing users.

### Endpoints

#### Create User
- **URL**: `/user`
- **Method**: `POST`
- **Description**: Creates a new user.
- **Request Body**: User data.
- **Responses**:
    - `200 OK`: User created successfully.
    - `401 Unauthorized`: You are not authorized to access the resource.
    - `500 Internal Server Error`: Application failed to process the request.

## Extra mile for recommendation service (optional):
* Malicious users will always exist, so it will be really beneficial if at least we can rate limit
  them (based on IP)

## Installations needed

* Java 21
* Spring 3.2.5
* Gradle
* Docker or Rancher

## Running

### Build & run

* Clone the repo


* Run
```
$ ./gradlew bootRun
```

* Run with spring profile as "dev" (default)
```
$ ./gradlew bootRun --args='--spring.profiles.active=dev'
```

* Run with spring profile "prod"
```
$ ./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Build & run using Docker

* Build docker image
```
$ docker build -t phone-service .
```

* Run the container to expose app on port 9090
```
$ docker run -it -p 9090:9090 phone-service
```

### Access the app

App will be accessible at:
```
http://localhost:9090
```

* Step 1: Create a user with admin role:
    * curl --location 'http://localhost:9090/user' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "username": "admin@admin.com",
    "password": "1@TestDevices",
    "authority": "ROLE_ADMIN"
    }'
* Step 2: Create a user without admin role:
  * curl --location 'http://localhost:9090/user' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "username": "user@user.com",
    "password": "1@TestDevices",
    "authority": "ROLE_USER"
    }'
  #### Note: Only user with admin role can book phones, rest of the users will get 401 error.
* Step 3: Now you can invoke the endpoint to get phone information, book phone (only with admin role) and return phone.

### Additional funtionality implemented:
  * Circuit breaker using Resilience4j
  * Retry mechanism using Resilience4j
  * Rate limiting using bucket4j

### Documentation

Open API documentation accessible at:
```
http://localhost:9090/api-docs
```

Swagger UI accessible at:
```
http://localhost:9090/swagger-ui/index.html
```

### Potential Improvements

* If you want to avoid maintaining a list of phones in your application.yml file to prevent redeployment every time you add a new phone, you can consider:
  * Store the phone data in a database table
  * Use an external configuration service such as Spring Cloud Config Server
  * Store the phone data in a file or cloud storage
  * Expose a REST API from another service or application that provides the phone data.