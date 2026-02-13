package com.example.gread.app.feed.repository;

import com.example.gread.app.feed.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findByTitleContainingIgnoreCaseOrKeywordsContainingIgnoreCase(String title, String keyword, Pageable pageable);
    Page<Book> findByCategory(String category, Pageable pageable);
}
