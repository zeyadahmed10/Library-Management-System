package com.zeyad.maid.lms.mapper;

import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.dto.response.BorrowingRecordResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.entity.BorrowingRecordEntity;

public class BorrowingRecordResponseMapper {
    public static BorrowingRecordResponseDTO map(BorrowingRecordEntity borrowingRecordEntity){
        if(borrowingRecordEntity==null)
            return null;
        return BorrowingRecordResponseDTO.builder()
                .id(borrowingRecordEntity.getId())
                .bookId(borrowingRecordEntity.getBookEntity().getId())
                .patronId(borrowingRecordEntity.getPatronEntity().getId())
                .borrowDate(borrowingRecordEntity.getBorrowDate())
                .returnDate(borrowingRecordEntity.getReturnDate())
                .actualReturnDate(borrowingRecordEntity.getActualReturnDate())
                .build();
    }
}
