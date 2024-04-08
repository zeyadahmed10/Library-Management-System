package com.zeyad.maid.lms.repos;

import com.zeyad.maid.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}