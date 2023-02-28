package com.dmdev.mapper;

import com.dmdev.database.dao.repositories.OrderProductRepository;
import com.dmdev.database.dao.repositories.UserRepository;
import com.dmdev.database.entity.Order;
import com.dmdev.database.entity.fields.Status;
import com.dmdev.dto.OrderCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;

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
