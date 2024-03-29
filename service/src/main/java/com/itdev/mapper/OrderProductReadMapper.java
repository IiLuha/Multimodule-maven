package com.itdev.mapper;

import com.itdev.database.entity.OrderProduct;
import com.itdev.dto.OrderProductReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProductReadMapper implements Mapper<OrderProduct, OrderProductReadDto> {

    private final BookReadMapper bookReadMapper;

    @Override
    public OrderProductReadDto map(OrderProduct object) {
        return new OrderProductReadDto(
                object.getId(),
                object.getOrder().getId(),
                bookReadMapper.map(object.getBook()),
                object.getQuantity(),
                object.getTotalPrice()
        );
    }
}
