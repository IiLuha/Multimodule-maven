package com.dmdev.database.dao.repositories;

import com.dmdev.database.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>, QuerydslPredicateExecutor<UserDetails> {

    Optional<UserDetails> findByUserId (Integer userId);
}
