package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.request.BookRequestDTO;
import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
public interface BookService {
    public List<BookResponseDTO> findAllBooks(String name, Integer page, Integer size);

    public BookResponseDTO findById(Long id);

    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO);

    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO);

    public void deleteBook(Long id);
}
