package com.tidev.http.controller;

import com.tidev.database.dao.repositories.AuthorRepository;
import com.tidev.dto.AuthorFilter;
import com.tidev.dto.PageResponse;
import com.tidev.dto.BookCreateEditDto;
import com.tidev.dto.BookFilter;
import com.tidev.dto.BookReadDto;
import com.tidev.service.BookService;
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
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorRepository authorRepository;

    @GetMapping("")
    public String findAll(Model model, BookFilter filter, Pageable pageable) {
        Page<BookReadDto> page = bookService.findAll(filter, pageable);
        model.addAttribute("books", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "book/books";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return bookService.findById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "book/book";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/findAuthor")
    public String findAuthorToAdd(Model model, @PathVariable("id") Long id, AuthorFilter filter) {
        model.addAttribute("id", id);
        model.addAttribute("filter", filter);
        return "book/addAuthor";
    }

    @PostMapping("/{id}/addAuthor")
    public String addAuthor(@PathVariable("id") Long id, AuthorFilter filter) {
        return bookService.addAuthor(id, filter)
                .map(book -> "redirect:/books/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/create")
    public String create() {
        return "book/create";
    }

    @PostMapping("/create")
    public String create(@Validated BookCreateEditDto book) {
        BookReadDto bookReadDto = bookService.create(book);
        return "redirect:/books/" + bookReadDto.getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @Validated BookCreateEditDto book) {
        Optional<BookReadDto> update = bookService.update(id, book);
        return update
                .map(it -> "redirect:/books/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (bookService.delete(id)) {
            return "redirect:/books";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
