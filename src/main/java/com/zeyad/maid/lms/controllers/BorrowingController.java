package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.annotation.CustomLogger;
import com.zeyad.maid.lms.annotation.LogPerformance;
import com.zeyad.maid.lms.dto.response.BorrowingRecordResponseDTO;
import com.zeyad.maid.lms.services.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Borrowing Controller", description = "APIs for borrowing management")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BorrowingController {

    private final BorrowingService borrowingService;

    @CustomLogger
    @LogPerformance
    @Operation(summary = "Borrow book", description = "Borrow Book from library to specific patron and patron should not exceed 5 books to borrow")
    @ApiResponse(responseCode = "201", description = "Patron borrowed book successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access you need to login")
    @ApiResponse(responseCode = "409", description = "Conflict with existing resource can not borrow book from the library")
    @ApiResponse(responseCode = "404", description = "Not found book or patron")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public BorrowingRecordResponseDTO borrowBook(@PathVariable Long bookId, @PathVariable Long patronId){
        return borrowingService.borrowBook(bookId, patronId);
    }

    @CustomLogger
    @LogPerformance
    @Operation(summary = "Return borrowed book", description = "Return borrowed book to library for specific patron")
    @ApiResponse(responseCode = "200", description = "Book returned successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access you need to login")
    @ApiResponse(responseCode = "404", description = "Not found book or patron, or no active record for that book")
    @PutMapping("return/{bookId}/patron/{patronId}")
    public BorrowingRecordResponseDTO returnBook(@PathVariable Long bookId, @PathVariable Long patronId){
        return borrowingService.returnBook(bookId, patronId);
    }
}
