package com.example.library.mapper;

import com.example.library.dto.BookResponse;
import com.example.library.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponse bookToResponse(Book book) {
        if (book == null) return null;
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .categoryName(book.getCategory().getName())
                .build();
    }

    public List<BookResponse> booksToResponse(List<Book> books) {
        return books.stream().map(this::bookToResponse).collect(Collectors.toList());
    }
}
