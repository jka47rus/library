package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.model.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {

    Book createBook(BookRequest request, String categoryName);

    List<Book> findAll();

    Book findBook(String bookName, String author);

    List<Book> findAllByCategory(String categoryName);

    Book updateBook(UUID id, BookRequest request, String categoryName);

    void deleteById(UUID id);

    Book findById(UUID id);
}
