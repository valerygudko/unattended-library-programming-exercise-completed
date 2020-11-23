package com.sky.library.service;

import com.sky.library.exception.BookNotFoundException;
import com.sky.library.model.Book;
import com.sky.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.sky.library.utility.BookConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl testObj;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    private void setUp(){
        ReflectionTestUtils.setField(testObj, "bookReferencePrefix", "BOOK-", String.class);
        ReflectionTestUtils.setField(testObj, "bookSummaryWordsLength", 3, int.class);
    }

    @Test
    @DisplayName("When invalid book reference passed to retrieve book then IllegalArgumentException thrown")
    void retrieveBook_whenInvalidBookReferencePassed_thenIllegalArgumentExceptionThrown() {
        //when then
        assertThrows(IllegalArgumentException.class, () -> testObj.retrieveBook(INVALID_BOOK_REFERENCE), "Invalid book reference");
        verifyZeroInteractions(bookRepository);
    }

    @Test
    @DisplayName("When valid book reference passed to retrieve book but book not found BookNotFoundException thrown")
    void retrieveBook_whenBookReferencePassed_bookNotFound_thenBookNotFoundExceptionThrown() {
        //given
        when(bookRepository.findFirstByReference(VALID_BOOK_REFERENCE)).thenReturn(null);
        //when then
        assertThrows(BookNotFoundException.class, () -> testObj.retrieveBook(VALID_BOOK_REFERENCE), "No book found");
        verify(bookRepository).findFirstByReference(VALID_BOOK_REFERENCE);
    }

    @Test
    @DisplayName("When valid book reference passed to retrieve book and book found then book retrieved")
    void retrieveBook_whenBookReferencePassed_bookFound_thenBookReturned() throws BookNotFoundException {
        //given
        Book expectedBook = Book.builder().reference(VALID_BOOK_REFERENCE).title(SOME_TITLE).review(REVIEW).build();
        when(bookRepository.findFirstByReference(VALID_BOOK_REFERENCE)).thenReturn(expectedBook);
        //when
        Book result = testObj.retrieveBook(VALID_BOOK_REFERENCE);
        //then
        assertThat(result).isEqualToComparingFieldByField(expectedBook);
        verify(bookRepository).findFirstByReference(VALID_BOOK_REFERENCE);
    }

    @Test
    @DisplayName("When invalid book reference passed to get book summary then IllegalArgumentException thrown")
    void getBookSummary_whenInvalidBookReferencePassed_thenIllegalArgumentExceptionThrown() {
        //when then
        assertThrows(IllegalArgumentException.class, () -> testObj.getBookSummary(INVALID_BOOK_REFERENCE), "Invalid book reference");
        verifyZeroInteractions(bookRepository);
    }

    @Test
    @DisplayName("When valid book reference passed to get book summary but book not found BookNotFoundException thrown")
    void getBookSummary_whenBookReferencePassed_bookNotFound_thenBookNotFoundExceptionThrown() {
        //given
        when(bookRepository.findFirstByReference(VALID_BOOK_REFERENCE)).thenReturn(null);
        //when then
        assertThrows(BookNotFoundException.class, () -> testObj.getBookSummary(VALID_BOOK_REFERENCE), "No book found");
        verify(bookRepository).findFirstByReference(VALID_BOOK_REFERENCE);
    }

    @Test
    @DisplayName("When valid book reference passed o get book summary and book found then book summary returned")
    void getBookSummary_whenBookReferencePassed_bookFound_thenBookSummaryReturned() throws BookNotFoundException {
        //given
        Book book = Book.builder().reference(VALID_BOOK_REFERENCE).title(SOME_TITLE).review(REVIEW).build();
        String expectedSummary = String.format("%s %s %s...", VALID_BOOK_REFERENCE, SOME_TITLE, "This is very");
        when(bookRepository.findFirstByReference(VALID_BOOK_REFERENCE)).thenReturn(book);
        //when
        String result = testObj.getBookSummary(VALID_BOOK_REFERENCE);
        //then
        assertThat(result).isEqualTo(expectedSummary);
        verify(bookRepository).findFirstByReference(VALID_BOOK_REFERENCE);
    }

}