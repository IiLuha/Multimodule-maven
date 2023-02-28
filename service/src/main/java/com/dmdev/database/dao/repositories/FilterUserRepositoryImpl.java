package com.dmdev.database.dao.repositories;

import com.dmdev.database.dao.predicates.QPredicate;
import com.dmdev.database.entity.QUser;
import com.dmdev.database.entity.User;
import com.dmdev.dto.UserFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<User> findByFilter(UserFilter filter) {
        return Optional.ofNullable(filterQuery(filter).fetchFirst());
    }

    private JPAQuery<User> filterQuery(UserFilter filter) {
        return new JPAQuery<User>(entityManager)
                .select(QUser.user)
                .from(QUser.user)
                .where(buildPredicate(filter));
    }

    public static Predicate buildPredicate(UserFilter filter) {
        return QPredicate.builder()
                .add(filter.email(), QUser.user.email::containsIgnoreCase)
                .add(filter.password(), QUser.user.password::containsIgnoreCase)
                .add(filter.role(), QUser.user.role::eq)
                .add(filter.firstname(), QUser.user.userDetails.firstname::containsIgnoreCase)
                .add(filter.lastname(), QUser.user.userDetails.lastname::containsIgnoreCase)
                .add(filter.patronymic(), QUser.user.userDetails.patronymic::containsIgnoreCase)
                .add(filter.phone(), QUser.user.userDetails.phone::containsIgnoreCase)
                .buildAnd();
    }
}
