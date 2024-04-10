package com.zeyad.maid.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronResponseDTO implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
}
