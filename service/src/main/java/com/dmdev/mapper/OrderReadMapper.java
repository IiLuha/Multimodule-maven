package com.dmdev.mapper;

import com.dmdev.database.entity.Order;
import com.dmdev.dto.OrderReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderReadDto>{

    private final OrderProductReadMapper orderProductReadMapper;

    @Override
    public OrderReadDto map(Order object) {
        return new OrderReadDto(
                object.getId(),
                object.getCreatedAt(),
                object.getEndAt(),
                object.getStatus(),
                object.getPrice(),
                object.getUser().getId(),
                object.getOrderProducts().stream()
                        .map(orderProductReadMapper::map)
                        .toList()
        );
    }
}
