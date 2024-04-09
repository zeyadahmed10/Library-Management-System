package com.zeyad.maid.lms.dto.request;

import com.zeyad.maid.lms.annotation.FirstOrder;
import com.zeyad.maid.lms.annotation.SecondOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({BookRequestDTO.class, FirstOrder.class, SecondOrder.class})
public class BookRequestDTO {
    @NotBlank(message = "title must not be empty", groups = FirstOrder.class)
    private String title;

    @NotBlank(message = "author must not be empty", groups = FirstOrder.class)
    private String author;

    @NotNull(message = "Publication year cannot be null", groups = FirstOrder.class)
    @PositiveOrZero(message = "Publication year cannot be negative value", groups = SecondOrder.class)
    private Integer publicationYear;

    @NotBlank(message = "isbn must not be empty", groups = FirstOrder.class)
    private String isbn;

    @NotNull(message = "Amount cannot be null", groups = FirstOrder.class)
    @PositiveOrZero(message = "Amount cannot be negative value", groups = SecondOrder.class)
    private Integer amount;
}
