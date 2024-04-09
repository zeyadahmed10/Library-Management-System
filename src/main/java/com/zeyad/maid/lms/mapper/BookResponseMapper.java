package com.zeyad.maid.lms.mapper;

import com.zeyad.maid.lms.dto.response.BookResponseDTO;
import com.zeyad.maid.lms.entity.BookEntity;

import java.util.ArrayList;
import java.util.List;

public class BookResponseMapper {
    public static BookResponseDTO map(BookEntity bookEntity){
        if(bookEntity==null)
            return null;
        return BookResponseDTO.builder()
                .id(bookEntity.getId()).title(bookEntity.getTitle())
                .author(bookEntity.getAuthor()).isbn(bookEntity.getAuthor())
                .publicationYear(bookEntity.getPublicationYear()).build();
    }
    public static List<BookResponseDTO> map(List<BookEntity> bookEntityList){
        List<BookResponseDTO> responseDTOs = new ArrayList<>();
        for(var item: bookEntityList){
            responseDTOs.add(BookResponseMapper.map(item));
        }
        return responseDTOs;
    }
}
