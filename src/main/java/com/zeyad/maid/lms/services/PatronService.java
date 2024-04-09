package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.request.PatronRequestDTO;
import com.zeyad.maid.lms.dto.response.PatronResponseDTO;

import java.util.List;

public interface PatronService {
    List<PatronResponseDTO> findAllPatrons(String name, Integer page, Integer size);

    PatronResponseDTO findById(Long id);
       
    void deletePatron(Long id);

    PatronResponseDTO addPatron(PatronRequestDTO patronRequestDTO);

    PatronResponseDTO updatePatron(Long id, PatronRequestDTO patronRequestDTO);
}
