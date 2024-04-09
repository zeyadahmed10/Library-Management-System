package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.request.BookRequestDTO;
import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import com.zeyad.maid.lms.repos.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

class BookServiceImplUnitTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllBooks_whenRetrievingEntityList_shouldResponseDtoDetailsMatchEntity() {
        // Arrange
        String title = "Title";
        int page = 0;
        int size = 10;
        var bookEntity =  BookEntity.builder().id(1L)
                .author("sample author 1").title("sample title1")
                .publicationYear(2023).amount(10).rented(0)
                .isbn("123456789")
                .build();
        List<BookEntity> bookEntityList = Collections.singletonList(bookEntity);
        Page<BookEntity> bookEntityPage = new PageImpl<>(bookEntityList);
        doReturn(bookEntityPage).when(bookRepository).findByTitleContaining(anyString(),any(Pageable.class));
        // Act
        List<BookResponseDTO> result = bookService.findAllBooks(title, page, size);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(bookEntityList.size(), result.size());
        assertEquals(result.get(0).getTitle(), bookEntityList.get(0).getTitle());
        assertEquals(result.get(0).getAuthor(), bookEntityList.get(0).getAuthor());
        assertEquals(result.get(0).getId(), bookEntityList.get(0).getId());
        assertEquals(result.get(0).getRented(), bookEntityList.get(0).getRented());
        assertEquals(result.get(0).getAmount(), bookEntityList.get(0).getAmount());
        assertEquals(result.get(0).getPublicationYear(), bookEntityList.get(0).getPublicationYear());
        assertEquals(result.get(0).getIsbn(), bookEntityList.get(0).getIsbn());
    }
    @Test
    void testFindById_WhenExistingIdProvided_ReturnsCorrectBookResponseDTO() {
        // Arrange
        Long id = 1L;
        var bookEntity =  BookEntity.builder().id(1L)
                .author("sample author 1").title("sample title1")
                .publicationYear(2023).amount(10).rented(0)
                .isbn("123456789")
                .build();
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(any(Long.class));

        // Act
        BookResponseDTO result = bookService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(result.getTitle(), bookEntity.getTitle());
        assertEquals(result.getAuthor(), bookEntity.getAuthor());
        assertEquals(result.getId(), bookEntity.getId());
        assertEquals(result.getRented(), bookEntity.getRented());
        assertEquals(result.getAmount(), bookEntity.getAmount());
        assertEquals(result.getPublicationYear(), bookEntity.getPublicationYear());
        assertEquals(result.getIsbn(), bookEntity.getIsbn());
    }
    @Test
    void testFindById_WhenNotExistingIdProvided_ThrowsResourceNotFoundException(){
        //Arrange
        Long id = 2L;
        doReturn(Optional.empty()).when(bookRepository).findById(any(Long.class));

        //act & assert
        assertThrows(RuntimeException.class, ()-> bookService.findById(id) );


    }
    @Test
    void testAddBook_WhenValidBookRequestDTOProvided_ReturnsBookResponseDTO(){
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 0;
        String isbn = "123456789";

        var bookEntity =  BookEntity.builder().id(id)
                .author(author).title(sampleTitle1)
                .publicationYear(publicationYear).amount(amount).rented(rented)
                .isbn(isbn)
                .build();

        var bookRequestDto = new BookRequestDTO(sampleTitle1, author, publicationYear, isbn, amount);
        doReturn(false).when(bookRepository).existsByTitle(anyString());
        doReturn(bookEntity).when(bookRepository).save(any(BookEntity.class));
        //act
        BookResponseDTO result = bookService.addBook(bookRequestDto);

        //assert
        assertNotNull(result);
        assertEquals(result.getTitle(), bookEntity.getTitle());
        assertEquals(result.getAuthor(), bookEntity.getAuthor());
        assertEquals(result.getId(), bookEntity.getId());
        assertEquals(result.getRented(), bookEntity.getRented());
        assertEquals(result.getAmount(), bookEntity.getAmount());
        assertEquals(result.getPublicationYear(), bookEntity.getPublicationYear());
        assertEquals(result.getIsbn(), bookEntity.getIsbn());

    }
    @Test
    void testAddBook_WhenBookWithExistedTitleProvided_ThrowsResourceExistedException(){
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 0;
        String isbn = "123456789";
        var bookRequestDto = new BookRequestDTO(sampleTitle1, author, publicationYear, isbn, amount);
        doReturn(true).when(bookRepository).existsByTitle(any(String.class));

        //act & assert
        assertThrows(ResourceExistedException.class, ()-> bookService.addBook(bookRequestDto) );
    }

    @Test
    void testUpdateBook_WhenNotExistingIdProvided_ThrowsResourceNotFoundException(){
        //Arrange
        Long id = 2L;
        doReturn(Optional.empty()).when(bookRepository).findById(any(Long.class));

        //act & assert
        assertThrows(ResourceNotFoundException.class, ()-> bookService.updateBook(id, null) );


    }

    @Test
    void testUpdateBook_WhenTryingToUpdateWithExistedTitleForDifferentBook_ThrowsResourceExistedException(){
        //Arrange
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 0;
        String isbn = "123456789";
        var bookRequestDto = new BookRequestDTO(sampleTitle1, author, publicationYear, isbn, amount);
        var bookEntity =  BookEntity.builder().id(id)
                .author(author).title(sampleTitle1)
                .publicationYear(publicationYear).amount(amount).rented(rented)
                .isbn(isbn)
                .build();

        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(any(Long.class));
        doReturn(true).when(bookRepository).existsByTitleAndIdNot(anyString(), anyLong());
        //act & assert
        assertThrows(ResourceExistedException.class, ()-> bookService.updateBook(id, bookRequestDto) );
    }

    @Test
    void testUpdateBook_WhenValidBookRequestDTO_ReturnsBookResponseDto(){
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 0;
        String isbn = "123456789";
        var bookRequestDto = new BookRequestDTO(sampleTitle1, author, publicationYear, isbn, amount);
        var bookEntity =  BookEntity.builder().id(id)
                .author(author).title(sampleTitle1)
                .publicationYear(publicationYear).amount(amount).rented(rented)
                .isbn(isbn)
                .build();

        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(any(Long.class));
        doReturn(false).when(bookRepository).existsByTitleAndIdNot(anyString(), anyLong());

        //act
        BookResponseDTO result = bookService.updateBook(id, bookRequestDto);
        //assert

        assertNotNull(result);
        assertEquals(result.getTitle(), bookRequestDto.getTitle());
    }
    @Test
    void testUpdateBook_WhenTheRentedAmountGreaterThanAmount_ThrowsIllegalArgumentException(){
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 11;
        String isbn = "123456789";
        var bookRequestDto = new BookRequestDTO(sampleTitle1, author, publicationYear, isbn, amount);
        var bookEntity =  BookEntity.builder().id(id)
                .author(author).title(sampleTitle1)
                .publicationYear(publicationYear).amount(amount).rented(rented)
                .isbn(isbn)
                .build();

        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(any(Long.class));
        doReturn(false).when(bookRepository).existsByTitleAndIdNot(anyString(), anyLong());

        //act&assert
        assertThrows(IllegalArgumentException.class, ()->
                bookService.updateBook(id, bookRequestDto)
        );
    }

    @Test
    public void testDeleteBook_WhenValidIdProvided_DeletesBookAndThrowsNoException(){
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 0;
        String isbn = "123456789";
        var bookEntity =  BookEntity.builder().id(id)
                .author(author).title(sampleTitle1)
                .publicationYear(publicationYear).amount(amount).rented(rented)
                .isbn(isbn)
                .build();

        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        //act & assert
        assertDoesNotThrow(() -> {
            bookService.deleteBook(id);
        });
    }

    @Test
    public void testDeleteBook_WhenBookIsOutOfInventory_ThrowsIllegalArgumentException(){
        //Arrange
        Long id = 1L;
        String author = "sample author 1";
        String sampleTitle1 = "sample title1";
        int publicationYear = 2023;
        int amount = 10;
        int rented = 1;
        String isbn = "123456789";
        var bookEntity =  BookEntity.builder().id(id)
                .author(author).title(sampleTitle1)
                .publicationYear(publicationYear).amount(amount).rented(rented)
                .isbn(isbn)
                .build();

        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyLong());
        //act & assert
        assertThrows(IllegalArgumentException.class, ()->
                bookService.deleteBook(id)
                );
    }
    @Test
    public void testDeleteBook_WhenIdNotExisted_ThrowsResourceNotFoundException(){
        //Arrange
        Long id = 1L;

        doReturn(Optional.empty()).when(bookRepository).findById(anyLong());
        //act & assert
        assertThrows(ResourceNotFoundException.class, ()->
                bookService.deleteBook(id)
        );
    }

}