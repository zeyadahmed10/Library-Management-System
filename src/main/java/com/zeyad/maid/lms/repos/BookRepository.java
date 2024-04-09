package com.zeyad.maid.lms.repos;

import com.zeyad.maid.lms.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Page<BookEntity> findByTitleContaining(String title, Pageable pageable);
    boolean existsByTitle(String title);
    boolean existsByTitleAndId(String title, Long id);
}