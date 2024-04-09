package com.zeyad.maid.lms.dto.request;

import com.zeyad.maid.lms.annotation.FirstOrder;
import com.zeyad.maid.lms.annotation.SecondOrder;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@GroupSequence({BookRequestDTO.class, FirstOrder.class, SecondOrder.class})
public class BookRequestDTO {
    private String title;
    private String author;
    private Integer publicationYear;
    private String isbn;
//    @NotNull(message = "Amount cannot be null enter zero or positive value",groups = FirstOrder.class)
    @PositiveOrZero(message = "Amount cannot be negative value", groups = SecondOrder.class)
    private Integer amount;
}
