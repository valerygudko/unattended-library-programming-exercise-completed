package com.sky.library.controller;

import com.sky.library.repository.BookRepository;
import com.sky.library.repository.BookRepositoryStub;
import com.sky.library.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.sky.library.utility.BookConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest({"spring.main.allow-bean-definition-overriding=true"})
@Import(BookControllerFunctionalTest.OverrideBean.class)
@AutoConfigureMockMvc
public class BookControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @TestConfiguration
    public static class OverrideBean {

        @Bean
        public BookRepository bookRepository() {
            return new BookRepositoryStub();
        }
    }

    @Test
    @DisplayName("When invalid book reference passed to retrieve book then bad request error is returned")
    void getBook_invalidBookRef_returns400() throws Exception {

        mockMvc.perform(get("/books/book")
                .param(BOOK_REFERENCE_REQUEST_PARAM, INVALID_BOOK_REFERENCE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When valid book reference passed to retrieve book and book not found then not found error is returned")
    void getBook_validBookRef_bookNotFound_returns404() throws Exception {

        mockMvc.perform(get("/books/book")
                .param(BOOK_REFERENCE_REQUEST_PARAM, VALID_BOOK_REFERENCE_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @DisplayName("When valid book reference passed to retrieve book and book found then book is returned")
    @ParameterizedTest(name = "Valid book returned for the book reference: {0}")
    @CsvFileSource(resources = "validBookResponses.csv")
    void getBook_validBookRef_bookFound_returns200(String bookReference, String title, String review) throws Exception {

        mockMvc.perform(get("/books/book")
                .param(BOOK_REFERENCE_REQUEST_PARAM, bookReference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value(bookReference))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.review").value(review));

    }

    @Test
    @DisplayName("When invalid book reference passed to get book summary then bad request error is returned")
    void getSummary_invalidBookRef_returns400() throws Exception {

        mockMvc.perform(get("/books/summary")
                .param(BOOK_REFERENCE_REQUEST_PARAM, INVALID_BOOK_REFERENCE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When valid book reference passed to get book summary and book not found then not found error is returned")
    void getSummary_validBookRef_bookNotFound_returns404() throws Exception {

        mockMvc.perform(get("/books/summary")
                .param(BOOK_REFERENCE_REQUEST_PARAM, VALID_BOOK_REFERENCE_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @DisplayName("When valid book reference passed to get book summary and book found then book summary is returned")
    @ParameterizedTest(name = "Valid book summary returned for the book reference: {0}")
    @CsvFileSource(resources = "validBookSummaryResponses.csv")
    void getSummary_validBookRef_bookFound_returns200(String bookReference, String title, String shortReview) throws Exception {

        String expectedSummary = String.format("%s %s %s", bookReference, title, shortReview);

        MvcResult result = mockMvc.perform(get("/books/summary")
                .param(BOOK_REFERENCE_REQUEST_PARAM, bookReference))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(expectedSummary);
    }
}
