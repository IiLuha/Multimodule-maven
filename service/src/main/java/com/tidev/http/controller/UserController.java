package com.tidev.http.controller;

import com.tidev.database.entity.fields.Role;
import com.tidev.dto.PageResponse;
import com.tidev.dto.UserCreateEditDto;
import com.tidev.dto.UserFilter;
import com.tidev.dto.UserReadDto;
import com.tidev.service.UserService;
import com.tidev.validation.group.CreateAction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.groups.Default;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
        Page<UserReadDto> page = userService.findAll(filter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("roles", Role.values());
        model.addAttribute("filter", filter);
        return "user/users";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findById(@PathVariable("id") Integer id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    model.addAttribute("userDetails", user.getUserDetails());
                    model.addAttribute("userAddress", user.getUserAddress());
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public String create(@Validated({Default.class, CreateAction.class}) UserCreateEditDto user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }
        userService.create(user);
        return "redirect:/login";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, @Validated UserCreateEditDto user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<UserReadDto> update = userService.update(id, user);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }
        return update
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        if (userService.delete(id)) {
            return "redirect:/users";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/registration")
    public String registration(Model model, UserCreateEditDto user) {
        model.addAttribute("user", user);
        model.addAttribute("role", Role.USER);
        return "/user/registration";
    }
}
