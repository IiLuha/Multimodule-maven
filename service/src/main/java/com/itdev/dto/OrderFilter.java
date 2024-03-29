package com.itdev.dto;

import com.itdev.database.entity.fields.Role;
import com.itdev.database.entity.fields.Status;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@FieldNameConstants
public record OrderFilter(
        Long id,

        @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime afterCreatedAt,

        @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime beforeCreatedAt,

        @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime afterEndAt,

        @DateTimeFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime beforeEndAt,
        Status[] statuses,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String email,
        Role role,
        Integer clientId,
        String firstname,
        String lastname,
        String patronymic,
        String phone,
        String name,
        String description,
        Short quantity,
        Short issueYear,
        Short minIssueYear,
        Short maxIssueYear,
        String firstnameOfAuthor,
        String lastnameOfAuthor,
        String patronymicOfAuthor,

        @DateTimeFormat(pattern = dateFormat)
        LocalDate birthday,

        @DateTimeFormat(pattern = dateFormat)
        LocalDate minBirthday,

        @DateTimeFormat(pattern = dateFormat)
        LocalDate maxBirthday) {
        public static final String dateFormat = "yyyy-MM-dd";
}
