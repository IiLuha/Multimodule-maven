package com.tidev.integration.http.controller;

import com.tidev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.tidev.dto.UserCreateEditDto.Fields.email;
import static com.tidev.dto.UserCreateEditDto.Fields.rawPassword;
import static com.tidev.dto.UserCreateEditDto.Fields.role;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user"));
    }

//    @Test
    void registration() throws Exception {
        mockMvc.perform(
                    get("/users/registration")
                        .param(email, "testt@gmail.com")
                        .param(rawPassword, "testPass")
                        .param(role, "USER")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/user/registration"));
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/login"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users/create")
                        .param(email, "testt@gmail.com")
                        .param(rawPassword, "testPass")
                        .param(role, "USER")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/logi?")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/users/2/update")
                        .param(email, "ttest@gmail.com")
                        .param(rawPassword, "testPass")
                        .param(role, "USER")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/users/2/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/user?")
                );
    }
}