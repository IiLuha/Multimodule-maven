package com.dmdev.mapper;

import com.dmdev.database.entity.Book;
import com.dmdev.dto.BookReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReadMapper implements Mapper<Book, BookReadDto>{

    private final AuthorReadMapper authorReadMapper;

    @Override
    public BookReadDto map(Book object) {
        return new BookReadDto(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getPrice(),
                object.getQuantity(),
                object.getIssueYear(),
                object.getAuthors().stream()
                        .map(authorReadMapper::map)
                        .toList(),
                object.getImage()
        );
    }
}
