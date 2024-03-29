package com.itdev.database.dao.repositories;

import com.itdev.database.entity.Order;
import com.itdev.database.entity.fields.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order>, FilterOrderRepository {

    List<Order> findAllByUserId(Integer clientId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Order o " +
            "set o.status = :status " +
            "where o.id in (:ids)")
    void updateStatusByIds( Status status, Long... ids);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllByCreatedAtBefore(LocalDateTime date);

    List<Order> findTop3By();
}
