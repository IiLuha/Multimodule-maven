package com.tidev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@FieldNameConstants
public class OrderProductCreateEditDto {

    @NotNull
    Long orderId;

    @NotNull
    Long bookId;

    @NotNull
    @Min(0)
    Integer quantity;

    @NotNull
    @Min(0)
    BigDecimal totalPrice;
}
