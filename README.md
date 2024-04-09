
# Library Management System APIs

The Library Management System built using Spring Boot provides a robust API for librarians to efficiently manage books, patrons, and borrowing records. It offers a comprehensive set of features and endpoints to streamline library operations.


## Appendix

- [Features](#features)
- [Installation](#installation)
- [How to Use and Documentation](#how-to-use-and-documentation)
- [Technologies Used](#technologies-used)
- [Running Tests](#running-tests)



## Features
- **AOP Aspects with Custom Annotations:** Engineered Aspect-Oriented Programming (AOP) aspects with custom annotations for log auditing and peformance metrics

- **Security Measures:** Implemented security measures using Spring Security 6 and KeyCloak, ensuring secure authentication and authorization processes with **JWT and OAuth2**.

- **Unit and Integration Testing:** Conducted thorough unit and integration tests using JUnit 5, Mockito, and Testcontainers for both Postgres and Keycloak, achieving an **88% code coverage** to ensure robustness and reliability.

- **API Documentation:** Documented API endpoints using **Swagger** to provide clear and comprehensive documentation for API usage understanding.

- **Global Exception Handling:** Handling exceptions gracefully.

- **User Input Validation:** Validation over user input and provide user friendly message when validations fails.
- **Transaction Management**
## Installation

**Make sure That you have docker desktop installed first, Refer to [Docker Docs](https://docs.docker.com/desktop/install/windows-install/) for help**

1. Run Docker Desktop
2. Run docker compose to setup KeyCloak and Postgres

```bash
  cd your-path/Library-Management-System
  docker-compose up -d
```

2. Run project after making sure that docker containers working properly

```bash
  mvnw spring-boot:run -DskipTests
```
    
## How to Use and Documentation

- The API documentation is available at http://localhost:8080/swagger-ui.html.
- All endpoints are secured except the Auth endpoints so you will have to login first
- Default username: **client-admin** , password: **password**
- If you are using swagger to interact with the api don't forget to copy the access token from the login endpoint and add it to the Authorize header from ![here](./swagger.png)
- If you are using postman or any different method dont't forget to add authorization haeder with bearer + access_token
- Swagger has details documentation on how to use every endpoint.


## Running Tests

To run tests, run the following command

**Make sure to run docker-compose-test.yaml as it contains configuration for test profile**

1. Run Docker Desktop
2. Run docker compose to setup KeyCloak and Postgres for test enviroment
```bash
  cd your-path/Library-Management-System
  docker-compose -f docker-compose-test.yaml up -d 
```
3. Run tests after making sure that docker containers working properly
```bash
  mvnw test
```

## Technologies Used
- Java 17
- Spring Boot 3
- Spring Security 6
- KeyCloak
- AspectJ
- Postgres
- JUnit 5 & Mockito
- Docker & Docker Compose
- Testcontainers
- Swagger
- Spring Profiles
- RestAssured