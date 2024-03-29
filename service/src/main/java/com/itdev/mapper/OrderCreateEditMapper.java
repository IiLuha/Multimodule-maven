package com.itdev.mapper;

import com.itdev.database.dao.repositories.UserRepository;
import com.itdev.database.entity.Order;
import com.itdev.dto.OrderCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private final UserRepository userRepository;

    @Override
    public Order map(OrderCreateEditDto fromObject, Order toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Order map(OrderCreateEditDto object) {
        Order order = new Order();
        copy(object, order);
        return order;
    }

    private void copy(OrderCreateEditDto object, Order order) {
        order.setCreatedAt(object.getCreatedAt());
        order.setEndAt(object.getEndAt());
        order.setStatus(object.getStatus());
        order.setPrice(object.getPrice());
        if (object.getClientId() != null) {
            order.setUser(userRepository.findById(object.getClientId()).orElseThrow());
        }
        if (object.getOrderProducts() != null) {
            order.addOrderProducts(object.getOrderProducts());
        }
    }
}
