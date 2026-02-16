package com.example.gread.app.bookDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 기본 CRUD 메서드가 자동으로 제공됩니다.
}
