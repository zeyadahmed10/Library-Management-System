package com.zeyad.maid.lms.dto.request;

import com.zeyad.maid.lms.annotation.FirstOrder;
import com.zeyad.maid.lms.annotation.SecondOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({PatronRequestDTO.class, FirstOrder.class, SecondOrder.class})
public class PatronRequestDTO {

    @NotBlank(message = "Name must not be empty", groups = FirstOrder.class)
    private String name;

    @NotBlank(message = "address must not be empty", groups = FirstOrder.class)
    private String address;

    @NotBlank(message = "Phone number must not be empty", groups = FirstOrder.class)
    @Pattern(regexp = "^\\+?\\d+$", message = "invalid number format", groups = SecondOrder.class)
    private String phoneNumber;
}
