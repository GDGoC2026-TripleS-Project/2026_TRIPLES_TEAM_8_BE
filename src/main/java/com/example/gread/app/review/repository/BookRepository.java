package com.example.gread.app.review.repository;

import com.example.gread.app.review.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
