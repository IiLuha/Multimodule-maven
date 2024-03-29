package com.itdev.database.dao.repositories;

import com.itdev.database.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>, QuerydslPredicateExecutor<UserDetails> {

    Optional<UserDetails> findByUserId (Integer userId);
}
