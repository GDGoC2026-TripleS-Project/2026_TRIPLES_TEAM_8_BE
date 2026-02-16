/*
package com.example.gread.app.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "FeedBook")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feed_books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String publisher;
    
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @Column(name = "keyword1")
    private String keyword1;
    
    @Column(name = "keyword2")
    private String keyword2;
}
*/
