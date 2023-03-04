package com.tidev.mapper;

import com.tidev.database.entity.OrderProduct;
import com.tidev.dto.OrderProductReadDto;
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
