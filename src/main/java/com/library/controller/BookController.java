package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // GET all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // GET search books
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    // POST add a new book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // POST add multiple books at once (bulk)
    @PostMapping("/bulk")
    public ResponseEntity<String> addBulkBooks(@RequestBody List<Book> books) {
        books.forEach(book -> book.setAvailable(true));
        bookRepository.saveAll(books);
        return ResponseEntity.ok("Added " + books.size() + " books successfully");
    }

    // DELETE a book by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.ok("Book deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
