package com.dmdev.integration.service;

import com.dmdev.database.entity.fields.Role;
import com.dmdev.dto.UserCreateEditDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private static final Integer USER_1 = 1;

    @Autowired
    private UserService service;

    @Test
    void createUserTest() {
        UserCreateEditDto cUser = new UserCreateEditDto(
                "test@gmail.com",
                "testPass",
                Role.USER,
                null
        );

        UserReadDto actualUser = service.create(cUser);

        assertNotNull(actualUser.getId());
    }

    @Test
    void findByIdTest() {
        Optional<UserReadDto> maybeUser = service.findById(USER_1);

        assertFalse(maybeUser.isEmpty());
        assertEquals("Bill@Email.com", maybeUser.get().getEmail());
    }

    @Test
    void deleteUserTest() {
        assertFalse(service.delete(-124));
        assertTrue(service.delete(USER_1));
    }

    @Test
    void findAllWithGraphsTest() {
        List<UserReadDto> results = service.findAll();
        assertThat(results).hasSize(4);

        List<String> firstNames = results.stream()
                .map(UserReadDto::getUserDetails)
                .filter(Objects::nonNull)
                .map(UserDetailsReadDto::getFirstname)
                .collect(toList());
        assertThat(firstNames).containsExactlyInAnyOrder("Bill", "Steve", "Sergey");
    }
//
//    @Test
//    void findAllByFirstnameTest() {
//        List<User> results = service.findAllByFirstname("Bill");
//        assertThat(results).hasSize(1);
//
//        List<String> firstNames = results.stream().map(User::firstName).collect(toList());
//        assertThat(firstNames).containsExactlyInAnyOrder("Bill Gates");
//    }
}
