package com.dmdev.mapper;

import com.dmdev.database.dao.repositories.AuthorRepository;
import com.dmdev.database.entity.Book;
import com.dmdev.dto.BookCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookCreateEditMapper implements Mapper<BookCreateEditDto, Book> {

    private final AuthorRepository authorRepository;

    @Override
    public Book map(BookCreateEditDto fromObject, Book toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Book map(BookCreateEditDto object) {
        Book book = new Book();
        copy(object, book);
        return book;
    }

    private void copy(BookCreateEditDto object, Book book) {
        book.setName(object.getName());
        book.setDescription(object.getDescription());
        book.setPrice(object.getPrice());
        book.setQuantity(object.getQuantity());
        book.setIssueYear(object.getIssueYear());
        if (object.getAuthorIds() != null) {
            if(!object.getAuthorIds().isEmpty()) {
                book.setAuthors(authorRepository.findAllByIds(object.getAuthorIds()));
            }
        }
    }
}
