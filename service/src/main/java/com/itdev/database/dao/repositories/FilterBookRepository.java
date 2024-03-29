package com.itdev.database.dao.repositories;

import com.itdev.database.entity.Book;
import com.itdev.dto.BookFilter;

public interface FilterBookRepository extends FilterRepository<BookFilter, Book> {
}
