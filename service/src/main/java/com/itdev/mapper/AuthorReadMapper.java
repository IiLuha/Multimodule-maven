package com.itdev.mapper;

import com.itdev.database.entity.Author;
import com.itdev.database.entity.Book;
import com.itdev.dto.AuthorReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorReadMapper implements Mapper<Author, AuthorReadDto> {

    @Override
    public AuthorReadDto map(Author object) {
        return new AuthorReadDto(
                object.getId(),
                object.getFirstname(),
                object.getLastname(),
                object.getPatronymic(),
                object.getBirthday(),
                object.getBooks().stream()
                        .map(Book::getId)
                        .toList(),
                object.getImage()
        );
    }
}
