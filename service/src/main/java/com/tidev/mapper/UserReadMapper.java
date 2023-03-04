package com.tidev.mapper;

import com.tidev.database.entity.User;
import com.tidev.dto.UserAddressReadDto;
import com.tidev.dto.UserDetailsReadDto;
import com.tidev.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final UserDetailsReadMapper userDetailsReadMapper;
    private final UserAddressReadMapper userAddressReadMapper;

    @Override
    public UserReadDto map(User object) {
        UserDetailsReadDto userDetails = Optional.ofNullable(object.getUserDetails()).map(userDetailsReadMapper::map).orElse(null);
        UserAddressReadDto userAddress = Optional.ofNullable(object.getUserAddress()).map(userAddressReadMapper::map).orElse(null);
        return new UserReadDto(
                object.getId(),
                object.getEmail(),
                object.getPassword(),
                object.getRole(),
                object.getImage(),
                userDetails,
                userAddress
        );
    }
}
