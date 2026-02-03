package com.example.gread.app.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Book{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;


    public Book(String title) {
        this.title = title;
    }
}
