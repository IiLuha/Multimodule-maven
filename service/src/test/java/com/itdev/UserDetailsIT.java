package com.itdev;

import com.itdev.database.dao.repositories.UserDetailsRepository;
import com.itdev.database.entity.User;
import com.itdev.database.entity.UserDetails;
import com.itdev.util.HibernateITUtil;
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

class UserDetailsIT extends IntegrationTestBase {

    private User user;
    private UserDetails rudUserDetails;
    @Autowired
    private EntityManager session;
    @Autowired
    private UserDetailsRepository repository;

    @BeforeEach
    void prepareUserDetailsTable() {
        user = HibernateITUtil.createUserToReadUpdateDelete();
        session.persist(user);
        rudUserDetails = HibernateITUtil.createUserDetails();
        user.addUserDetails(rudUserDetails);
        session.persist(rudUserDetails);
        session.flush();
    }

    @Test
    void createUserDetailsTest() {
        User createUser = HibernateITUtil.createUserToInsert();
        session.persist(createUser);
        UserDetails cUserDetails = HibernateITUtil.createUserDetails();
        createUser.addUserDetails(cUserDetails);

        repository.save(cUserDetails);

        assertNotNull(cUserDetails.getId());
    }

    @Test
    void readUserDetailsTest() {
        session.detach(rudUserDetails);
        Optional<UserDetails> maybeUser = repository.findById(rudUserDetails.getId());

        assertFalse(maybeUser.isEmpty());
        assertEquals(rudUserDetails, maybeUser.get());
    }

    @Test
    void deleteUserDetailsTest() {
        user.setUserDetails(null);
        repository.delete(rudUserDetails);
        UserDetails actualUserDetails = session.find(UserDetails.class, rudUserDetails.getId());

        assertNull(actualUserDetails);
    }

    @Test
    void findAllTest() {
        List<UserDetails> results = repository.findAll();
        assertThat(results).hasSize(4);

        List<String> fullNames = results.stream().map(userDetails -> userDetails.getUser().fullName()).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates", "Steve Jobs", "Sergey Brin", "Pavel Pavlov");
    }
}