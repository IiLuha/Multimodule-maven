package com.itdev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

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
