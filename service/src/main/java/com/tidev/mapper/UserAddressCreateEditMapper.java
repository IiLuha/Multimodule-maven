package com.tidev.mapper;

import com.tidev.database.dao.repositories.UserRepository;
import com.tidev.database.entity.User;
import com.tidev.database.entity.UserAddress;
import com.tidev.dto.UserAddressCreateEditDto;
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
            userAddress.setId(object.getUserId());
            maybeUser.get().addUserAddress(userAddress);
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
