package com.dmdev.http.controller;

import com.dmdev.dto.AuthorCreateEditDto;
import com.dmdev.dto.AuthorFilter;
import com.dmdev.dto.AuthorReadDto;
import com.dmdev.dto.PageResponse;
import com.dmdev.service.AuthorService;
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
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("")
    public String findAll(Model model, AuthorFilter filter, Pageable pageable) {
        Page<AuthorReadDto> page = authorService.findAll(filter, pageable);
        model.addAttribute("authors", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "author/authors";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        return authorService.findById(id)
                .map(author -> {
                    model.addAttribute("author", author);
                    return "author/author";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/new")
    public String createPage() {
        return "author/new";
    }

    @PostMapping("/create")
    public String create(@Validated AuthorCreateEditDto author) {
        AuthorReadDto authorReadDto = authorService.create(author);
        return "redirect:/authors/" + authorReadDto.getId();
    }

    @PostMapping("/{id}/update")
    public String update(Model model, @PathVariable("id") Integer id, @Validated AuthorCreateEditDto author) {
        Optional<AuthorReadDto> update = authorService.update(id, author);
        return update
                .map(it -> "redirect:/authors/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        if (authorService.delete(id)) {
            return "redirect:/authors";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
