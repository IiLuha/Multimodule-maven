package com.tidev.http.rest;

import com.tidev.dto.BookCreateEditDto;
import com.tidev.dto.BookFilter;
import com.tidev.dto.BookReadDto;
import com.tidev.dto.PageResponse;
import com.tidev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public PageResponse<BookReadDto> findAll(BookFilter filter, Pageable pageable) {
        return PageResponse.of(bookService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public BookReadDto findById(@PathVariable Long id) {
        return bookService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> findImage(@PathVariable("id") Long id) {
        return bookService.findImage(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(notFound()::build);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BookReadDto create(@Validated @RequestBody BookCreateEditDto book) {
        return bookService.create(book);
    }

    @PutMapping("/{id}")
    public BookReadDto update(@PathVariable Long id, @Validated @RequestBody BookCreateEditDto editDto) {
        return bookService.update(id, editDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!bookService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
