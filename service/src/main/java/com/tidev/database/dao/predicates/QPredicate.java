package com.tidev.database.dao.predicates;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.querydsl.core.types.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {
    public final List<Predicate> predicates = new ArrayList<>();

    public static QPredicate builder() {
        return new QPredicate();
    }

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            if (object instanceof String) {
                if (!((String) object).isEmpty()) {
                    predicates.add(function.apply(object));
                }
            } else {
                predicates.add(function.apply(object));
            }
        }
        return this;
    }

    public Predicate buildAnd() {
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public Predicate buildOr() {
        return Optional.ofNullable(ExpressionUtils.anyOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }
}
