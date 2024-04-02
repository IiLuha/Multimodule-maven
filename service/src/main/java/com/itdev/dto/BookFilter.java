package com.itdev.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@FieldNameConstants
public record BookFilter(
        String name,
        String description,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Short quantity,
        Short issueYear,
        Short minIssueYear,
        Short maxIssueYear,
        String firstname,
        String lastname,
        String patronymic,

        @DateTimeFormat(pattern = dateFormat)
        LocalDate birthday,

        @DateTimeFormat(pattern = dateFormat)
        LocalDate minBirthday,

        @DateTimeFormat(pattern = dateFormat)
        LocalDate maxBirthday) {

        @Builder public BookFilter {}

        public static final String dateFormat = "yyyy-MM-dd";
}
