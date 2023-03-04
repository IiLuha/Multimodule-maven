package com.tidev.database.dao.repositories;

import com.tidev.database.entity.Book;
import com.tidev.dto.BookFilter;

public interface FilterBookRepository extends FilterRepository<BookFilter, Book> {
}
