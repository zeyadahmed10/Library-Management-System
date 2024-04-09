package com.zeyad.maid.lms.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import com.zeyad.maid.lms.dto.request.PatronRequestDTO;
import com.zeyad.maid.lms.dto.response.PatronResponseDTO;
import com.zeyad.maid.lms.entity.PatronEntity;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import com.zeyad.maid.lms.repos.BorrowingRecordRepository;
import com.zeyad.maid.lms.repos.PatronRepository;
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

class PatronServiceImplUnitTest {
    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPatrons_WhenRetrievingEntityList_ShouldReturnCorrectPatronResponseDTOList() {
        // Arrange
        String name = "zeyad";
        int page = 0;
        int size = 10;
        var patronEntity = PatronEntity.builder()
                .id(1L)
                .name("Zeyad Ahmed")
                .address("cairo")
                .phoneNumber("1234567890")
                .build();
        List<PatronEntity> patronEntityList = Collections.singletonList(patronEntity);
        Page<PatronEntity> patronEntityPage = new PageImpl<>(patronEntityList);
        doReturn(patronEntityPage).when(patronRepository).findByNameContaining(anyString(), any(Pageable.class));

        // Act
        List<PatronResponseDTO> result = patronService.findAllPatrons(name, page, size);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(patronEntity.getName(), result.get(0).getName());
        assertEquals(patronEntity.getAddress(), result.get(0).getAddress());
        assertEquals(patronEntity.getPhoneNumber(), result.get(0).getPhoneNumber());
    }

