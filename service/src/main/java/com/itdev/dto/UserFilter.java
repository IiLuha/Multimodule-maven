package com.itdev.dto;

import com.itdev.database.entity.fields.Role;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record UserFilter(String email, String password, Role role,
                         String firstname, String lastname, String patronymic,
                         String phone) {
    @Builder public UserFilter {}
}
