package com.example.demo.repository;

import com.example.demo.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Books, Long> {
    //Use when you want to make Custom Queries with Annotations
//    @Query("SELECT b FROM Books b WHERE b.author = ?1")
//    List<Books> findByAuthor(String author);
}
