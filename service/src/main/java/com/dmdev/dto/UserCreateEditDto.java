package com.dmdev.dto;

import com.dmdev.database.entity.fields.Role;
import com.dmdev.validation.group.CreateAction;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    @Size(max = 128)
    @Email
    String email;


    @Size(max = 128)
    @NotBlank(groups = CreateAction.class)
    String rawPassword;

    @NotNull
    Role role;

    MultipartFile image;
}
