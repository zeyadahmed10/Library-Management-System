package com.zeyad.maid.lms.dto.request;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninRequestDTO {
    @NotBlank(message = "Username must not be empty")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    private String username;
    @NotBlank(message = "password must not be empty")
    @Size(min = 5, message = "password must be at least 8 characters long")
    private String password;
}