package com.dmdev.integration;

import com.dmdev.database.dao.repositories.AuthorRepository;
import com.dmdev.database.entity.Author;
import com.dmdev.util.HibernateTestUtil;
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

class AuthorIT extends IntegrationTestBase {

    @Autowired
    private EntityManager session;
    @Autowired
    private AuthorRepository repository;
    private Author rudAuthor;

    @BeforeEach
    void prepareAuthorTable() {
        rudAuthor = HibernateTestUtil.createAuthorToReadUpdateDelete();
        session.persist(rudAuthor);
        session.flush();
    }

    @Test
    void createAuthorTest() {
        Author cAuthor = HibernateTestUtil.createAuthorToInsert();

        repository.save(cAuthor);

        assertNotNull(cAuthor.getId());
    }

    @Test
    void readAuthorTest() {
        session.detach(rudAuthor);
        Optional<Author> maybeAuthor = repository.findById(rudAuthor.getId());

        assertFalse(maybeAuthor.isEmpty());
        assertEquals(rudAuthor, maybeAuthor.get());
    }

    @Test
    void deleteAuthorTest() {
        repository.delete(rudAuthor);

        Author actualAuthor = session.find(Author.class, rudAuthor.getId());
        assertNull(actualAuthor);
    }

    @Test
    void findAllTest() {
        List<Author> results = repository.findAll();
        assertThat(results).hasSize(8);

        List<String> fullNames = results.stream().map(Author::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder(
                "Кей Хорстманн",
                "Гари Корнелл",
                "Стивен Кови",
                "Тони Роббинс",
                "Наполеон Хилл",
                "Роберт Кийосаки",
                "Дейл Карнеги",
                "IvanDeleteReadUpdate Ivanov");
    }
}