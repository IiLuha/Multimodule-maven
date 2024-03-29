package com.itdev.database.dao.repositories;

import com.itdev.database.entity.Author;
import com.itdev.database.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author, Integer>, QuerydslPredicateExecutor<Author>, FilterAuthorRepository {
    @Transactional
    @Modifying
    @Query("""
            update Author a 
            set a.books = ?2
            where a.id = ?1""")
    void addBook(Integer id, List<Book> books);

    Optional<Author> findTopBy();

    @Query("""
            select a from Author a 
            where a.id in (:ids)""")
    List<Author> findAllByIds(List<Integer> ids);
}
