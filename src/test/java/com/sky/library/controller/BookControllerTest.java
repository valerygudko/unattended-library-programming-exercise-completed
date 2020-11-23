package com.sky.library.controller;

import com.sky.library.exception.BookNotFoundException;
import com.sky.library.model.Book;
import com.sky.library.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.sky.library.utility.BookConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("When invalid book reference passed to retrieve book then bad request error is returned")
    void getBook_invalidBookRef_returns400() throws Exception {
        when(bookService.retrieveBook(INVALID_BOOK_REFERENCE)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/books/book")
                .param(BOOK_REFERENCE_REQUEST_PARAM, INVALID_BOOK_REFERENCE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When valid book reference passed to retrieve book and book not found then not found error is returned")
    void getBook_validBookRef_bookNotFound_returns404() throws Exception {
        when(bookService.retrieveBook(VALID_BOOK_REFERENCE_NOT_FOUND)).thenThrow(BookNotFoundException.class);

        mockMvc.perform(get("/books/book")
                .param(BOOK_REFERENCE_REQUEST_PARAM, VALID_BOOK_REFERENCE_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When valid book reference passed to retrieve book and book found then book is returned")
    void getBook_validBookRef_bookFound_returns200() throws Exception {

        Book expectedBook = Book.builder().reference(VALID_BOOK_REFERENCE).title(SOME_TITLE).review(REVIEW).build();
        when(bookService.retrieveBook(VALID_BOOK_REFERENCE)).thenReturn(expectedBook);

        mockMvc.perform(get("/books/book")
                .param(BOOK_REFERENCE_REQUEST_PARAM, VALID_BOOK_REFERENCE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value(VALID_BOOK_REFERENCE))
                .andExpect(jsonPath("$.title").value(SOME_TITLE))
                .andExpect(jsonPath("$.review").value(REVIEW));

    }

    @Test
    @DisplayName("When invalid book reference passed to get book summary then bad request error is returned")
    void getSummary_invalidBookRef_returns400() throws Exception {
        when(bookService.getBookSummary(INVALID_BOOK_REFERENCE)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/books/summary")
                .param(BOOK_REFERENCE_REQUEST_PARAM, INVALID_BOOK_REFERENCE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When valid book reference passed to get book summary and book not found then not found error is returned")
    void getSummary_validBookRef_bookNotFound_returns404() throws Exception {
        when(bookService.getBookSummary(VALID_BOOK_REFERENCE_NOT_FOUND)).thenThrow(BookNotFoundException.class);

        mockMvc.perform(get("/books/summary")
                .param(BOOK_REFERENCE_REQUEST_PARAM, VALID_BOOK_REFERENCE_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When valid book reference passed to get book summary and book found then book summary is returned")
    void getSummary_validBookRef_bookFound_returns200() throws Exception {

        String expectedSummary = String.format("%s %s %s...", VALID_BOOK_REFERENCE, SOME_TITLE, "This is very");
        when(bookService.getBookSummary(VALID_BOOK_REFERENCE)).thenReturn(expectedSummary);

        MvcResult result = mockMvc.perform(get("/books/summary")
                .param(BOOK_REFERENCE_REQUEST_PARAM, VALID_BOOK_REFERENCE))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(expectedSummary);
    }
}