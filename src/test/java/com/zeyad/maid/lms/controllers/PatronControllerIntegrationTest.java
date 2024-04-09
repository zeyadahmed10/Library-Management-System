package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.entity.BorrowingRecordEntity;
import com.zeyad.maid.lms.entity.PatronEntity;
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
class PatronControllerIntegrationTest {
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
        body.put("name","   ");
        body.put("phoneNumber", "   ");
        body.put("address", null);
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/patrons" ).then().statusCode(400)
                .body("name", equalTo("Name must not be empty"))
                .body("address", equalTo("address must not be empty"))
                .body("phoneNumber", equalTo("Phone number must not be empty"));
    }

    void testAddPatron_WhenValidationFailsOnSecondOrder_Returns400(){
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("name","Zeyad Ahmed");
        body.put("phoneNumber", "xyx");
        body.put("address", "Cairo");
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/patrons" ).then().statusCode(400)
                .body("phoneNumber", equalTo("invalid number format"));
    }

}