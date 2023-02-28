package com.dmdev.dto;

import com.dmdev.database.entity.fields.Status;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@FieldNameConstants
public class OrderReadDto {

    Long id;
    LocalDateTime createdAt;
    LocalDateTime endAt;
    Status status;
    BigDecimal price;
    Integer clientId;
    List<OrderProductReadDto> orderProducts;
}
