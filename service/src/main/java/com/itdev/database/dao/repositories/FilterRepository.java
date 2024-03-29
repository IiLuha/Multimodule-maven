package com.itdev.database.dao.repositories;

import java.util.Optional;

public interface FilterRepository<F, E> {

    Optional<E> findByFilter(F filter);
}
