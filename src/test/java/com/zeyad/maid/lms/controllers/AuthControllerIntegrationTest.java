package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.dto.request.SigninRequestDTO;
import com.zeyad.maid.lms.dto.request.SignupRequestDTO;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
class AuthControllerIntegrationTest {
    static Network network = Network.newNetwork();
    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer =  new PostgreSQLContainer<>("postgres:alpine3.18")
            .withDatabaseName("postgres")
            .withPassword("password")
            .withUsername("zeyad")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withCopyFileToContainer(MountableFile.forClasspathResource("dump.sql"), "/docker-entrypoint-initdb.d/init.sql")
            .withExposedPorts(5432);
    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:23.0")
            .withRealmImportFile("realm-export.json")
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases("x")
            .dependsOn(postgreSQLContainer)
            .withEnv("KC_DB", "postgres")
            .withEnv("KC_DB_PASSWORD","password")
            .withEnv("KC_DB_SCHEMA", "bitnami")
            .withEnv("KC_DB_URL","jdbc:postgresql://postgres:5432/postgres")
            .withEnv("KC_DB_USERNAME","zeyad")
            .withEnv("KC_DB_URL_PORT","5432")
            .withEnv("KC_DB_URL_HOST", "postgres");

    @BeforeAll
    static void init() throws Exception{
        try{
            postgreSQLContainer.start();
            keycloakContainer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
        assertTrue(keycloakContainer.isCreated());
        assertTrue(keycloakContainer.isRunning());
    }
    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        int keyCloakPort = keycloakContainer.getFirstMappedPort();
        String authUrl = "http://localhost:"+String.valueOf(keyCloakPort);
        System.out.println(authUrl);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> authUrl + "/realms/Secure-File-System");
        registry.add("keycloak.serverUrl" ,()-> authUrl);
        registry.add("keycloak.realm", ()-> "Secure-File-System");
        registry.add("keycloak.clientId",()-> "secure-file-system-CLI");
        registry.add("keycloak.username",()-> "client-admin");
        registry.add("keycloak.password", ()-> "password");
        registry.add("keycloak.clientSecret",()-> "NH9NwlM7pahNEFIDOILWAjC0hYRixwNp");
        registry.add("keycloak.loginUrl",()-> authUrl + "/realms/Secure-File-System/protocol/openid-connect/token");
    }
    @LocalServerPort
    private int port;


    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
    @Test
    void testSignupUser_whenUserRegistered_shouldReturn409Conflict() {
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO(
                "client-admin", "firstName", "lastName","email@gmail.com",
                "password", "password", "user"
        );
        var response = given().contentType(ContentType.JSON).body(signupRequestDTO)
                .when().post("/api/auth/signup");
        assertEquals(409, response.getStatusCode() );
    }
    @Test
    void testSignupUser_whenNewUserRegistered_shouldReturn200ok() {
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO(
                "new user", "firstName", "lastName","email@gmail.com",
                "password", "password", "user"
        );
        var response = given().contentType(ContentType.JSON).body(signupRequestDTO)
                .when().post("/api/auth/signup");
        assertEquals(200, response.getStatusCode() );
    }

    @Test
    public void testSigninUser_whenValidCredentials_shouldReturn200Ok() {
        SigninRequestDTO signinRequestDTO = new SigninRequestDTO("client-admin", "password");
        var response =RestAssured.given()
                .contentType(ContentType.JSON).body(signinRequestDTO)
                .when()
                .post("/api/auth/signin");
        assertEquals(200, response.getStatusCode());
    }
    @Test
    public void testSigninUser_whenInvalidCredentials_shouldReturn401unauthorized() {
        SigninRequestDTO signinRequestDTO = new SigninRequestDTO("invalid-admin", "password");
        var response =RestAssured.given()
                .contentType(ContentType.JSON).body(signinRequestDTO)
                .when()
                .post("/api/auth/signin");
        assertEquals(401, response.getStatusCode());
    }
}