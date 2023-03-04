package com.tidev.mapper;

import com.tidev.database.entity.Author;
import com.tidev.dto.AuthorCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorCreateEditMapper implements Mapper<AuthorCreateEditDto, Author> {

    @Override
    public Author map(AuthorCreateEditDto fromObject, Author toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Author map(AuthorCreateEditDto object) {
        Author author = new Author();
        copy(object, author);
        return author;
    }

    private void copy(AuthorCreateEditDto object, Author author) {
        author.setFirstname(object.getFirstname());
        author.setLastname(object.getLastname());
        author.setPatronymic(object.getPatronymic());
        author.setBirthday(object.getBirthday());
    }
}
