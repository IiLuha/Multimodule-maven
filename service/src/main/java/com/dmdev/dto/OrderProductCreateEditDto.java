package com.dmdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

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
