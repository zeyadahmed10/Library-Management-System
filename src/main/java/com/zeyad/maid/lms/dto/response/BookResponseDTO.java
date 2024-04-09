package com.zeyad.maid.lms.dto.response;

import lombok.Builder;

@Builder
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
}
