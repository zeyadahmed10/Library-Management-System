package com.zeyad.maid.lms.dto.request;

import com.zeyad.maid.lms.annotation.FirstOrder;
import com.zeyad.maid.lms.annotation.SecondOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@GroupSequence({SigninRequestDTO.class, FirstOrder.class, SecondOrder.class})
public class SigninRequestDTO {

    @NotBlank(message = "Username must not be empty", groups = FirstOrder.class)
    @Size(min = 5, message = "Username must be at least 5 characters long", groups = SecondOrder.class)
    private String username;

    @NotBlank(message = "password must not be empty", groups = FirstOrder.class)
    @Size(min = 8, message = "password must be at least 8 characters long", groups = SecondOrder.class)
    private String password;
}