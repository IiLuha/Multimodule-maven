package com.tidev.mapper;

import com.tidev.database.entity.UserAddress;
import com.tidev.dto.UserAddressReadDto;
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
