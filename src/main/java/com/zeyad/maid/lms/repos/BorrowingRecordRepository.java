package com.zeyad.maid.lms.repos;

import com.zeyad.maid.lms.entity.BorrowingRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecordEntity, Long> {
    @Query("SELECT COUNT(br) FROM BorrowingRecordEntity br WHERE br.bookEntity.id = :bookId")
    long countByBookId(@Param("bookId") Long bookId);

    @Query("SELECT CASE WHEN COUNT(br) > 0 THEN true ELSE false END FROM BorrowingRecordEntity br WHERE br.patronEntity.id = :patronId AND br.actualReturnDate IS NULL")
    boolean existsByPatronIdAndActualReturnDateIsNull(@Param("patronId") Long patronId);
}