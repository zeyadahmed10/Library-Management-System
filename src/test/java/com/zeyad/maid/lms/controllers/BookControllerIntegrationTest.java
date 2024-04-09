package com.zeyad.maid.lms.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {
    @LocalServerPort
    private int port;

    private Map<String, Object> requestHeader;
    @BeforeEach
    void setup() throws JSONException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        Map<String, Object> authRequestBody = new HashMap<>();
        authRequestBody.put("username", "client-admin");
        authRequestBody.put("password", "password");
        var authResponse = given()
                .contentType(ContentType.JSON).body(authRequestBody)
                .when()
                .post("/api/auth/signin");
        assertEquals(HttpStatus.OK.value(), authResponse.getStatusCode());
        var access_token = new JSONObject(authResponse.asString()).getString("access_token");
        requestHeader = new HashMap<String,Object>();
        requestHeader.put("Authorization", "Bearer " + access_token);
    }

    @Test
    void testAddPatron_WhenValidationFailsOnFirstOrder_Returns400(){
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("title","   ");
        body.put("author", "  ");
        body.put("publicationYear", null);
        body.put("isbn", "           ");
        body.put("amount", null);
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/books" ).then().statusCode(400)
                .body("amount", equalTo("Amount cannot be null"))
                .body("isbn", equalTo("isbn must not be empty"))
                .body("publicationYear", equalTo("Publication year cannot be null"))
                .body("author", equalTo("author must not be empty"))
                .body("title", equalTo("title must not be empty"));

    }

    @Test
    void testAddPatron_WhenValidationFailsOnSecondOrder_Returns400(){
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("title","title 1");
        body.put("author", "author 1");
        body.put("publicationYear",-1);
        body.put("isbn", "12345678");
        body.put("amount",-1);
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/books" ).then().statusCode(400)
                .body("amount", equalTo("Amount cannot be negative value"))
                .body("publicationYear", equalTo("Publication year cannot be negative value"));

    }
}