package com.example.demo.services;

import com.example.demo.dto.BookDto;
import com.example.demo.models.Books;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    public String addBook(BookDto bookDto) {
        Books book = new Books();
        book.setBookName(bookDto.getBookName());
        book.setAuthor(bookDto.getAuthor());
        book.setDescription(bookDto.getDescription());

        bookRepository.save(book);
        return "Saved Successfully";
    }
    public boolean deleteBook(long id) {
        Optional<Books> existingBook = bookRepository.findById(id);
        if(existingBook.isPresent()){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean updateBook(long id, BookDto bookDto) {
        Optional<Books> existingBook = bookRepository.findById(id);
        if(existingBook.isPresent()){
            Books bookToUpdate = existingBook.get();
            bookToUpdate.setBookName(bookDto.getBookName());
            bookToUpdate.setAuthor(bookDto.getAuthor());
            bookToUpdate.setDescription(bookDto.getDescription());
            bookRepository.save(bookToUpdate);
            return true;
        }
        return false;
    }
}
