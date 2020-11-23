package com.sky.library.repository;

import com.sky.library.model.Book;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookRepositoryStub implements BookRepository  {
    private static final String BOOK_REFERENCE_PREFIX = "BOOK-";

    private static final String THE_GRUFFALO_REFERENCE = BOOK_REFERENCE_PREFIX + "GRUFF472";
    private static final String WINNIE_THE_POOH_REFERENCE = BOOK_REFERENCE_PREFIX + "POOH222";
    private static final String THE_WIND_IN_THE_WILLOWS_REFERENCE = BOOK_REFERENCE_PREFIX + "WILL987";

    private static final Map<String, Book> books;

    static {
        books = new HashMap<>();
        books.put(THE_GRUFFALO_REFERENCE, new Book(THE_GRUFFALO_REFERENCE, "The Gruffalo", "A mouse taking a walk in the woods."));
        books.put(WINNIE_THE_POOH_REFERENCE, new Book(WINNIE_THE_POOH_REFERENCE, "Winnie The Pooh", "In this first volume, we meet all the friends from the Hundred Acre Wood."));
        books.put(THE_WIND_IN_THE_WILLOWS_REFERENCE, new Book(THE_WIND_IN_THE_WILLOWS_REFERENCE, "The Wind In The Willows", "With the arrival of spring and fine weather outside, " +
                "the good-natured Mole loses patience with spring cleaning. He flees his underground home, emerging to take in the air and ends up at the river, which he has " +
                "never seen before. Here he meets Rat (a water vole), who at this time of year spends all his days in, on and close by the river. Rat takes Mole for a ride " +
                "in his rowing boat. They get along well and spend many more days boating, with Rat teaching Mole the ways of the river."));
    }


    public Book findFirstByReference(String reference) {
        return books.get(reference);
    }

    @Override
    public <S extends Book> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Book> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Book> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Book> findAll() {
        return null;
    }

    @Override
    public Iterable<Book> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Book entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Book> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
