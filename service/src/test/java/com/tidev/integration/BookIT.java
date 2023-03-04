package com.tidev.integration;

import com.tidev.database.dao.repositories.AuthorRepository;
import com.tidev.database.dao.repositories.BookRepository;
import com.tidev.database.entity.Author;
import com.tidev.database.entity.Book;
import com.tidev.util.HibernateTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookIT extends IntegrationTestBase {

    private Book book;
    @Autowired
    private EntityManager session;
    @Autowired
    private BookRepository repository;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void prepareBookTable(){
        Author author = HibernateTestUtil.createAuthorToReadUpdateDelete();
        book = HibernateTestUtil.createRudBook(author);
        session.persist(author);
        session.persist(book);
        session.flush();
    }

    @Test
    void createBookTest() {
        Author author = HibernateTestUtil.createAuthorToInsert();
        Book book = HibernateTestUtil.createBook(author);

        session.persist(author);
        repository.save(book);

        assertNotNull(book.getId());
    }

    @Test
    void readBookTest() {
        session.detach(book);
        Optional<Book> maybeBook = repository.findById(book.getId());

        assertFalse(maybeBook.isEmpty());
        assertEquals(book, maybeBook.get());
    }

    @Test
    void deleteBookTest() {
        repository.delete(book);
        Book actualBook = session.find(Book.class, book.getId());

        assertNull(actualBook);
    }

    @Test
    void getAllTest() {
        List<Book> results = repository.findAll();
        assertThat(results).hasSize(11);

        List<String> fullNames = results.stream().map(Book::getName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder(
                "Java. Библиотека профессионала. Том 1",
                "Java. Библиотека профессионала. Том 2",
                "Java SE 8. Вводный курс",
                "7 навыков высокоэффективных людей",
                "Разбуди в себе исполина",
                "Думай и богатей",
                "Богатый папа, бедный папа",
                "Квадрант денежного потока",
                "Как перестать беспокоиться и начать жить",
                "Как завоевывать друзей и оказывать влияние на людей",
                "RUDBook"
        );
    }

    @Test
    void getAllByAuthorIdTest() {
        Optional<Author> authorForFind = authorRepository.findTopBy();
        assertFalse(authorForFind.isEmpty());

        List<Book> results = repository.findAllByAuthorId(authorForFind.get().getId());

        assertThat(results).hasSize(authorForFind.get().getBooks().size());
    }

    @Test
    void getAllByIssueYearTest() {
        List<Book> results = repository.findAllByIssueYear(book.getIssueYear());

        assertThat(results).contains(book);
        assertThat(results).hasSize(2);
    }
}