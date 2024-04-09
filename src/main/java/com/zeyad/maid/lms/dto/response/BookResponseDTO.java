package com.zeyad.maid.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
    private Integer amount;
    private Integer rented;
}
