package com.dmdev.database.dao.repositories;

import com.dmdev.database.entity.Book;
import com.dmdev.dto.BookFilter;

public interface FilterBookRepository extends FilterRepository<BookFilter, Book> {
}
