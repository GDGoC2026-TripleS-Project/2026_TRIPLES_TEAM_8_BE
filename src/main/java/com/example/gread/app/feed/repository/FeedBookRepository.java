package com.example.gread.app.feed.repository;

import com.example.gread.app.bookDetail.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBookRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.keyword1) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.keyword2) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Book> findByMajorName(String majorName, Pageable pageable); // 기존 페이지네이션 포함 메서드

    List<Book> findByMajorName(String majorName); // 페이지네이션 제거된 메서드

    @Query("SELECT b FROM Book b WHERE " +
            "b.majorName = :majorName AND (" +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.keyword1) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.keyword2) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Book> findByMajorNameAndKeyword(@Param("majorName") String majorName, @Param("keyword") String keyword, Pageable pageable);

    Page<Book> findByMajorNameIn(List<String> majorNames, Pageable pageable);
}
