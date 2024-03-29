package com.itdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.List;

@Value
@FieldNameConstants
public class BookReadDto {

    Long id;
    String name;
    String description;
    BigDecimal price;
    Short quantity;
    Short issueYear;
    List<AuthorReadDto> authors;
    String imagePath;
}
