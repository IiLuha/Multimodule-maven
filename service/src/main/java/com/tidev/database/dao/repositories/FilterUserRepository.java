package com.tidev.database.dao.repositories;

import com.tidev.database.entity.User;
import com.tidev.dto.UserFilter;

public interface FilterUserRepository extends FilterRepository<UserFilter, User> {
}
