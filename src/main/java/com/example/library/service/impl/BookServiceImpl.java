package com.example.library.service.impl;

import com.example.library.configuration.AppProperties;
import com.example.library.dto.BookRequest;
import com.example.library.model.Book;
import com.example.library.model.Category;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import com.example.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;


    @Override
    @Cacheable("bookEntities")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = AppProperties.CacheNames.BOOK_BY_ID, key = "#id")
    public Book findById(UUID id) {
        return bookRepository.findById(id).orElseThrow();
    }


    @Override
    @Cacheable(cacheNames = AppProperties.CacheNames.BOOK_NAME_AUTHOR, key = "#bookName + #author")
    public Book findBook(String bookName, String author) {
        Book bookProbe = new Book();
        bookProbe.setName(bookName);
        bookProbe.setAuthor(author);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "category");

        Example<Book> example = Example.of(bookProbe, matcher);
        return bookRepository.findOne(example).get();

    }

    @Override
    @Cacheable(cacheNames = AppProperties.CacheNames.BOOK_BY_CATEGORY, key = "#categoryName")
    public List<Book> findAllByCategory(String categoryName) {
        return bookRepository.findByCategoryName(categoryName);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_BY_CATEGORY, key = "#categoryName", beforeInvocation = true),
            @CacheEvict(value = "bookEntities", allEntries = true)
    })
    public Book createBook(BookRequest request, String categoryName) {
        if (findBook(request.getName(), request.getAuthor()) != null) {
            throw new RuntimeException(MessageFormat.
                    format("Book with name {0} and author {1} already exist!", request.getName(), request.getAuthor()));
        }
        Book book;
        if (!categoryService.existsByName(categoryName)) {
            Category category = categoryService.createCategory(categoryName);
            book = createNewBook(request, category);
        } else {
            Category category = categoryService.findByName(categoryName);
            book = createNewBook(request, category);
        }

        return bookRepository.save(book);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_BY_CATEGORY, allEntries = true),
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_NAME_AUTHOR, allEntries = true),
            @CacheEvict(value = "bookEntities", allEntries = true),
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_BY_ID, key = "#id")
    })
    public Book updateBook(UUID id, BookRequest request, String categoryName) {
        Book book = findById(id);
        if (categoryName == null) {
            book.setName(request.getName());
            book.setAuthor(request.getAuthor());
            return bookRepository.save(book);
        }
        if (!categoryService.existsByName(categoryName)) {
            Category category = categoryService.createCategory(categoryName);
            book.setName(request.getName());
            book.setAuthor(request.getAuthor());
            book.setCategory(category);
            return bookRepository.save(book);
        }
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        return bookRepository.save(book);

    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_BY_CATEGORY, allEntries = true),
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_NAME_AUTHOR, allEntries = true),
            @CacheEvict(value = "bookEntities", allEntries = true),
            @CacheEvict(cacheNames = AppProperties.CacheNames.BOOK_BY_ID, key = "#id")
    })
    public void deleteById(UUID id) {
        Book book = findById(id);
        Category category = categoryService.findById(book.getCategory().getId());
        category.deleteBook(book);
        bookRepository.deleteById(id);
    }


    private Book createNewBook(BookRequest request, Category category) {
        Book book = Book.builder()
                .name(request.getName())
                .author(request.getAuthor())
                .category(category)
                .build();
        category.addBook(book);

        return book;
    }

}
