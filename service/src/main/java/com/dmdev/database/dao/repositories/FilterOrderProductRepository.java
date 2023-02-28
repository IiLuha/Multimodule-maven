package com.dmdev.database.dao.repositories;

import com.dmdev.database.entity.OrderProduct;
import com.dmdev.dto.OrderProductFilter;

public interface FilterOrderProductRepository extends FilterRepository<OrderProductFilter, OrderProduct> {
}
