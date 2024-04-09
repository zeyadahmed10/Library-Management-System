package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.response.BorrowingRecordResponseDTO;

public interface BorrowingService {
    BorrowingRecordResponseDTO borrowBook(Long bookId, Long patronId);

    BorrowingRecordResponseDTO returnBook(Long bookId, Long patronId);
}
