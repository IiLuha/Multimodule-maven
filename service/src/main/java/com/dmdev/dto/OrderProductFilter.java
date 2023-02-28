package com.dmdev.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@FieldNameConstants
public record OrderProductFilter(

        Long id,
        Integer quantity,
        BigDecimal minTotalPrice,
        BigDecimal maxTotalPrice,
        String name,
        String description,
        BigDecimal minPrice,
        BigDecimal maxPrice,
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
        public static final String dateFormat = "yyyy-MM-dd";
}
