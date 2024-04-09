package com.zeyad.maid.lms.mapper;

import com.zeyad.maid.lms.dto.response.PatronResponseDTO;
import com.zeyad.maid.lms.entity.PatronEntity;

import java.util.ArrayList;
import java.util.List;

public class PatronResponseMapper {
    public static PatronResponseDTO map(PatronEntity patronEntity){
        if(patronEntity==null)
            return null;
        return PatronResponseDTO.builder()
                .id(patronEntity.getId()).name(patronEntity.getName())
                .address(patronEntity.getAddress()).phoneNumber(patronEntity.getPhoneNumber())
                .build();
    }

    public static List<PatronResponseDTO> map(List<PatronEntity> patronEntityList){
        List<PatronResponseDTO> responseDTOs = new ArrayList<>();
        for(var item: patronEntityList){
            responseDTOs.add(PatronResponseMapper.map(item));
        }
        return responseDTOs;
    }
}
