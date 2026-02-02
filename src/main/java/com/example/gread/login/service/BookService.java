package com.example.gread.login.service;

import com.example.gread.login.domain.Book;
import com.example.gread.login.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * 전체 도서 목록 조회
     */
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * 특정 도서 상세 조회
     */
    public Book findOne(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 없습니다."));
    }
}