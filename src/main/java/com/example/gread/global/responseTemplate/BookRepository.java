package com.example.gread.global.responseTemplate;

import com.example.gread.login.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}