package com.itdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserDetailsCreateEditDto {

    @NotNull
    Integer userId;

    @Size(max = 128)
    @NotBlank
    String firstname;

    @Size(max = 128)
    @NotBlank
    String lastname;

    @Size(max = 128)
    String patronymic;

    @Size(max = 32)
    String phone;
}
