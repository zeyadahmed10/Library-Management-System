package com.zeyad.maid.lms.controllers;

import com.zeyad.maid.lms.dto.request.SigninRequestDTO;
import com.zeyad.maid.lms.dto.request.SignupRequestDTO;
import com.zeyad.maid.lms.services.KeyCloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "APIs for user authentication and authorization")
@RequiredArgsConstructor
public class AuthController {

    private final KeyCloakService keyCloakService;

    @Operation(
            summary = "Signup new user",
            description = "Create a new user account."
    )
    @PostMapping("/signup")
    @ApiResponse(responseCode = "200", description = "OK")
    public String signupUser(
            @Parameter(description = "User signup information") @Valid @RequestBody SignupRequestDTO signupRequestDTO
    ) throws IllegalAccessException {
        return keyCloakService.createUser(signupRequestDTO);
    }

    @Operation(
            summary = "Sign in user",
            description = "Authenticate and obtain a JWT token for the user."
    )
    @PostMapping("/signin")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
    public ResponseEntity<Object> signinUser(
            @Parameter(description = "User login information") @Valid @RequestBody SigninRequestDTO signinRequestDTO
    ) {
        return keyCloakService.getUserToken(signinRequestDTO);
    }
}