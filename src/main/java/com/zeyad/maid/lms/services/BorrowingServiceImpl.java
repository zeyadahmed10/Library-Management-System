package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.response.BorrowingRecordResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.entity.BorrowingRecordEntity;
import com.zeyad.maid.lms.entity.PatronEntity;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import com.zeyad.maid.lms.mapper.BorrowingRecordResponseMapper;
import com.zeyad.maid.lms.repos.BookRepository;
import com.zeyad.maid.lms.repos.BorrowingRecordRepository;
import com.zeyad.maid.lms.repos.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService{

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final static Integer MAX_BORROW_LIMIT = 5;

    @Transactional
    @Override
    public BorrowingRecordResponseDTO borrowBook(Long bookId, Long patronId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("No Book found for id: " + bookId));
        PatronEntity patronEntity = patronRepository.findById(patronId).orElseThrow(() -> new ResourceNotFoundException("No Patron found for id: " + patronId));
        if(bookEntity.getAmount() - bookEntity.getRented() <=0)
            throw new ResourceExistedException("No books left in inventory for book with id: " + bookId);

        Integer borrowedBooksCnt = borrowingRecordRepository.countByPatronIdAndActualReturnDateIsNull(patronId);

        if(borrowedBooksCnt>= MAX_BORROW_LIMIT)
            throw new ResourceExistedException("Patron with id: " + patronId+" cannot borrow exceeded the limit of borrowing must returns books first");

        Optional<BorrowingRecordEntity> recordEntity = borrowingRecordRepository.findByBookIdAndPatronIdAndActualReturnDateIsNull(bookId, patronId);
        if(recordEntity.isPresent())
            throw new ResourceExistedException("Patron with id: " + patronId+" already borrowing the book and not returned it yet, book id: " + bookId);

        bookEntity.setRented(bookEntity.getRented()+1);
        bookRepository.save(bookEntity);

        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder()
                .bookEntity(bookEntity).patronEntity(patronEntity).actualReturnDate(null).build();

        return BorrowingRecordResponseMapper.map(borrowingRecordRepository.save(borrowingRecordEntity));

    }
    @Transactional
    @Override
    public BorrowingRecordResponseDTO returnBook(Long bookId, Long patronId) {

        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("No Book found for id: " + bookId));
        PatronEntity patronEntity = patronRepository.findById(patronId).orElseThrow(() -> new ResourceNotFoundException("No Patron found for id: " + patronId));

        Optional<BorrowingRecordEntity> borrowingRecordOptional = borrowingRecordRepository.findByBookIdAndPatronIdAndActualReturnDateIsNull(bookId, patronId);
        if(borrowingRecordOptional.isEmpty())
           throw new ResourceNotFoundException("No active borrowing record found for patron id: " +patronId + ", and book id: "+bookId);

        BorrowingRecordEntity borrowingRecordEntity = borrowingRecordOptional.get();
        bookEntity.setRented(bookEntity.getRented()-1);
        bookRepository.save(bookEntity);

        borrowingRecordEntity.setActualReturnDate(new Date());
        return BorrowingRecordResponseMapper.map(borrowingRecordRepository.save(borrowingRecordEntity));
    }
}
