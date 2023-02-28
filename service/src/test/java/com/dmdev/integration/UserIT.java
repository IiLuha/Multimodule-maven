package com.dmdev.integration;

import com.dmdev.database.dao.repositories.UserRepository;
import com.dmdev.database.entity.User;
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

class UserIT extends IntegrationTestBase {

    private User rudUser;
    @Autowired
    private EntityManager session;
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void prepareUserTable() {
        rudUser = HibernateTestUtil.createUserToReadUpdateDelete();
        session.persist(rudUser);
        session.flush();
        rudUser.addUserDetails(HibernateTestUtil.createUserDetails("Ivan", "Ivanov"));
    }

    @Test
    void createUserTest() {
        User cUser = HibernateTestUtil.createUserToInsert();

        repository.save(cUser);

        assertNotNull(cUser.getId());
    }

    @Test
    void readUserTest() {
        session.detach(rudUser);
        Optional<User> maybeUser = repository.findById(rudUser.getId());

        assertFalse(maybeUser.isEmpty());
        assertEquals(rudUser, maybeUser.get());
    }

    @Test
    void deleteUserTest() {
        repository.delete(rudUser);

        User maybeUser = session.find(User.class, rudUser.getId());
        assertNull(maybeUser);
    }

    @Test
    void findAllTest() {
        List<User> results = repository.findAll();
        assertThat(results).hasSize(5);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates", "Steve Jobs", "Sergey Brin", "no information available", "Ivan Ivanov");
    }

    @Test
    void findAllWithGraphsTest() {
        List<User> results = repository.findAll();
        assertThat(results).hasSize(5);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates", "Steve Jobs", "Sergey Brin", "no information available", "Ivan Ivanov");
    }

    @Test
    void findAllByFirstnameTest() {
        List<User> results = repository.findAllByFirstname("Bill");
        assertThat(results).hasSize(1);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates");
    }
}