package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.request.PatronRequestDTO;
import com.zeyad.maid.lms.dto.response.PatronResponseDTO;
import com.zeyad.maid.lms.entity.PatronEntity;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import com.zeyad.maid.lms.mapper.PatronResponseMapper;
import com.zeyad.maid.lms.repos.BorrowingRecordRepository;
import com.zeyad.maid.lms.repos.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PatronServiceImpl implements PatronService{

    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    @Override
    public List<PatronResponseDTO> findAllPatrons(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatronEntity> patronEntityPage = patronRepository.findByNameContaining(name, pageable);
        return PatronResponseMapper.map(patronEntityPage.getContent());
    }

    @Override
    public PatronResponseDTO findById(Long id) {
        PatronEntity patronEntity = patronRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Patron found for id: " + id));
        return PatronResponseMapper.map(patronEntity);
    }
    @Transactional
    @Override
    public void deletePatron(Long id) {
        PatronEntity patronEntity = patronRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Patron found for id: " + id));
        if(borrowingRecordRepository.existsByPatronIdAndActualReturnDateIsNull(id))
            throw new ResourceExistedException("Can't delete the patron with id: "+id+", must returns all books first");
        patronRepository.delete(patronEntity);

    }
    @Transactional
    @Override
    public PatronResponseDTO addPatron(PatronRequestDTO patronRequestDTO) {
        if(patronRepository.existsByPhoneNumber(patronRequestDTO.getPhoneNumber()))
            throw new ResourceExistedException("Patron already exists with phone number: " + patronRequestDTO.getPhoneNumber());

        PatronEntity patronEntity = PatronEntity.builder().address(patronRequestDTO.getAddress())
                .name(patronRequestDTO.getName()).phoneNumber(patronRequestDTO.getPhoneNumber())
                .build();

        return PatronResponseMapper.map(patronRepository.save(patronEntity));
    }
    @Transactional
    @Override
    public PatronResponseDTO updatePatron(Long id, PatronRequestDTO patronRequestDTO) {
        PatronEntity patronEntity = patronRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Patron found for id: " + id));

        if(patronRepository.existsByPhoneNumberAndIdNot(patronRequestDTO.getPhoneNumber(), id))
            throw new ResourceExistedException("Patron already exists with phone number: " + patronRequestDTO.getPhoneNumber());

        patronEntity.setAddress((patronRequestDTO.getAddress()==null) ? patronEntity.getAddress() : patronRequestDTO.getAddress());
        patronEntity.setPhoneNumber((patronRequestDTO.getPhoneNumber()==null) ? patronEntity.getPhoneNumber() : patronRequestDTO.getPhoneNumber());
        patronEntity.setName((patronRequestDTO.getName()==null) ? patronEntity.getName() : patronRequestDTO.getName());

        patronRepository.save(patronEntity);
        return PatronResponseMapper.map(patronEntity);
    }
}
