package com.dmdev.database.dao.repositories;

import com.dmdev.database.entity.User;
import com.dmdev.dto.UserFilter;

public interface FilterUserRepository extends FilterRepository<UserFilter, User> {
}
