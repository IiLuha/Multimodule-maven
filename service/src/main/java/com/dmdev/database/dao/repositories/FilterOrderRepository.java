package com.dmdev.database.dao.repositories;

import com.dmdev.database.entity.Order;
import com.dmdev.dto.OrderFilter;

public interface FilterOrderRepository extends FilterRepository<OrderFilter, Order> {
}
