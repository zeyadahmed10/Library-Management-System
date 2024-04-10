package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.annotation.CustomLogger;
import com.zeyad.maid.lms.annotation.LogPerformance;
import com.zeyad.maid.lms.dto.request.BookRequestDTO;
import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books Controller", description = "APIs for books management")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    @CustomLogger
    @Operation(summary = "Get Books", description = "Retrieves a list of books, based on title, page, size parameters or using the default values")
    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login")
    @ApiResponse(responseCode = "404", description = "No books found with the specified parameters")
    @GetMapping
    public List<BookResponseDTO> getBooks(
            @Parameter(description = "Title of the book") @RequestParam(name = "title", defaultValue = "") String title,
            @Parameter(description = "Page number") @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Page size") @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return bookService.findAllBooks(title, page, size);
    }

    @CustomLogger
    @Operation(summary = "Get book by its id", description = "Retrieves a patron by its id")
    @ApiResponse(responseCode = "200", description = "Book response")
    @ApiResponse(responseCode = "404", description = "Patron not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login")
    @GetMapping(value = "/{id}")
    @Cacheable(value = "books", key = "#id")
    public BookResponseDTO getBookById(@PathVariable Long id){
        return bookService.findById(id);
    }

    @CustomLogger
    @LogPerformance
    @Operation(summary = "Add book", description = "Add new book to the library")
    @ApiResponse(responseCode = "201", description = "Book created and returns book response")
    @ApiResponse(responseCode = "401", description = "Unauthorized access you need to login to add book")
    @ApiResponse(responseCode = "400", description = "Bad request can not add book to the library")
    @ApiResponse(responseCode = "409", description = "Conflict with existing resource can not add book to the library")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookResponseDTO addBook(@RequestBody @Valid BookRequestDTO bookRequestDTO){
        return bookService.addBook(bookRequestDTO);
    }

    @CustomLogger
    @LogPerformance
    @Operation(summary = "Updated book", description = "Update Book with new details")
    @ApiResponse(responseCode = "200", description = "Book response with new book updated content")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login to update book")
    @ApiResponse(responseCode = "400", description = "Bad request can not update book details")
    @ApiResponse(responseCode = "409", description = "Conflict with existing resource can not update book details")
    @PutMapping("/{id}")
    @CachePut(value ="books", key="#id")
    public BookResponseDTO updateBook(@PathVariable Long id, @RequestBody @Valid BookRequestDTO bookRequestDTO){
        return bookService.updateBook(id, bookRequestDTO);
    }

    @CustomLogger
    @LogPerformance
    @Operation(summary = "Delete book", description = "Delete book with its id")
    @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login to delete book")
    @DeleteMapping("/{id}")
    @CacheEvict(cacheNames = "books", key = "#id", beforeInvocation = false)
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

}