    @Test
    void testFindById_WhenExistingIdProvided_ReturnsCorrectPatronResponseDTO() {
        // Arrange
        Long id = 1L;
        var patronEntity = PatronEntity.builder()
                .id(id)
                .name("Zeyad Ahmed")
                .address("cairo")
                .phoneNumber("1234567890")
                .build();
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(any(Long.class));

        // Act
        PatronResponseDTO result = patronService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(patronEntity.getName(), result.getName());
        assertEquals(patronEntity.getAddress(), result.getAddress());
        assertEquals(patronEntity.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void testFindById_whenNonExistedIdProvided_ThrowsResourceNotFoundException(){
        Long id = 1L;
        doReturn(Optional.empty()).when(patronRepository).findById(any(Long.class));

        //act & assert
        assertThrows(ResourceNotFoundException.class, ()-> patronService.findById(id) );
    }
    @Test
    public void testDeletePatron_WhenNotExistedIdProvided_ThrowsResourceNotFoundException(){
        Long id = 1L;
        doReturn(Optional.empty()).when(patronRepository).findById(any(Long.class));

        //act & assert
        assertThrows(ResourceNotFoundException.class, ()-> patronService.deletePatron(id) );
    }
    @Test
    public void testDeletePatron_WhenNotAllBooksReturned_ThrowsResourceExistedException(){
        //Arrange
        Long id = 1L;
        var patronEntity = PatronEntity.builder()
                .id(1L)
                .name("Zeyad Ahmed")
                .address("cairo")
                .phoneNumber("1234567890")
                .build();
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());
        doReturn(true).when(borrowingRecordRepository).existsByPatronIdAndActualReturnDateIsNull(anyLong());
        //act & assert
        assertThrows(ResourceExistedException.class, ()->
                patronService.deletePatron(id)
        );
    }

    @Test
    public void testDeletePatron_WhenValidIdProvided_DeletesPatronAndThrowsNoException(){
        //Arrange
        Long id = 1L;
        var patronEntity = PatronEntity.builder()
                .id(1L)
                .name("Zeyad Ahmed")
                .address("cairo")
                .phoneNumber("1234567890")
                .build();
        doReturn(Optional.of(patronEntity)).when(patronRepository).findById(anyLong());
        doReturn(false).when(borrowingRecordRepository).existsByPatronIdAndActualReturnDateIsNull(anyLong());
        //act & assert
        assertDoesNotThrow(()->
                patronService.deletePatron(id)
        );
    }

    @Test
    void testAddPatron_WhenPatronDoesNotExist_ShouldReturnPatronResponseDTO() {
        // Arrange
        String name = "Zeyad Ahmed";
        String address = "Cairo";
        String phoneNumber = "1234567890";
        var patronRequestDTO = new PatronRequestDTO(name, address, phoneNumber);
        var patronEntity = PatronEntity.builder()
                .id(1L)
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
        doReturn(false).when(patronRepository).existsByPhoneNumber(anyString());
        doReturn(patronEntity).when(patronRepository).save(any(PatronEntity.class));

        // Act
        PatronResponseDTO result = patronService.addPatron(patronRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(patronEntity.getId(), result.getId());
        assertEquals(patronEntity.getName(), result.getName());
        assertEquals(patronEntity.getAddress(), result.getAddress());
        assertEquals(patronEntity.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void testAddPatron_WhenPatronAlreadyExists_ShouldThrowResourceExistedException() {
        // Arrange
        String name = "Zeyad Ahmed";
        String address = "Cairo";
        String phoneNumber = "1234567890";
        var patronRequestDTO = new PatronRequestDTO(name, address, phoneNumber);
        doReturn(true).when(patronRepository).existsByPhoneNumber(anyString());

        // Act & Assert
        assertThrows(ResourceExistedException.class,
                () -> patronService.addPatron(patronRequestDTO));
    }
    @Test
    void testUpdatePatron_WhenPatronExistsAndPhoneNumberNotChanged_ShouldReturnUpdatedPatron() {
        // Arrange
        Long id = 1L;
        String name = "John Doe";
        String address = "123 Main St";
        String phoneNumber = "1234567890";
        var patronRequestDTO = new PatronRequestDTO(name, address, phoneNumber);
        var existingPatronEntity = PatronEntity.builder()
                .id(id)
                .name("Previous Name")
                .address("Previous Address")
                .phoneNumber(phoneNumber)
                .build();
        var updatedPatronEntity = PatronEntity.builder()
                .id(id)
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
        doReturn(Optional.of(existingPatronEntity)).when(patronRepository).findById(anyLong());
        doReturn(false).when(patronRepository).existsByPhoneNumberAndIdNot(anyString(), anyLong());
        doReturn(updatedPatronEntity).when(patronRepository).save(any(PatronEntity.class));

        // Act
        PatronResponseDTO result = patronService.updatePatron(id, patronRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedPatronEntity.getId(), result.getId());
        assertEquals(updatedPatronEntity.getName(), result.getName());
        assertEquals(updatedPatronEntity.getAddress(), result.getAddress());
        assertEquals(updatedPatronEntity.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void testUpdatePatron_WhenPatronExistsAndPhoneNumberChanged_ShouldThrowResourceExistedException() {
        // Arrange
        Long id = 1L;
        String name = "John Doe";
        String address = "123 Main St";
        String phoneNumber = "1234567890";
        var patronRequestDTO = new PatronRequestDTO(name, address, phoneNumber);
        var existingPatronEntity = PatronEntity.builder()
                .id(id)
                .name("Previous Name")
                .address("Previous Address")
                .phoneNumber("Different Phone Number")
                .build();
        doReturn(Optional.of(existingPatronEntity)).when(patronRepository).findById(anyLong());
        doReturn(true).when(patronRepository).existsByPhoneNumberAndIdNot(anyString(), anyLong());

        // Act & Assert
        ResourceExistedException exception = assertThrows(ResourceExistedException.class,
                () -> patronService.updatePatron(id, patronRequestDTO));
        assertEquals("Patron already exists with phone number: " + phoneNumber, exception.getMessage());
    }

    @Test
    void testUpdatePatron_WhenPatronDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        doReturn(Optional.empty()).when(patronRepository).findById(anyLong());
        var patronRequestDTO = new PatronRequestDTO("John Doe", "123 Main St", "1234567890");

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patronService.updatePatron(id, patronRequestDTO));
        assertEquals("No Patron found for id: " + id, exception.getMessage());
    }

}