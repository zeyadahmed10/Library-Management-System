package com.zeyad.maid.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO implements Serializable {
    private Long id;
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
    private Integer amount;
    private Integer rented;
}
