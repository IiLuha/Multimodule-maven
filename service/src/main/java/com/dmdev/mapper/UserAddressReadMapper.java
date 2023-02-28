package com.dmdev.mapper;

import com.dmdev.database.entity.User;
import com.dmdev.database.entity.UserAddress;
import com.dmdev.dto.UserAddressReadDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
