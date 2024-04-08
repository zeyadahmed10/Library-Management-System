package com.zeyad.maid.lms.repos;

import com.zeyad.maid.lms.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}