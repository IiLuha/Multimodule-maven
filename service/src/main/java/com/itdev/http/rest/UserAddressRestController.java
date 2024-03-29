package com.itdev.http.rest;

import com.itdev.dto.UserAddressCreateEditDto;
import com.itdev.dto.UserAddressReadDto;
import com.itdev.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/userAddress")
@RequiredArgsConstructor
public class UserAddressRestController {

    private final UserAddressService userAddressService;

    @GetMapping("/{userId}")
    public UserAddressReadDto findByUserId(@PathVariable("userId") Integer userId) {
        return userAddressService.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAddressReadDto create(@Validated @RequestBody UserAddressCreateEditDto userAddress) {
        return userAddressService.create(userAddress);
    }

    @PutMapping("/{userId}")
    public UserAddressReadDto updateByUserId(@PathVariable("userId") Integer userId, @Validated @RequestBody UserAddressCreateEditDto userAddress) {
        return userAddressService.updateByUserId(userId, userAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUserId(@PathVariable("userId") Integer userId) {
        if (!userAddressService.deleteByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
