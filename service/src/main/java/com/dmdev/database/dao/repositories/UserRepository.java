package com.dmdev.database.dao.repositories;

import com.dmdev.database.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User>, FilterUserRepository {

    @Override
    @EntityGraph(attributePaths = {"userDetails", "userAddress"})
    List<User> findAll();

    @EntityGraph(attributePaths = {"userDetails", "userAddress"})
    @Query("select u from User u " +
            "where u.userDetails.firstname = :firstname")
    List<User> findAllByFirstname( String firstname);

    Optional<User> findTopBy();

    Optional<User> findByEmail(String email);
}
