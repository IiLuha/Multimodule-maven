package com.itdev.querydsl;

import com.itdev.database.dao.repositories.UserRepository;
import com.itdev.dto.UserFilter;
import com.itdev.database.entity.User;
import com.itdev.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.stream.Collectors.toList;

class UserIT extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllTest() {
        List<User> results = userRepository.findAll();
        assertThat(results).hasSize(4);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates", "Steve Jobs", "Sergey Brin", "no information available");
    }

    @Test
    public void findAllByFilterTest() {
        UserFilter filter = UserFilter.builder()
                .firstname("Bill")
                .build();

        List<User> results = userRepository.findAllByFirstname("Bill");
        assertThat(results).hasSize(1);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates");
    }
}