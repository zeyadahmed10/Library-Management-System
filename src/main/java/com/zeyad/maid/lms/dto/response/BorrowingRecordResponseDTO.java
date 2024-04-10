package com.zeyad.maid.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordResponseDTO implements Serializable {

    private Long id;
    private Long bookId;
    private Long patronId;
    private Date borrowDate;
    private Date returnDate;
    private Date actualReturnDate;
}
