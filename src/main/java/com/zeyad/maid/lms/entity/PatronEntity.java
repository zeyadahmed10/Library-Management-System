package com.zeyad.maid.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patrons", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatronEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String address;
    @Column(unique = true)
    private String phoneNumber;
}
