package com.tidev.database.dao.repositories;

import com.tidev.database.dao.predicates.QPredicate;
import com.tidev.database.entity.Book;
import com.tidev.database.entity.QAuthor;
import com.tidev.database.entity.QBook;
import com.tidev.dto.BookFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class FilterBookRepositoryImpl implements FilterBookRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Book> findByFilter(BookFilter filter) {
        return Optional.ofNullable(filterQuery(filter).fetchFirst());
    }

    private JPAQuery<Book> filterQuery(BookFilter filter) {
        return new JPAQuery<Book>(entityManager)
                .select(QBook.book)
                .from(QBook.book)
                .join(QBook.book.authors, QAuthor.author)
                .where(buildPredicate(filter));
    }

    public static Predicate buildPredicate(BookFilter filter) {
        return QPredicate.builder()
                .add(filter.name(),         QBook.book.name::containsIgnoreCase)
                .add(filter.description(),  QBook.book.description::containsIgnoreCase)
                .add(filter.minPrice(),     QBook.book.price::goe)
                .add(filter.maxPrice(),     QBook.book.price::loe)
                .add(filter.quantity(),     QBook.book.quantity::goe)
                .add(filter.issueYear(),    QBook.book.issueYear::eq)
                .add(filter.minIssueYear(), QBook.book.issueYear::goe)
                .add(filter.maxIssueYear(), QBook.book.issueYear::loe)
                .add(filter.firstname(),    QBook.book.authors.any().firstname::containsIgnoreCase)
                .add(filter.lastname(),     QBook.book.authors.any().lastname::containsIgnoreCase)
                .add(filter.patronymic(),   QBook.book.authors.any().patronymic::containsIgnoreCase)
                .add(filter.birthday(),     QBook.book.authors.any().birthday::eq)
                .add(filter.minBirthday(),  QBook.book.authors.any().birthday::after)
                .add(filter.maxBirthday(),  QBook.book.authors.any().birthday::before)
                .buildAnd();
    }
}
