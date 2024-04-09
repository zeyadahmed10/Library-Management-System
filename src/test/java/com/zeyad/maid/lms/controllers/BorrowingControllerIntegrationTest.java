package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.entity.BorrowingRecordEntity;
import com.zeyad.maid.lms.entity.PatronEntity;
import com.zeyad.maid.lms.repos.BookRepository;
import com.zeyad.maid.lms.repos.BorrowingRecordRepository;
import com.zeyad.maid.lms.repos.PatronRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BorrowingControllerIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PatronRepository patronRepository;
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private BookEntity bookEntity;
    private PatronEntity patronEntity;
    private BorrowingRecordEntity borrowingRecordEntity;
    private Map<String, Object> requestHeader;
    @BeforeEach
    void setup() throws JSONException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        borrowingRecordRepository.deleteAll();
        bookRepository.deleteAll();
        patronRepository.deleteAll();

        bookEntity =  BookEntity.builder()
                .author("sample author 1").title("sample title1")
                .publicationYear(2023).amount(10).rented(0)
                .isbn("123456789")
                .build();

        bookRepository.save(bookEntity);

        patronEntity = PatronEntity.builder().name("Zeyad Ahmed").phoneNumber("123456789")
                .address("cairo").build();
        patronRepository.save(patronEntity);

        borrowingRecordEntity = BorrowingRecordEntity.builder().bookEntity(bookEntity)
                .patronEntity(patronEntity).build();

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
    void testSuccessfulBorrowing_Returns201() {
        Response response = given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", bookEntity.getId(), patronEntity.getId());

        response.then()
                .statusCode(201);
    }

    @Test
    void testUnauthorizedAccess_Returns401() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", 1, 1)
                .then()
                .statusCode(401);
    }

    @Test
    void testConflictWithExistingResource_whenPatronAlreadyBookTheSpecifiedBook_Returns409() {
        borrowingRecordRepository.save(borrowingRecordEntity);
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", bookEntity.getId(), patronEntity.getId())
                .then()
                .statusCode(409);
    }
    @Test
    void testNotFoundCases_WhenPatronOrBookNotFound_Returns404() {
        // Test book not found
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", -1, patronEntity.getId())
                .then()
                .statusCode(404);

        // Test patron not found
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/borrow/{bookId}/patron/{patronId}", bookEntity.getId(), -1)
                .then()
                .statusCode(404);
    }

    @Test
    void testReturnBook_Success_Returns200() {
        borrowingRecordRepository.save(borrowingRecordEntity);

        given().headers(requestHeader)
                .when()
                .put("/api/return/{bookId}/patron/{patronId}", bookEntity.getId(), patronEntity.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void testReturnBook_UnAuthorized_Returns401() {
        given()
                .when()
                .put("/api/return/{bookId}/patron/{patronId}", bookEntity.getId(), patronEntity.getId())
                .then()
                .statusCode(401);
    }

    @Test
    void testReturnBook_WhenThereIsNoActiveRecord_Returns404(){
        borrowingRecordEntity.setActualReturnDate(new Date());
        borrowingRecordRepository.save(borrowingRecordEntity);
        given().headers(requestHeader)
                .when()
                .put("/api/return/{bookId}/patron/{patronId}", bookEntity.getId(), patronEntity.getId())
                .then()
                .statusCode(404);
    }
    @Test
    void testReturnBook_NotFound_Returns404(){
        // Test book not found
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/return/{bookId}/patron/{patronId}", -1, patronEntity.getId())
                .then()
                .statusCode(404);

        // Test patron not found
        given().headers(requestHeader)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/return/{bookId}/patron/{patronId}", bookEntity.getId(), -1)
                .then()
                .statusCode(404);
    }

}