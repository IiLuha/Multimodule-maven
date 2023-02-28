package com.dmdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserAddressReadDto {

    Integer userId;
    String region;
    String district;
    String populationCenter;
    String street;
    String house;
    Boolean isPrivate;
    Integer frontDoor;
    Integer floor;
    Integer flat;
}
