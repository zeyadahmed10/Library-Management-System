package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.request.BookRequestDTO;
import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import com.zeyad.maid.lms.mapper.BookResponseMapper;
import com.zeyad.maid.lms.repos.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    @Override
    public List<BookResponseDTO> findAllBooks(String title, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookEntityPage = bookRepository.findByTitleContaining(title, pageable);
        return BookResponseMapper.map(bookEntityPage.getContent());
    }

    @Override
    public BookResponseDTO findById(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Book found for id: " + id));
        return BookResponseMapper.map(bookEntity);
    }

    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        if(bookRepository.existsByTitle(bookRequestDTO.getTitle()))
            throw new ResourceExistedException("Book already exists for title: " + bookRequestDTO.getTitle());

        BookEntity bookEntity = BookEntity.builder().
        title(bookRequestDTO.getTitle()).author(bookRequestDTO.getAuthor())
                .publicationYear(bookRequestDTO.getPublicationYear()).isbn(bookRequestDTO.getIsbn())
                .amount(bookRequestDTO.getAmount()).build();

        return BookResponseMapper.map(bookRepository.save(bookEntity));
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Book found for id: " + id));
        if(bookRepository.existsByTitleAndId(bookRequestDTO.getTitle(), id))
            throw new ResourceExistedException("Book already exists for title: " + bookRequestDTO.getTitle());

        if(bookRequestDTO.getAmount()==null)
            bookRequestDTO.setAmount(bookEntity.getAmount());

        if(bookEntity.getRented()> bookRequestDTO.getAmount())
            throw new IllegalArgumentException("Update amount cannot be less than rented amount, rented amount : "+ bookEntity.getRented());

        bookEntity.setTitle((bookRequestDTO.getTitle()==null)? bookEntity.getTitle() : bookRequestDTO.getTitle());
        bookEntity.setAuthor((bookRequestDTO.getAuthor()==null)? bookEntity.getAuthor() : bookRequestDTO.getAuthor());
        bookEntity.setPublicationYear((bookRequestDTO.getPublicationYear()==null)? bookEntity.getPublicationYear() : bookRequestDTO.getPublicationYear());
        bookEntity.setIsbn((bookRequestDTO.getIsbn()==null)? bookEntity.getIsbn() : bookRequestDTO.getIsbn());
        bookEntity.setAmount((bookRequestDTO.getAmount()==null)? bookEntity.getAmount() : bookRequestDTO.getAmount());

        return BookResponseMapper.map(bookRepository.save(bookEntity));

    }

    @Override
    public void deleteBook(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Book found for id: " + id));
        if(bookEntity.getRented()!=0)
            throw new IllegalArgumentException("All books must return to inventory before deleting the book, rented amount : "+ bookEntity.getRented());
        bookRepository.delete(bookEntity);
    }
}
