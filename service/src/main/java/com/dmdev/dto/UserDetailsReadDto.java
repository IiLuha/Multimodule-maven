package com.dmdev.dto;

import com.dmdev.database.entity.User;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserDetailsReadDto {

    Integer id;
    Integer userId;
    String firstname;
    String lastname;
    String patronymic;
    String phone;
}
