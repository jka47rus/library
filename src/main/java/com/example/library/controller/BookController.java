package com.example.library.controller;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.mapper.BookMapper;
import com.example.library.model.Book;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<BookResponse> crateBook(@RequestBody BookRequest request,
                                                  @RequestParam String category) {
        Book newBook = bookService.createBook(request, category);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(newBook));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookMapper.booksToResponse(bookService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookMapper.bookToResponse(bookService.findById(id)));
    }


    @GetMapping("/find")
    public ResponseEntity<BookResponse> findBook(@RequestParam String bookName,
                                                 @RequestParam String author) {
        return ResponseEntity.ok(bookMapper.bookToResponse(bookService.findBook(bookName, author)));
    }

    @GetMapping("/category")
    ResponseEntity<List<BookResponse>> findAllByCategory(@RequestParam String category) {
        return ResponseEntity.ok(bookMapper.booksToResponse(bookService.findAllByCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id,
                                                   @RequestBody BookRequest request,
                                                   @RequestParam(required = false) String categoryName) {
        Book book = bookService.updateBook(id, request, categoryName);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
