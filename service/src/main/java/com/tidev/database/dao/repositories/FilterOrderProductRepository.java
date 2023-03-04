package com.tidev.database.dao.repositories;

import com.tidev.database.entity.OrderProduct;
import com.tidev.dto.OrderProductFilter;

public interface FilterOrderProductRepository extends FilterRepository<OrderProductFilter, OrderProduct> {
}
