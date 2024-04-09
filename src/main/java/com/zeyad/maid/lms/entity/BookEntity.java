package com.zeyad.maid.lms.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
}
