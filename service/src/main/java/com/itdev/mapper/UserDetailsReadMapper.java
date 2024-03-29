package com.itdev.mapper;

import com.itdev.database.entity.UserDetails;
import com.itdev.dto.UserDetailsReadDto;
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
