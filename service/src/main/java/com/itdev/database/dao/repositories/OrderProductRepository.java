package com.itdev.database.dao.repositories;

import com.itdev.database.entity.OrderProduct;
import com.itdev.database.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>, QuerydslPredicateExecutor<OrderProduct>, FilterOrderProductRepository {

    @Modifying
    @Query("delete from OrderProduct op " +
            "where op.order in :orders")
    void deleteAllByOrders( List<Order> orders);

    @Modifying
    @Query("delete from OrderProduct op " +
            "where op.order = :order")
    void deleteALlByOrder( Order order);

    @Query("select op from Order o " +
            "join o.orderProducts op " +
            "where not o in (:orders)")
    List<OrderProduct> findAllByNotOrders( List<Order> orders);

    @Query("""
            select a from OrderProduct a 
            where a.id in (:ids)""")
    List<OrderProduct> findAllByIds(List<Long> ids);
}
