package com.tidev.dto;

import com.tidev.database.entity.OrderProduct;
import com.tidev.database.entity.fields.Status;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@FieldNameConstants
public class OrderCreateEditDto {

    @NotNull
    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime createdAt;

    @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime endAt;

    @NotNull
    Status status;

    @NotNull
    @Min(0)
    BigDecimal price;

    @NotNull
    Integer clientId;

    List<OrderProduct> orderProducts;
}
