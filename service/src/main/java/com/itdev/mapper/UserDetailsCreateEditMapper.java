package com.itdev.mapper;

import com.itdev.database.entity.User;
import com.itdev.database.entity.UserDetails;
import com.itdev.dto.UserDetailsCreateEditDto;
import com.itdev.database.dao.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsCreateEditMapper implements Mapper<UserDetailsCreateEditDto, UserDetails> {

    private final UserRepository userRepository;

    @Override
    public UserDetails map(UserDetailsCreateEditDto fromObject, UserDetails toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public UserDetails map(UserDetailsCreateEditDto object) {
        UserDetails userDetails = new UserDetails();
        copy(object, userDetails);
        return userDetails;
    }

    private void copy(UserDetailsCreateEditDto object, UserDetails userDetails) {
        Optional<User> maybeUser = userRepository.findById(object.getUserId());
        if (maybeUser.isPresent()) {
            maybeUser.get().addUserDetails(userDetails);
            userDetails.setFirstname(object.getFirstname());
            userDetails.setLastname(object.getLastname());
            userDetails.setPatronymic(object.getPatronymic());
            userDetails.setPhone(object.getPhone());
        }
    }
}
