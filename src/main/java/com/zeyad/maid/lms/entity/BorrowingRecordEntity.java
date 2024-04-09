package com.zeyad.maid.lms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "borrowing_records", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patron_id")
    private PatronEntity patronEntity;

    @Temporal(TemporalType.DATE)
    private Date borrowDate;

    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @Temporal(TemporalType.DATE)
    private Date actualReturnDate;

    @PrePersist
    public void prePersist() {
        if (borrowDate == null) borrowDate = new Date();
        if (returnDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 10);
            borrowDate = calendar.getTime();
        }
    }
}
