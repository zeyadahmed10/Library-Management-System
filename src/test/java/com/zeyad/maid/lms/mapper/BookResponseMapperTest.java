package com.zeyad.maid.lms.mapper;

import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookResponseMapperTest {

    @Test
    void testMap_WithValidBookEntity_ReturnsCorrectBookResponseDTO() {
        // Arrange
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setTitle("Sample Title");
        bookEntity.setAuthor("Sample Author");
        bookEntity.setIsbn("1234567890");
        bookEntity.setPublicationYear(2022);
        bookEntity.setRented(0);
        bookEntity.setAmount(10);

        // Act
        var bookResponseDTO = BookResponseMapper.map(bookEntity);

        // Assert
        assertNotNull(bookResponseDTO);
        assertEquals(bookEntity.getId(), bookResponseDTO.getId());
        assertEquals(bookEntity.getTitle(), bookResponseDTO.getTitle());
        assertEquals(bookEntity.getAuthor(), bookResponseDTO.getAuthor());
        assertEquals(bookEntity.getIsbn(), bookResponseDTO.getIsbn());
        assertEquals(bookEntity.getPublicationYear(), bookResponseDTO.getPublicationYear());
        assertEquals(bookEntity.getRented(), bookResponseDTO.getRented());
        assertEquals(bookEntity.getAmount(), bookResponseDTO.getAmount());
    }
    @Test
    void testMap_WhenBookEntityIsNull_ReturnsNull(){
        //Arrange & act
        BookEntity bookEntity = null;
        var bookResponseDTO = BookResponseMapper.map(bookEntity);
        //Assert
        assertEquals(null, bookResponseDTO);
    }

    @Test
    void testMap_whenListOfBookEntity_shouldReturnListOfBookResponseDTO() {
        List<BookEntity> bookEntityList = new ArrayList<>();
        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setId(1L);
        bookEntity1.setTitle("Sample Title 1");
        bookEntity1.setAuthor("Sample Author 1");
        bookEntity1.setIsbn("1234567891");
        bookEntity1.setPublicationYear(2022);
        bookEntity1.setRented(0);
        bookEntity1.setAmount(10);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setId(2L);
        bookEntity2.setTitle("Sample Title 2");
        bookEntity2.setAuthor("Sample Author 2");
        bookEntity2.setIsbn("1234567892");
        bookEntity2.setPublicationYear(2023);
        bookEntity2.setRented(0);
        bookEntity2.setAmount(15);

        bookEntityList.add(bookEntity1);
        bookEntityList.add(bookEntity2);

        // Act
        List<BookResponseDTO> bookResponseDTOList = BookResponseMapper.map(bookEntityList);

        // Assert
        assertNotNull(bookResponseDTOList);
        assertEquals(2, bookResponseDTOList.size());

        // Assert the first book
        BookEntity firstBookEntity = bookEntityList.get(0);
        BookResponseDTO firstBookResponseDTO = bookResponseDTOList.get(0);
        assertEquals(firstBookEntity.getId(), firstBookResponseDTO.getId());
        assertEquals(firstBookEntity.getTitle(), firstBookResponseDTO.getTitle());
        assertEquals(firstBookEntity.getAuthor(), firstBookResponseDTO.getAuthor());
        assertEquals(firstBookEntity.getIsbn(), firstBookResponseDTO.getIsbn());
        assertEquals(firstBookEntity.getPublicationYear(), firstBookResponseDTO.getPublicationYear());
        assertEquals(firstBookEntity.getRented(), firstBookResponseDTO.getRented());
        assertEquals(firstBookEntity.getAmount(), firstBookResponseDTO.getAmount());

        // Assert the second book
        BookEntity secondBookEntity = bookEntityList.get(1);
        BookResponseDTO secondBookResponseDTO = bookResponseDTOList.get(1);
        assertEquals(secondBookEntity.getId(), secondBookResponseDTO.getId());
        assertEquals(secondBookEntity.getTitle(), secondBookResponseDTO.getTitle());
        assertEquals(secondBookEntity.getAuthor(), secondBookResponseDTO.getAuthor());
        assertEquals(secondBookEntity.getIsbn(), secondBookResponseDTO.getIsbn());
        assertEquals(secondBookEntity.getPublicationYear(), secondBookResponseDTO.getPublicationYear());
        assertEquals(secondBookEntity.getRented(), secondBookResponseDTO.getRented());
        assertEquals(secondBookEntity.getAmount(), secondBookResponseDTO.getAmount());
    }
}