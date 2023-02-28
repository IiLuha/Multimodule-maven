package com.dmdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
