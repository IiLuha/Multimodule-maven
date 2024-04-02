package com.itdev;

import com.itdev.database.dao.repositories.UserAddressRepository;
import com.itdev.database.dao.repositories.UserRepository;
import com.itdev.database.entity.User;
import com.itdev.database.entity.UserAddress;
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
import static org.junit.jupiter.api.Assertions.assertNull;

class UserAddressIT extends IntegrationTestBase {

    private User user;
    private UserAddress rudUserAddress;
    @Autowired
    private EntityManager session;
    @Autowired
    private UserAddressRepository repository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void prepareUserAddressTable() {
        user = HibernateITUtil.createUserToReadUpdateDelete();
        session.persist(user);
        rudUserAddress = HibernateITUtil.createUserAddress();
        user.addUserAddress(rudUserAddress);
        session.persist(rudUserAddress);
        session.flush();
    }

    @Test
    void createUserAddressTest() {
        User createUser = HibernateITUtil.createUserToInsert();
        session.persist(createUser);
        session.detach(createUser);
        UserAddress cUserAddress = HibernateITUtil.createUserAddress();
        createUser.addUserAddress(cUserAddress);

        userRepository.save(createUser);
        session.detach(cUserAddress);
        UserAddress actualUserAddress = session.find(UserAddress.class, cUserAddress.getId());

        assertEquals(cUserAddress, actualUserAddress);
    }

    @Test
    void readUserAddressTest() {
        session.detach(rudUserAddress);
        Optional<UserAddress> actualUserAddress = repository.findById(rudUserAddress.getId());

        assertFalse(actualUserAddress.isEmpty());
        assertEquals(rudUserAddress, actualUserAddress.get());
    }

    @Test
    void deleteUserAddressTest() {
        user.setUserAddress(null);
        repository.delete(rudUserAddress);
        UserAddress actualUserAddress = session.find(UserAddress.class, rudUserAddress.getId());

        assertNull(actualUserAddress);
    }

    @Test
    void findAllTest() {
        List<UserAddress> results = repository.findAll();
        assertThat(results).hasSize(4);

        List<String> fullNames = results.stream().map(UserAddress::getHouse).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("14A", "15", "15K4", "14");
    }
}