package com.tidev.database.dao.repositories;

import com.tidev.database.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book>, FilterBookRepository {

    @Query("select b from Author a " +
            "join a.books b " +
            "where a.id = :id")
    List<Book> findAllByAuthorId( Integer id);

    List<Book> findAllByIssueYear(Short issueYear);
}
