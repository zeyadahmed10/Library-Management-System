package com.zeyad.maid.lms.mapper;

import com.zeyad.maid.lms.dto.response.BorrowingRecordResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.entity.BorrowingRecordEntity;
import com.zeyad.maid.lms.entity.PatronEntity;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingRecordResponseMapperUnitTest {
    @Test
    void map_WithValidBorrowingRecordEntity_ReturnsCorrectBorrowingRecordResponseDTO() {
        // Arrange
        BorrowingRecordEntity borrowingRecordEntity = new BorrowingRecordEntity();
        borrowingRecordEntity.setId(1L);

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(101L);
        PatronEntity patronEntity = new PatronEntity();
        patronEntity.setId(2L);

        borrowingRecordEntity.setBookEntity(bookEntity);

        borrowingRecordEntity.setPatronEntity(patronEntity);

        borrowingRecordEntity.setBorrowDate(new Date());
        borrowingRecordEntity.setReturnDate(new Date());
        borrowingRecordEntity.setActualReturnDate(new Date());

        // Act
        BorrowingRecordResponseDTO borrowingRecordResponseDTO = BorrowingRecordResponseMapper.map(borrowingRecordEntity);

        // Assert
        assertNotNull(borrowingRecordResponseDTO);
        assertEquals(borrowingRecordEntity.getId(), borrowingRecordResponseDTO.getId());
        assertEquals(borrowingRecordEntity.getBookEntity().getId(), borrowingRecordResponseDTO.getBookId());
        assertEquals(borrowingRecordEntity.getPatronEntity().getId(), borrowingRecordResponseDTO.getPatronId());
        assertEquals(borrowingRecordEntity.getBorrowDate(), borrowingRecordResponseDTO.getBorrowDate());
        assertEquals(borrowingRecordEntity.getReturnDate(), borrowingRecordResponseDTO.getReturnDate());
        assertEquals(borrowingRecordEntity.getActualReturnDate(), borrowingRecordResponseDTO.getActualReturnDate());
    }

    @Test
    void testMap_whenNullBorrowingRecordEntity_ReturnsNull(){
        BorrowingRecordEntity borrowingRecordEntity = null;
        BorrowingRecordResponseDTO borrowingRecordResponseDTO = BorrowingRecordResponseMapper.map(borrowingRecordEntity);
        assertEquals(borrowingRecordResponseDTO, null);
    }
}