package com.example.gread.app.feed.repository;

import com.example.gread.app.feed.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCaseOrKeywordsContainingIgnoreCase(String title, String keyword, Pageable pageable);
}
