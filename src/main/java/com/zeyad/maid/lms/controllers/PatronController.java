package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.dto.request.BookRequestDTO;
import com.zeyad.maid.lms.dto.request.PatronRequestDTO;
import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.dto.response.PatronResponseDTO;
import com.zeyad.maid.lms.services.PatronService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@Tag(name = "Patrons Controller", description = "APIs for patrons management")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PatronController {

    private final PatronService patronService;

    @Operation(summary = "Get Patrons", description = "Retrieves a list of patrons, based on name, page, size parameters or using the default values")
    @ApiResponse(responseCode = "200", description = "List of patrons retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login")
    @ApiResponse(responseCode = "404", description = "No patrons found with the specified parameters")
    @GetMapping
    public List<PatronResponseDTO> getPatrons(
            @Parameter(description = "Name of the patron") @RequestParam(name = "name", defaultValue = "") String name,
            @Parameter(description = "Page number") @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Page size") @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return patronService.findAllPatrons(name, page, size);
    }

    @Operation(summary = "Get patron by its id", description = "Retrieves a patron by its id")
    @ApiResponse(responseCode = "200", description = "Patron response")
    @ApiResponse(responseCode = "404", description = "Patron not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login")
    @GetMapping(value = "/{id}")
    public PatronResponseDTO getPatronById(@PathVariable Long id){
        return patronService.findById(id);
    }

    @Operation(summary = "Add Patron", description = "Add new Patron to the library")
    @ApiResponse(responseCode = "201", description = "Patron created and returns book response")
    @ApiResponse(responseCode = "401", description = "Unauthorized access you need to login to add Patron")
    @ApiResponse(responseCode = "400", description = "Bad request can not add patron")
    @ApiResponse(responseCode = "401", description = "Conflict with existing resource can not add patron")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PatronResponseDTO addPatron(@RequestBody @Valid PatronRequestDTO patronRequestDTO){
        return patronService.addPatron(patronRequestDTO);
    }

    @Operation(summary = "Updated patron", description = "patron Book with new details")
    @ApiResponse(responseCode = "200", description = "Patron response with new book updated content")
    @ApiResponse(responseCode = "404", description = "Patron not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login to update patron")
    @ApiResponse(responseCode = "400", description = "Bad request can not update patron details")
    @ApiResponse(responseCode = "401", description = "Conflict with existing resource can not update patron")
    @PutMapping("/{id}")
    public PatronResponseDTO updatePatron(@PathVariable Long id, @RequestBody @Valid PatronRequestDTO patronRequestDTO){
        return patronService.updatePatron(id, patronRequestDTO);
    }

    @Operation(summary = "Delete patron", description = "Delete patron with its id")
    @ApiResponse(responseCode = "200", description = "patron deleted successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access need to login to delete patron")
    @ApiResponse(responseCode = "409", description = "Can't delete the patron, must returns all books first")
    @DeleteMapping("/{id}")
    public void deletePatron(@PathVariable Long id){
        patronService.deletePatron(id);
    }
}
