package com.sky.library.controller;

import com.sky.library.exception.BookNotFoundException;
import com.sky.library.model.Book;
import com.sky.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(
            value = "/book")
    public Book getBook(@RequestParam @Valid String bookReference) throws BookNotFoundException {
        return bookService.retrieveBook(bookReference);
    }

    @GetMapping(
            value = "/summary")
    public String getSummary(@RequestParam @Valid String bookReference) throws BookNotFoundException {
        return bookService.getBookSummary(bookReference);
    }
}
