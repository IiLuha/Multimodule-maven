package com.tidev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;

@Value
@FieldNameConstants
public class AuthorReadDto {

    Integer id;
    String firstname;
    String lastname;
    String patronymic;
    LocalDate birthday;
    List<Long> bookIds;
    String imagePath;
}
