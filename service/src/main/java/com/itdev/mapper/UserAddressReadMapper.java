package com.itdev.mapper;

import com.itdev.database.entity.UserAddress;
import com.itdev.dto.UserAddressReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAddressReadMapper implements Mapper<UserAddress, UserAddressReadDto> {


    @Override
    public UserAddressReadDto map(UserAddress object) {
        return new UserAddressReadDto(
                object.getId(),
                object.getRegion(),
                object.getDistrict(),
                object.getPopulationCenter(),
                object.getStreet(),
                object.getHouse(),
                object.getIsPrivate(),
                object.getFrontDoor(),
                object.getFloor(),
                object.getFlat()
        );
    }
}
