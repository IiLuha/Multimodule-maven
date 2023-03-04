package com.tidev.service;

import com.tidev.database.dao.repositories.AuthorRepository;
import com.tidev.database.dao.repositories.FilterAuthorRepositoryImpl;
import com.tidev.database.entity.Author;
import com.tidev.dto.AuthorCreateEditDto;
import com.tidev.dto.AuthorFilter;
import com.tidev.dto.AuthorReadDto;
import com.tidev.mapper.AuthorCreateEditMapper;
import com.tidev.mapper.AuthorReadMapper;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorReadMapper authorReadMapper;
    private final AuthorCreateEditMapper authorCreateEditMapper;
    private final AuthorRepository authorRepository;
    private final ImageService imageService;

    public Page<AuthorReadDto> findAll(AuthorFilter filter, Pageable pageable) {
        Predicate predicate = FilterAuthorRepositoryImpl.buildPredicate(filter);
        return authorRepository.findAll(predicate, pageable)
                .map(authorReadMapper::map);
    }

    public List<AuthorReadDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorReadMapper::map)
                .toList();
    }

    public Optional<AuthorReadDto> findById(Integer id) {
        return authorRepository.findById(id)
                .map(authorReadMapper::map);
    }

    public Optional<byte[]> findImage(Integer id) {
        return authorRepository.findById(id)
                .map(Author::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::download);
    }

    @Transactional
    public AuthorReadDto create(AuthorCreateEditDto authorDto) {
        return Optional.of(authorDto)
                .map(object -> {
                    uploadImage(authorDto.getImage());
                    return authorCreateEditMapper.map(object);
                })
                .map(authorRepository::save)
                .map(authorReadMapper::map)
                .orElseThrow();
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional
    public Optional<AuthorReadDto> update(Integer id, AuthorCreateEditDto authorDto) {
        return authorRepository.findById(id)
                .map(author -> {
                    uploadImage(authorDto.getImage());
                    return authorCreateEditMapper.map(authorDto, author);
                })
                .map(authorRepository::saveAndFlush)
                .map(authorReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return authorRepository.findById(id)
                .map(entity -> {
                    authorRepository.delete(entity);
                    authorRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Optional<AuthorReadDto> findByFilter(AuthorFilter filter) {
        return authorRepository.findByFilter(filter)
                .map(authorReadMapper::map);
    }
}
