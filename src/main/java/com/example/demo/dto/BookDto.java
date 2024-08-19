package com.example.demo.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String bookName;
    private String author;
    private String description;
}
