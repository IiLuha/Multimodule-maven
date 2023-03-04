package com.tidev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserAddressCreateEditDto {

    @NotNull
    Integer userId;

    @Size(max = 128)
    @NotBlank
    String region;

    @Size(max = 128)
    @NotBlank
    String district;

    @Size(max = 128)
    @NotBlank
    String populationCenter;

    @Size(max = 128)
    @NotBlank
    String street;

    @Size(max = 128)
    @NotBlank
    String house;

    Boolean isPrivate;

    Integer frontDoor;

    Integer floor;

    Integer flat;
}
