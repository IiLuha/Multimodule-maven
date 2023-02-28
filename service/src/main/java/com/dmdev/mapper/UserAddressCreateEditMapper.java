package com.dmdev.mapper;

import com.dmdev.database.dao.repositories.UserRepository;
import com.dmdev.database.entity.User;
import com.dmdev.database.entity.UserAddress;
import com.dmdev.dto.UserAddressCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAddressCreateEditMapper implements Mapper<UserAddressCreateEditDto, UserAddress> {

    private final UserRepository userRepository;

    @Override
    public UserAddress map(UserAddressCreateEditDto fromObject, UserAddress toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public UserAddress map(UserAddressCreateEditDto object) {
        UserAddress userAddress = new UserAddress();
        copy(object, userAddress);
        return userAddress;
    }
    private void copy(UserAddressCreateEditDto object, UserAddress userAddress) {
        Optional<User> maybeUser = userRepository.findById(object.getUserId());
        if (maybeUser.isPresent()) {
            userAddress.setUser(maybeUser.get());
            userAddress.setRegion(object.getRegion());
            userAddress.setDistrict(object.getDistrict());
            userAddress.setPopulationCenter(object.getPopulationCenter());
            userAddress.setStreet(object.getStreet());
            userAddress.setHouse(object.getHouse());
            userAddress.setIsPrivate(object.getIsPrivate());
            userAddress.setFrontDoor(object.getFrontDoor());
            userAddress.setFlat(object.getFlat());
        }
    }
}
