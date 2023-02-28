package com.dmdev.dto;

import com.dmdev.database.entity.fields.Role;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record UserFilter(
        String email,
        String password,
        Role role,
        String firstname,
        String lastname,
        String patronymic,
        String phone) {
}
