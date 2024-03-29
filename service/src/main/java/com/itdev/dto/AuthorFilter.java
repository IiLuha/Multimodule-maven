package com.itdev.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@FieldNameConstants
public record AuthorFilter(
        String firstname,
        String lastname,
        String patronymic,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthday,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate minBirthday,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate maxBirthday) {
}
