package com.dmdev.service;

import com.dmdev.database.dao.repositories.UserAddressRepository;
import com.dmdev.dto.UserAddressCreateEditDto;
import com.dmdev.dto.UserAddressReadDto;
import com.dmdev.mapper.UserAddressCreateEditMapper;
import com.dmdev.mapper.UserAddressReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAddressService {

    private final UserAddressReadMapper userAddressReadMapper;
    private final UserAddressCreateEditMapper userAddressCreateEditMapper;
    private final UserAddressRepository userAddressRepository;

    public Optional<UserAddressReadDto> findByUserId(Integer userId) {
        return userAddressRepository.findByUserId(userId)
                .map(userAddressReadMapper::map);
    }

    @Transactional
    public UserAddressReadDto create(UserAddressCreateEditDto userAddressDto) {
        return Optional.of(userAddressDto)
                .map(userAddressCreateEditMapper::map)
                .map(userAddressRepository::save)
                .map(userAddressReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserAddressReadDto> updateByUserId(Integer userId, UserAddressCreateEditDto userAddressDto) {
        return userAddressRepository.findByUserId(userId)
                .map(userAddress -> userAddressCreateEditMapper.map(userAddressDto, userAddress))
                .map(userAddressRepository::saveAndFlush)
                .map(userAddressReadMapper::map);
    }

    @Transactional
    public boolean deleteByUserId(Integer userId) {
        return userAddressRepository.findByUserId(userId)
                .map(entity -> {
                    userAddressRepository.delete(entity);
                    userAddressRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
