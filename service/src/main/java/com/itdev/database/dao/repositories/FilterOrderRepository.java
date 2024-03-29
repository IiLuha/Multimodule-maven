package com.itdev.database.dao.repositories;

import com.itdev.database.entity.Order;
import com.itdev.dto.OrderFilter;

public interface FilterOrderRepository extends FilterRepository<OrderFilter, Order> {
}
