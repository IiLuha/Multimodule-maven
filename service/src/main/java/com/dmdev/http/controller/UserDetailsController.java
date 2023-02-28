package com.dmdev.http.controller;

import com.dmdev.database.entity.fields.Role;
import com.dmdev.dto.PageResponse;
import com.dmdev.dto.UserCreateEditDto;
import com.dmdev.dto.UserDetailsCreateEditDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.dto.UserFilter;
import com.dmdev.dto.UserReadDto;
import com.dmdev.service.UserDetailsService;
import com.dmdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/userDetails")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserDetailsService userDetailsService;

    @GetMapping("/{userId}")
    public String findByUserId(@PathVariable("userId") Integer userId, Model model) {
        return userDetailsService.findByUserId(userId)
                .map(userDetails -> {
                    model.addAttribute("userDetails", userDetails);
                    return "user/userDetails";
                })
                .orElseGet(() -> {
                    model.addAttribute("userId", userId);
                    return "user/userDetailsCreate";
                });
    }

    @PostMapping("/create")
    public String create(@Validated UserDetailsCreateEditDto userDetails) {
        UserDetailsReadDto userDetailsReadDto = userDetailsService.create(userDetails);
        return "redirect:/users/" + userDetailsReadDto.getUserId();
    }

    @PostMapping("/{userId}/update")
    public String updateByUserId(@PathVariable("userId") Integer userId, @Validated UserDetailsCreateEditDto userDetails) {
        Optional<UserDetailsReadDto> update = userDetailsService.updateByUserId(userId, userDetails);
        return update
                .map(it -> "redirect:/users/{userId}/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{userId}/delete")
    public String deleteByUserId(@PathVariable("userId") Integer userId) {
        if (userDetailsService.deleteByUserId(userId)) {
            return "redirect:/users/{userId}/";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
