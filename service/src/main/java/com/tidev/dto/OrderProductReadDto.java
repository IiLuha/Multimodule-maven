package com.tidev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Value
@FieldNameConstants
public class OrderProductReadDto {

    Long id;
    Long orderId;
    BookReadDto book;
    Integer quantity;
    BigDecimal totalPrice;
}
