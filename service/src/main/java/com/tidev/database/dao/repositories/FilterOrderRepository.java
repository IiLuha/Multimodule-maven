package com.tidev.database.dao.repositories;

import com.tidev.database.entity.Order;
import com.tidev.dto.OrderFilter;

public interface FilterOrderRepository extends FilterRepository<OrderFilter, Order> {
}
