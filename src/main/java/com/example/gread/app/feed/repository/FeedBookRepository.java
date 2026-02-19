package com.example.gread.app.feed.repository;

import com.example.gread.app.bookDetail.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBookRepository extends JpaRepository<Book, Long> {

    List<Book> findByMajorName(String majorName);
    List<Book> findByMajorNameIn(List<String> majorNames);
}
