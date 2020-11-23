package com.sky.library.service;

/*
 * Copyright © 2015 Sky plc All Rights reserved.
 * Please do not make your solution publicly available in any way e.g. post in forums or commit to GitHub.
 */

import com.sky.library.exception.BookNotFoundException;
import com.sky.library.model.Book;

public interface BookService {
    Book retrieveBook(String bookReference) throws BookNotFoundException, IllegalArgumentException;
    String getBookSummary(String bookReference) throws BookNotFoundException, IllegalArgumentException;
}
