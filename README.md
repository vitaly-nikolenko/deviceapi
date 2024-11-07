# Device Management API

This is a Spring Boot application that provides a REST API for managing device hierarchy. The application supports the following operations:

- List all devices, sorted by device type (Gateway > Switch > Access Point).
- Retrieving all registered network device topology.
- Retrieve a device by its MAC address.
- Retrieving network device topology starting from a specific device.
- Add a new device.
- Update an existing device by MAC address.
- Delete a device by MAC address.
  
### Technologies Used

- Spring Boot
- Spring Web
- Java
- JUnit 5 (for unit tests)
- Lombok
- Gradle

### Prerequisites

Before you begin, ensure that you have the following installed:

- JDK 21 or later
- Gradle (optional, if not using the wrapper)
  
### Setup and Installation

1. **Build the project**:

   If you have Gradle installed, you can build the project using the following command:

   ```bash
   gradle build
   ```

   Alternatively, you can use the Gradle wrapper:

   ```bash
   ./gradlew build
   ```

2. **Run the application**:

   After building the project, you can run the Spring Boot application using the following command:

   ```bash
   gradle bootRun
   ```

   Or with the Gradle wrapper:

   ```bash
   ./gradlew bootRun
   ```

   The application will start on port `8080` by default.

### API Endpoints

The following endpoints are available in the API:

#### 1. **List all devices sorted by device type (Gateway > Switch > Access Point)**

- **URL**: `/api/devices`
- **Method**: `GET`
- **Response**:

  A list of devices sorted by type.

#### 2. **Add a new device**

- **URL**: `/api/devices`
- **Method**: `POST`
- **Request Body**:

  ```json
  {
    "deviceType": "GATEWAY",
    "macAddress": "00:1A:2B:3C:4D:5E",
    "uplinkMacAddress": null
  }
  ```

- **Response**:

  The added device.

#### 3. **Update an existing device**

- **URL**: `/api/devices/{macAddress}`
- **Method**: `PUT`
- **Request Body**:

  ```json
  {
    "deviceType": "SWITCH",
    "macAddress": "00:1A:2B:3C:4D:5E",
    "uplinkMacAddress": "00:1A:2B:3C:4D:6F"
  }
  ```

- **Response**:

  The updated device.

#### 4. **Delete a device**

- **URL**: `/api/devices/{macAddress}`
- **Method**: `DELETE`
- **Response**:

  `204 No Content` if the device is deleted successfully.

#### 5. **Retrieving network device topology starting from a specific device**

- **URL**: `/api/devices/topology/{macAddress}`
- **Method**: `GET`
- **Response**:

  A tree structure of devices starting from the given device.

#### 6. **Retrieving all registered network device topology**

- **URL**: `/api/devices/topology`
- **Method**: `GET`
- **Response**:

  A list of device trees for all root devices.

#### 7. **Get a device by MAC address**

- **URL**: `/api/devices/{macAddress}`
- **Method**: `GET`
- **Response**:

  The device details.

### Unit Tests

The project includes unit tests for the `DeviceService`. The tests use JUnit 5 and Mockito for mocking dependencies.

To run the tests, use the following command:

```bash
gradle test
```

Or with the Gradle wrapper:

```bash
./gradlew test
```
