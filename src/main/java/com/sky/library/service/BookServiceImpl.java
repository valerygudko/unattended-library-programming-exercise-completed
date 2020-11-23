package com.sky.library.service;

import com.sky.library.exception.BookNotFoundException;
import com.sky.library.model.Book;
import com.sky.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static com.sky.library.utility.BookConstants.BOOK_SUMMARY_DELIMITER;
import static com.sky.library.utility.BookConstants.BOOK_SUMMARY_POSTFIX;
import static java.lang.String.format;
import static java.lang.String.join;

@Service
public class BookServiceImpl implements BookService {

    @Value("${book.reference.prefix}")
    private String bookReferencePrefix;

    @Value("${book.summary.words.length}")
    private int bookSummaryWordsLength;

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Book retrieveBook(String bookReference) throws BookNotFoundException, IllegalArgumentException {
        if (hasInvalidPrefix(bookReference)){
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(bookRepository.findFirstByReference(bookReference))
                .orElseThrow(BookNotFoundException::new);
    }

    private boolean hasInvalidPrefix(String bookReference){
        return !bookReference.startsWith(bookReferencePrefix);
    }

    @Override
    public String getBookSummary(String bookReference) throws BookNotFoundException, IllegalArgumentException {
        return buildBookSummary(retrieveBook(bookReference));
    }

    private String buildBookSummary(Book book){
        String[] reviewInWords = book.getReview().split("\\s");

        String[] shortReviewInWords = isTooLong(reviewInWords) ?
                getShortRepresentation(reviewInWords) : reviewInWords;
        return format("%s %s %s", book.getReference(), book.getTitle(),
                join(BOOK_SUMMARY_DELIMITER, shortReviewInWords));
    }

    private String[] getShortRepresentation(String[] words) {
        return Stream.concat(Arrays.stream(words, 0, bookSummaryWordsLength - 1),
                Stream.of(appendEllipsisToLastWord(words))).toArray(String[]::new);
    }

    private String appendEllipsisToLastWord(String[] words) {
        return format("%s%s", getTextFromLastWord(words), BOOK_SUMMARY_POSTFIX);
    }

    private String getTextFromLastWord(String[] words) {
        return words[bookSummaryWordsLength - 1].replaceAll("[^a-zA-Z0-9]", "");
    }

    private boolean isTooLong(String[] reviewInWords) {
        return reviewInWords.length > bookSummaryWordsLength;
    }

}
