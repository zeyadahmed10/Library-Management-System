package com.zeyad.maid.lms.dto.request;

import com.zeyad.maid.lms.annotation.FirstOrder;
import com.zeyad.maid.lms.annotation.SecondOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@GroupSequence({SignupRequestDTO.class, FirstOrder.class, SecondOrder.class})
public class SignupRequestDTO {

    @NotBlank(message = "Username must not be empty", groups = FirstOrder.class)
    @Size(min = 5, message = "Username must be at least 5 characters long", groups = SecondOrder.class)
    private String username;

    @NotBlank(message = "First Name must not be empty", groups = FirstOrder.class)
    private String firstName;

    @NotBlank(message = "Last Name must not be empty", groups = FirstOrder.class)
    private String lastName;

    @NotBlank(message = "Email must not be empty", groups = FirstOrder.class)
    @Email(message = "Not a valid email address", groups = SecondOrder.class)
    private String email;

    @NotBlank(message = "Password must not be empty", groups = FirstOrder.class)
    @Size(min = 8, message = "Password must be at least 8 characters long", groups = SecondOrder.class)
    private String password;

    @NotBlank(message = "Confirmed Password must not be empty", groups = FirstOrder.class)
    @Size(min = 8, message = "Confirmed Password must be at least 8 characters long", groups = SecondOrder.class)
    private String confirmedPassword;

    @NotBlank(message = "Password must not be empty", groups = FirstOrder.class)
    private String role;
}
