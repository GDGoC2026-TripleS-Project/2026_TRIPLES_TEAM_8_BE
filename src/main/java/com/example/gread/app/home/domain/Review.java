package com.example.gread.app.home.domain;

import com.example.gread.app.bookDetail.domain.Book;
import com.example.gread.app.login.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReaderType readerType;

    private String bookTitle;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Review(String content, String category, Book book, User user, ReaderType readerType, String bookTitle) {
        this.content = content;
        this.category = category;
        this.book = book;
        this.user = user;
        this.readerType = readerType;
        this.bookTitle = bookTitle;
        this.createdAt = LocalDateTime.now();
    }
}