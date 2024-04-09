package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.response.BorrowingRecordResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.entity.BorrowingRecordEntity;
import com.zeyad.maid.lms.entity.PatronEntity;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import com.zeyad.maid.lms.repos.BookRepository;
import com.zeyad.maid.lms.repos.BorrowingRecordRepository;
import com.zeyad.maid.lms.repos.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

class BorrowingServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook_WhenBookExistsAndPatronExistsAndBookIsAvailableAndPatronHasNotExceededLimit_ShouldReturnBorrowingRecordDTO() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 2L;
        BookEntity bookEntity = BookEntity.builder()
                .id(bookId)
                .amount(1)
                .rented(0)
                .build();
        PatronEntity patronEntity = PatronEntity.builder().id(patronId).build();
        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder().build();
        borrowingRecordEntity.setBookEntity(bookEntity);
        borrowingRecordEntity.setPatronEntity(patronEntity);
        borrowingRecordEntity.setId(3L);
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());
        doReturn(0).when(borrowingRecordRepository).countByPatronIdAndActualReturnDateIsNull(anyLong());
        doReturn(Optional.empty()).when(borrowingRecordRepository).findByBookIdAndPatronIdAndActualReturnDateIsNull(anyLong(), anyLong());
        doReturn(bookEntity).when(bookRepository).save(any(BookEntity.class));
        doReturn(borrowingRecordEntity).when(borrowingRecordRepository).save(any(BorrowingRecordEntity.class));

        // Act
        BorrowingRecordResponseDTO result = borrowingService.borrowBook(bookId, patronId);

        // Assert
        assertNotNull(result);
        assertEquals(bookEntity.getId(), result.getBookId());
        assertEquals(patronEntity.getId(), result.getPatronId());
        assertEquals(borrowingRecordEntity.getId(), result.getId());
        assertEquals(null, result.getActualReturnDate());
    }

    @Test
    void testBorrowBook_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        doReturn(Optional.empty()).when(bookRepository).findById(anyLong());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(bookId, patronId));
    }

    @Test
    void testBorrowBook_WhenPatronDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        BookEntity bookEntity = BookEntity.builder().build();
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        doReturn(Optional.empty()).when(patronRepository).findById(anyLong());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(bookId, patronId));
    }

    @Test
    void testBorrowBook_WhenBookIsNotAvailable_ShouldThrowResourceExistedException() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        BookEntity bookEntity = BookEntity.builder()
                .id(bookId)
                .amount(1)
                .rented(1)
                .build();
        PatronEntity patronEntity = PatronEntity.builder().id(patronId).build();
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());

        // Act & Assert
        assertThrows(ResourceExistedException.class, () -> borrowingService.borrowBook(bookId, patronId));
    }

    @Test
    void testBorrowBook_WhenPatronHasExceededLimit_ShouldThrowResourceExistedException() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        BookEntity bookEntity = BookEntity.builder().id(bookId).amount(1)
                .rented(0).build();
        PatronEntity patronEntity = PatronEntity.builder().id(patronId).build();
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());
        doReturn(5).when(borrowingRecordRepository).countByPatronIdAndActualReturnDateIsNull(anyLong());

        // Act & Assert
        assertThrows(ResourceExistedException.class, () -> borrowingService.borrowBook(bookId, patronId));
    }

    @Test
    void testBorrowBook_WhenPatronAlreadyBorrowingTheBook_ShouldThrowResourceExistedException() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        BookEntity bookEntity = BookEntity.builder().id(bookId).amount(1)
                .rented(0).build();
        PatronEntity patronEntity = PatronEntity.builder().id(patronId).build();
        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder().build();
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());
        doReturn(Optional.of(borrowingRecordEntity)).when(borrowingRecordRepository).findByBookIdAndPatronIdAndActualReturnDateIsNull(anyLong(), anyLong());

        // Act & Assert
        assertThrows(ResourceExistedException.class, () -> borrowingService.borrowBook(bookId, patronId));
    }
    @Test
    void testReturnBook_WhenBorrowingRecordExists_ShouldReturnBorrowingRecordDTO() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        BookEntity bookEntity = BookEntity.builder().id(bookId).rented(1).build();
        PatronEntity patronEntity = PatronEntity.builder().id(patronId).build();
        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder().actualReturnDate(new Date())
                .patronEntity(patronEntity).bookEntity(bookEntity).build();
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());
        doReturn(Optional.of(borrowingRecordEntity)).when(borrowingRecordRepository).findByBookIdAndPatronIdAndActualReturnDateIsNull(anyLong(), anyLong());
        doReturn(borrowingRecordEntity).when(borrowingRecordRepository).save(any(BorrowingRecordEntity.class));

        // Act
        BorrowingRecordResponseDTO result = borrowingService.returnBook(bookId, patronId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testReturnBook_WhenBorrowingRecordDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;
        BookEntity bookEntity = BookEntity.builder().build();
        PatronEntity patronEntity = PatronEntity.builder().build();
        doReturn(Optional.empty()).when(bookRepository).findById(anyLong());
        doReturn(Optional.empty()).when(patronRepository).findById(anyLong());
        doReturn(Optional.empty()).when(borrowingRecordRepository).findByBookIdAndPatronIdAndActualReturnDateIsNull(anyLong(), anyLong());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(bookId, patronId));
    }

}
