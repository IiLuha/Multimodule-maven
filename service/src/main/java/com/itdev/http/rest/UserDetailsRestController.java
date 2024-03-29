package com.itdev.http.rest;

import com.itdev.dto.UserDetailsCreateEditDto;
import com.itdev.dto.UserDetailsReadDto;
import com.itdev.service.UserDetailsService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/userDetails")
@RequiredArgsConstructor
public class UserDetailsRestController {

    private final UserDetailsService userDetailsService;

    @GetMapping("/{userId}")
    public UserDetailsReadDto findByUserId(@PathVariable("userId") Integer userId) {
        return userDetailsService.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public UserDetailsReadDto create(@Validated @RequestBody UserDetailsCreateEditDto userDetails) {
        return userDetailsService.create(userDetails);
    }

    @PutMapping("/{userId}")
    public UserDetailsReadDto updateByUserId(@PathVariable("userId") Integer userId, @Validated @RequestBody UserDetailsCreateEditDto userDetails) {
        return userDetailsService.updateByUserId(userId, userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{userId}")
    public void deleteByUserId(@PathVariable("userId") Integer userId) {
        if (!userDetailsService.deleteByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
