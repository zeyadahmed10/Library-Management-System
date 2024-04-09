package com.zeyad.maid.lms.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String title;
    private String author;
    private Integer publicationYear;
    private String isbn;
    private Integer amount;
    private Integer rented;

    @PrePersist
    public void prePersist() {
        if (amount == null) amount = 0;
        if (rented == null) rented = 0;
    }
}
