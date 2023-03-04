package com.tidev.dto;

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
public class BookCreateEditDto {

    @Size(max = 128)
    @NotBlank
    String name;

    @Size(max = 255)
    String description;

    @NotNull
    BigDecimal price;

    @NotNull
    @Min(0)
    Short quantity;

    @NotNull
    Short issueYear;

    List<Integer> authorIds;

    MultipartFile image;
}
