package com.itdev.database.dao.repositories;

import com.itdev.database.dao.predicates.QPredicate;
import com.itdev.database.entity.QAuthor;
import com.itdev.database.entity.Author;
import com.itdev.dto.AuthorFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class FilterAuthorRepositoryImpl implements FilterAuthorRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Author> findByFilter(AuthorFilter filter) {
        return Optional.ofNullable(filterQuery(filter).fetchFirst());
    }

    private JPAQuery<Author> filterQuery(AuthorFilter filter) {
        return new JPAQuery<Author>(entityManager)
                .select(QAuthor.author)
                .from(QAuthor.author)
                .where(buildPredicate(filter));
    }

    public static Predicate buildPredicate(AuthorFilter filter) {
        return QPredicate.builder()
                .add(filter.firstname(), QAuthor.author.firstname::containsIgnoreCase)
                .add(filter.lastname(), QAuthor.author.lastname::containsIgnoreCase)
                .add(filter.patronymic(), QAuthor.author.patronymic::containsIgnoreCase)
                .add(filter.birthday(), QAuthor.author.birthday::eq)
                .add(filter.minBirthday(), QAuthor.author.birthday::after)
                .add(filter.maxBirthday(), QAuthor.author.birthday::before)
                .buildAnd();
    }
}
