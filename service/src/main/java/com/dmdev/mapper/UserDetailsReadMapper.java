package com.dmdev.mapper;

import com.dmdev.database.entity.UserDetails;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsReadMapper implements Mapper<UserDetails, UserDetailsReadDto> {


    @Override
    public UserDetailsReadDto map(UserDetails object) {
        return new UserDetailsReadDto(
                object.getId(),
                object.getUser().getId(),
                object.getFirstname(),
                object.getLastname(),
                object.getPatronymic(),
                object.getPhone()
        );
    }
}
