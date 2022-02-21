package com.springboot.dynamodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.dynamodb.entity.Book;
import com.springboot.dynamodb.entity.BookDto;
import com.springboot.dynamodb.exception.EntityNotFound;
import com.springboot.dynamodb.service.BookService;

import lombok.extern.slf4j.Slf4j;

//lombok annotation for logger
@Slf4j
//spring annotations
@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookService service;

    // HTTP GET URL - http://localhost:8080/api/books
    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooks() {
        log.info("Getting all books from the db");
        return service.getBooks();
    }

    // HTTP GET URL - http://localhost:8080/api/books/<book_genre>
    @GetMapping("/books/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooksByGenre(@PathVariable("genre") final String genre) {
        log.info("Getting books by genre = {} from the db", genre);
        return service.getBooksByGenre(genre);
    }

    // HTTP GET URL - http://localhost:8080/api/book/<book_id>
    @GetMapping("/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(@PathVariable("id") final String id) throws EntityNotFound {
        log.info("Getting book id = {} from the db", id);
        return service.getBookById(id);
    }

    // HTTP DELETE URL - http://localhost:8080/api/book/<book_id>
    @DeleteMapping("/book/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") final String id) throws EntityNotFound {
        log.info("Delete book id = {} from the db", id);
        service.delete(id);
    }

    // HTTP PUT URL  - http://localhost:8080/api/book/<book_id>
    // Sample request body
    /*
    {
        "author": "J. K. Rowling",
        "genre": "Fantasy Fiction",
        "publisher": "Bloomsbury Publishing",
        "title": "Harry Potter",
        "quantity": 100
    }
    */
    @PutMapping("/book/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") final String id, @RequestBody final BookDto dto)
            throws EntityNotFound {
        log.info("Updating book id = {} into the db", id);
        service.update(id, dto);
    }

    // HTTP POST URL  - http://localhost:8080/api/book
    // Sample request body
    /*
    {
        "author": "Vasdev Mohi",
        "genre": "Ghazals",
        "publisher": "Central Sahitya Akademi",
        "title": "Cheque book",
        "quantity": 5
    }
    */
    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody final BookDto dto) {
        log.info("Saving new book = {} into the db", dto.toString());
        service.save(dto);
    }

    // HTTP POST URL - http://localhost:8080/api/books/count/<book_genre>
    @GetMapping("/books/count/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public long getCountByGenre(@PathVariable("genre") final String genre) {
        return service.getCountByGenre(genre);
    }
}
