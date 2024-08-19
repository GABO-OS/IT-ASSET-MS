package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_books")
public class Books {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Column(length = 255, nullable = false, name = "book_name")
    private String bookName;

    @Column(length = 255, nullable = false, name = "author")
    private String author;

    @Column(length = 255, nullable = false, name = "description")
    private String description;
}
