package com.tidev.database.dao.repositories;

import com.tidev.database.dao.predicates.QPredicate;
import com.tidev.database.entity.OrderProduct;
import com.tidev.database.entity.QAuthor;
import com.tidev.database.entity.QBook;
import com.tidev.database.entity.QOrderProduct;
import com.tidev.dto.OrderProductFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class FilterOrderProductRepositoryImpl implements FilterOrderProductRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<OrderProduct> findByFilter(OrderProductFilter filter) {
        return Optional.ofNullable(filterQuery(filter).fetchFirst());
    }

    private JPAQuery<OrderProduct> filterQuery(OrderProductFilter filter) {
        return new JPAQuery<OrderProduct>(entityManager)
                .select(QOrderProduct.orderProduct)
                .from(QOrderProduct.orderProduct)
                .join(QOrderProduct.orderProduct.book, QBook.book)
                .join(QBook.book.authors, QAuthor.author)
                .where(buildPredicate(filter));
    }

    public static Predicate buildPredicate(OrderProductFilter filter) {
        return QPredicate.builder()
                .add(filter.id(),               QOrderProduct.orderProduct.id::eq)
                .add(filter.quantity(),         QOrderProduct.orderProduct.book.quantity::goe)
                .add(filter.minTotalPrice(),    QOrderProduct.orderProduct.totalPrice::goe)
                .add(filter.maxTotalPrice(),    QOrderProduct.orderProduct.totalPrice::loe)
                .add(filter.name(),             QOrderProduct.orderProduct.book.name::containsIgnoreCase)
                .add(filter.description(),      QOrderProduct.orderProduct.book.description::containsIgnoreCase)
                .add(filter.minPrice(),         QOrderProduct.orderProduct.totalPrice::goe)
                .add(filter.maxPrice(),         QOrderProduct.orderProduct.totalPrice::loe)
                .add(filter.issueYear(),        QOrderProduct.orderProduct.book.issueYear::eq)
                .add(filter.minIssueYear(),     QOrderProduct.orderProduct.book.issueYear::goe)
                .add(filter.maxIssueYear(),     QOrderProduct.orderProduct.book.issueYear::loe)
                .add(filter.firstname(),        QOrderProduct.orderProduct.book.authors.any().firstname::containsIgnoreCase)
                .add(filter.lastname(),         QOrderProduct.orderProduct.book.authors.any().lastname::containsIgnoreCase)
                .add(filter.patronymic(),       QOrderProduct.orderProduct.book.authors.any().patronymic::containsIgnoreCase)
                .add(filter.birthday(),         QOrderProduct.orderProduct.book.authors.any().birthday::eq)
                .add(filter.minBirthday(),      QOrderProduct.orderProduct.book.authors.any().birthday::after)
                .add(filter.maxBirthday(),      QOrderProduct.orderProduct.book.authors.any().birthday::before)
                .buildAnd();
    }
}
