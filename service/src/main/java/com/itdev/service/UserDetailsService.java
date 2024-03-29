package com.itdev.service;

import com.itdev.database.dao.repositories.UserDetailsRepository;
import com.itdev.dto.UserDetailsCreateEditDto;
import com.itdev.dto.UserDetailsReadDto;
import com.itdev.mapper.UserDetailsCreateEditMapper;
import com.itdev.mapper.UserDetailsReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsService {

    private final UserDetailsReadMapper userDetailsReadMapper;
    private final UserDetailsCreateEditMapper userDetailsCreateEditMapper;
    private final UserDetailsRepository userDetailsRepository;

    public Optional<UserDetailsReadDto> findByUserId(Integer userId) {
        return userDetailsRepository.findByUserId(userId)
                .map(userDetailsReadMapper::map);
    }

    @Transactional
    public UserDetailsReadDto create(UserDetailsCreateEditDto userDetailsDto) {
        return Optional.of(userDetailsDto)
                .map(userDetailsCreateEditMapper::map)
                .map(userDetailsRepository::save)
                .map(userDetailsReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserDetailsReadDto> updateByUserId(Integer userId, UserDetailsCreateEditDto userDetailsDto) {
        return userDetailsRepository.findByUserId(userId)
                .map(userDetails -> userDetailsCreateEditMapper.map(userDetailsDto, userDetails))
                .map(userDetailsRepository::saveAndFlush)
                .map(userDetailsReadMapper::map);
    }

    @Transactional
    public boolean deleteByUserId(Integer userId) {
        return userDetailsRepository.findByUserId(userId)
                .map(entity -> {
                    userDetailsRepository.delete(entity);
                    userDetailsRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
