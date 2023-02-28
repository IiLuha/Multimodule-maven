package com.dmdev.dto;

import com.dmdev.database.entity.fields.Role;
import lombok.Value;
import lombok.experimental.FieldNameConstants;


@Value
@FieldNameConstants
public class UserReadDto {

    Integer id;
    String email;
    String password;
    Role role;
    String imagePath;
    UserDetailsReadDto userDetails;
    UserAddressReadDto userAddress;

    public String fullName() {
        return this.userDetails.getFirstname() + " " + this.userDetails.getLastname();
    }
}
