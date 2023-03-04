package com.tidev.http.rest;

import com.tidev.dto.AuthorCreateEditDto;
import com.tidev.dto.AuthorFilter;
import com.tidev.dto.AuthorReadDto;
import com.tidev.dto.PageResponse;
import com.tidev.service.AuthorService;
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
@RequestMapping("api/v1/authors")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping
    public PageResponse<AuthorReadDto> findAll(AuthorFilter filter, Pageable pageable) {
        return PageResponse.of(authorService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public AuthorReadDto findById(@PathVariable Integer id) {
        return authorService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> findImage(@PathVariable("id") Integer id) {
        return authorService.findImage(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(notFound()::build);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorReadDto create(@Validated @RequestBody AuthorCreateEditDto author) {
        return authorService.create(author);
    }

    @PutMapping("/{id}")
    public AuthorReadDto update(@PathVariable Integer id, @Validated @RequestBody AuthorCreateEditDto editDto) {
        return authorService.update(id, editDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!authorService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
