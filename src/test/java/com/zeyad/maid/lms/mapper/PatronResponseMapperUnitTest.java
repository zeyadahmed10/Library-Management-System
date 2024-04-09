package com.zeyad.maid.lms.mapper;

import com.zeyad.maid.lms.dto.response.PatronResponseDTO;
import com.zeyad.maid.lms.entity.PatronEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatronResponseMapperUnitTest {
    @Test
    void tesMap_WithValidPatronEntity_ReturnsCorrectPatronResponseDTO() {
        // Arrange
        PatronEntity patronEntity = new PatronEntity();
        patronEntity.setId(1L);
        patronEntity.setName("Zeyad Ahmed");
        patronEntity.setAddress("new cairo");
        patronEntity.setPhoneNumber("+1234567890");

        // Act
        PatronResponseDTO patronResponseDTO = PatronResponseMapper.map(patronEntity);

        // Assert
        assertNotNull(patronResponseDTO);
        assertEquals(patronEntity.getId(), patronResponseDTO.getId());
        assertEquals(patronEntity.getName(), patronResponseDTO.getName());
        assertEquals(patronEntity.getAddress(), patronResponseDTO.getAddress());
        assertEquals(patronEntity.getPhoneNumber(), patronResponseDTO.getPhoneNumber());
    }
    @Test
    void testMap_whenNullPatronEntity_ReturnsNull(){
        PatronEntity patronEntity = null;
        PatronResponseDTO patronResponseDTO = PatronResponseMapper.map(patronEntity);
        assertEquals(patronResponseDTO, null);
    }
    @Test
    void testMapList_WithValidPatronEntityList_ReturnsCorrectPatronResponseDTOList() {
        // Arrange
        List<PatronEntity> patronEntityList = new ArrayList<>();
        PatronEntity patronEntity1 = new PatronEntity();
        patronEntity1.setId(1L);
        patronEntity1.setName("Zeyad Ahmed");
        patronEntity1.setAddress("new cairo");
        patronEntity1.setPhoneNumber("+1234567890");

        PatronEntity patronEntity2 = new PatronEntity();
        patronEntity2.setId(2L);
        patronEntity2.setName("Hesham Ahmed");
        patronEntity2.setAddress("EL-nuzha");
        patronEntity2.setPhoneNumber("+91222222");

        patronEntityList.add(patronEntity1);
        patronEntityList.add(patronEntity2);

        // Act
        List<PatronResponseDTO> patronResponseDTOList = PatronResponseMapper.map(patronEntityList);

        // Assert
        assertNotNull(patronResponseDTOList);
        assertEquals(2, patronResponseDTOList.size());

        // Assert the first patron
        PatronEntity firstPatronEntity = patronEntityList.get(0);
        PatronResponseDTO firstPatronResponseDTO = patronResponseDTOList.get(0);
        assertEquals(firstPatronEntity.getId(), firstPatronResponseDTO.getId());
        assertEquals(firstPatronEntity.getName(), firstPatronResponseDTO.getName());
        assertEquals(firstPatronEntity.getAddress(), firstPatronResponseDTO.getAddress());
        assertEquals(firstPatronEntity.getPhoneNumber(), firstPatronResponseDTO.getPhoneNumber());

        // Assert the second patron
        PatronEntity secondPatronEntity = patronEntityList.get(1);
        PatronResponseDTO secondPatronResponseDTO = patronResponseDTOList.get(1);
        assertEquals(secondPatronEntity.getId(), secondPatronResponseDTO.getId());
        assertEquals(secondPatronEntity.getName(), secondPatronResponseDTO.getName());
        assertEquals(secondPatronEntity.getAddress(), secondPatronResponseDTO.getAddress());
        assertEquals(secondPatronEntity.getPhoneNumber(), secondPatronResponseDTO.getPhoneNumber());
    }
}