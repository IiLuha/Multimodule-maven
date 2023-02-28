package com.dmdev.database.dao.repositories;

import com.dmdev.database.dao.predicates.QPredicate;
import com.dmdev.database.entity.Order;
import com.dmdev.database.entity.QAuthor;
import com.dmdev.database.entity.QBook;
import com.dmdev.database.entity.QOrder;
import com.dmdev.database.entity.QOrderProduct;
import com.dmdev.database.entity.QUser;
import com.dmdev.database.entity.fields.Status;
import com.dmdev.dto.OrderFilter;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FilterOrderRepositoryImpl implements FilterOrderRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Order> findByFilter(OrderFilter filter) {
        return Optional.ofNullable(filterQuery(filter).fetchFirst());
    }

    private JPAQuery<Order> filterQuery(OrderFilter filter) {
        return new JPAQuery<Order>(entityManager)
                .select(QOrder.order)
                .from(QOrder.order)
                .join(QOrder.order.orderProducts, QOrderProduct.orderProduct)
                .join(QOrderProduct.orderProduct.book, QBook.book)
                .join(QBook.book.authors, QAuthor.author)
                .join(QOrder.order.user, QUser.user)
                .where(buildPredicate(filter));
    }

    public static Predicate buildPredicate(OrderFilter filter) {
        QPredicate and = QPredicate.builder();
        QPredicate or  = QPredicate.builder();
        if (filter.clientId() != null){
            and.add(filter.clientId(), QOrder.order.user.id::eq);
        } else {
            and
                .add(filter.email(),                QOrder.order.user.email::containsIgnoreCase)
                .add(filter.role(),                 QOrder.order.user.role::eq)
                .add(filter.firstname(),            QOrder.order.user.userDetails.firstname::containsIgnoreCase)
                .add(filter.lastname(),             QOrder.order.user.userDetails.lastname::containsIgnoreCase)
                .add(filter.patronymic(),           QOrder.order.user.userDetails.patronymic::containsIgnoreCase)
                .add(filter.phone(),                QOrder.order.user.userDetails.phone::containsIgnoreCase);
        }
        and
                .add(filter.id(),                   QOrder.order.id::eq)
                .add(filter.afterCreatedAt(),       QOrder.order.createdAt::after)
                .add(filter.beforeCreatedAt(),      QOrder.order.createdAt::before)
                .add(filter.afterEndAt(),           QOrder.order.endAt::after)
                .add(filter.beforeEndAt(),          QOrder.order.endAt::before)
                .add(filter.minPrice(),             QOrder.order.price::goe)
                .add(filter.maxPrice(),             QOrder.order.price::loe)
                .add(filter.name(),                 QOrder.order.orderProducts.any().book.name::containsIgnoreCase)
                .add(filter.description(),          QOrder.order.orderProducts.any().book.description::containsIgnoreCase)
                .add(filter.quantity(),             QOrder.order.orderProducts.any().book.quantity::goe)
                .add(filter.issueYear(),            QOrder.order.orderProducts.any().book.issueYear::eq)
                .add(filter.minIssueYear(),         QOrder.order.orderProducts.any().book.issueYear::goe)
                .add(filter.maxIssueYear(),         QOrder.order.orderProducts.any().book.issueYear::loe)
                .add(filter.firstnameOfAuthor(),    QOrder.order.orderProducts.any().book.authors.any().firstname::containsIgnoreCase)
                .add(filter.lastnameOfAuthor(),     QOrder.order.orderProducts.any().book.authors.any().lastname::containsIgnoreCase)
                .add(filter.patronymicOfAuthor(),   QOrder.order.orderProducts.any().book.authors.any().patronymic::containsIgnoreCase)
                .add(filter.birthday(),             QOrder.order.orderProducts.any().book.authors.any().birthday::eq)
                .add(filter.minBirthday(),          QOrder.order.orderProducts.any().book.authors.any().birthday::after)
                .add(filter.maxBirthday(),          QOrder.order.orderProducts.any().book.authors.any().birthday::before);
        if (filter.statuses() != null){
            for (Status status :
                    filter.statuses()) {
                or.add(status, QOrder.order.status::eq);
            }
        }
        Predicate buildOr  = or.buildOr();
        Predicate buildAnd = and.buildAnd();
        Predicate[] predicates = {buildOr, buildAnd};
        return ExpressionUtils.allOf(predicates);
    }
}
