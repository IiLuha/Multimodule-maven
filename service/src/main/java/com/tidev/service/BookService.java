package com.tidev.service;

import com.tidev.database.dao.repositories.AuthorRepository;
import com.tidev.database.dao.repositories.BookRepository;
import com.tidev.database.dao.repositories.FilterBookRepositoryImpl;
import com.tidev.database.entity.Author;
import com.tidev.database.entity.Book;
import com.tidev.dto.AuthorFilter;
import com.tidev.dto.BookCreateEditDto;
import com.tidev.dto.BookFilter;
import com.tidev.dto.BookReadDto;
import com.tidev.mapper.BookCreateEditMapper;
import com.tidev.mapper.BookReadMapper;
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
public class BookService {

    private final BookReadMapper bookReadMapper;
    private final BookCreateEditMapper bookCreateEditMapper;
    private final BookRepository bookRepository;
    private final ImageService imageService;
    private final AuthorRepository authorRepository;

    public Page<BookReadDto> findAll(BookFilter filter, Pageable pageable) {
        Predicate predicate = FilterBookRepositoryImpl.buildPredicate(filter);
        return bookRepository.findAll(predicate, pageable)
                .map(bookReadMapper::map);
    }

    public List<BookReadDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookReadMapper::map)
                .toList();
    }

    public Optional<BookReadDto> findById(Long id) {
        return bookRepository.findById(id)
                .map(bookReadMapper::map);
    }

    public Optional<byte[]> findImage(Long id) {
        return bookRepository.findById(id)
                .map(Book::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::download);
    }

    @Transactional
    public BookReadDto create(BookCreateEditDto bookDto, List<Author> authors) {
        return Optional.of(bookDto)
                .map(object -> {
                    uploadImage(bookDto.getImage());
                    return bookCreateEditMapper.map(object);
                })
                .map(book -> {
                    authors.forEach(book::addAuthor);
                    authorRepository.saveAll(authors);//authorRepository.addBook(author.getId(), author.getBooks()));
                    return book;
                })
                .map(bookRepository::save)
                .map(bookReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public BookReadDto create(BookCreateEditDto bookDto) {
        return Optional.of(bookDto)
                .map(object -> {
                    uploadImage(bookDto.getImage());
                    return bookCreateEditMapper.map(object);
                })
                .map(bookRepository::save)
                .map(bookReadMapper::map)
                .orElseThrow();
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (image != null) {
            if (!image.isEmpty()) {
                imageService.upload(image.getOriginalFilename(), image.getInputStream());
            }
        }
    }

    @Transactional
    public Optional<BookReadDto> update(Long id, BookCreateEditDto bookDto) {
        return bookRepository.findById(id)
                .map(book -> {
                    uploadImage(bookDto.getImage());
                    return bookCreateEditMapper.map(bookDto, book);
                })
                .map(bookRepository::saveAndFlush)
                .map(bookReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return bookRepository.findById(id)
                .map(entity -> {
                    bookRepository.delete(entity);
                    bookRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<BookReadDto> addAuthor(Long id, AuthorFilter filter) {
        Optional<Author> maybeAuthor = authorRepository.findByFilter(filter);
        return maybeAuthor.flatMap(author ->
                bookRepository.findById(id)
                .map(book -> {
                    book.addAuthor(author);
                    authorRepository.save(author);
                    bookRepository.saveAndFlush(book);
                    return book;
                })
                .map(bookReadMapper::map));
    }
}
