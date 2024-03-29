package com.itdev.database.dao.repositories;

import com.itdev.database.entity.User;
import com.itdev.dto.UserFilter;

public interface FilterUserRepository extends FilterRepository<UserFilter, User> {
}
