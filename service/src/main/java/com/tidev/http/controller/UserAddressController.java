package com.tidev.http.controller;

import com.tidev.dto.UserAddressCreateEditDto;
import com.tidev.dto.UserAddressReadDto;
import com.tidev.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/userAddress")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    @GetMapping("/{userId}")
    public String findByUserId(@PathVariable("userId") Integer userId, Model model) {
        return userAddressService.findByUserId(userId)
                .map(userAddress -> {
                    model.addAttribute("userAddress", userAddress);
                    return "user/userAddress";
                })
                .orElseGet(() -> {
                    model.addAttribute("userId", userId);
                    return "user/userAddressCreate";
                });
    }

    @PostMapping("/create")
    public String create(@Validated UserAddressCreateEditDto userAddress) {
        UserAddressReadDto userAddressReadDto = userAddressService.create(userAddress);
        return "redirect:/users/" + userAddressReadDto.getUserId();
    }

    @PostMapping("/{userId}/update")
    public String updateByUserId(@PathVariable("userId") Integer userId, @Validated UserAddressCreateEditDto userAddress) {
        Optional<UserAddressReadDto> update = userAddressService.updateByUserId(userId, userAddress);
        return update
                .map(it -> "redirect:/users/{userId}/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{userId}/delete")
    public String deleteByUserId(@PathVariable("userId") Integer userId) {
        if (userAddressService.deleteByUserId(userId)) {
            return "redirect:/users/{userId}/";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
