package com.tidev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

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
