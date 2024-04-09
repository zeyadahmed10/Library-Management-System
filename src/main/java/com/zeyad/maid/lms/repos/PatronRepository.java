package com.zeyad.maid.lms.repos;

import com.zeyad.maid.lms.entity.PatronEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<PatronEntity, Long> {
    Page<PatronEntity> findByNameContaining(String Name, Pageable pageable);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumberAndIdNot(String title, Long id);
}