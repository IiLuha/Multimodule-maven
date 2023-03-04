package com.tidev.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@FieldNameConstants
public class AuthorCreateEditDto {

    @Size(max = 128)
    @NotBlank
    String firstname;

    @Size(max = 128)
    @NotBlank
    String lastname;

    @Size(max = 128)
    String patronymic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    MultipartFile image;
}
