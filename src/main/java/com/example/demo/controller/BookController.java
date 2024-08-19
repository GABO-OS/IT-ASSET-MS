package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.models.Books;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping("/")
    public ResponseEntity<String> addBook(@RequestBody BookDto bookDto){
        bookService.addBook(bookDto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id){
        boolean res =bookService.deleteBook(id);
        if(res){
            return new ResponseEntity<>("Delete", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book id not found", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{id}")
    public long getBook (@PathVariable long id){
       return id;
    }

    //Return all data
    @GetMapping("/")
    public ResponseEntity<List<Books>> getAllBooks(){
        List books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable long id, @RequestBody BookDto bookDto){
        boolean isUpdated = bookService.updateBook(id, bookDto);
        if(isUpdated){
            return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book id not found", HttpStatus.BAD_REQUEST);
        }
    }
}
