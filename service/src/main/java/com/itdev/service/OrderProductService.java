package com.itdev.service;

import com.itdev.database.dao.repositories.FilterOrderProductRepositoryImpl;
import com.itdev.database.dao.repositories.OrderProductRepository;
import com.itdev.database.entity.Order;
import com.itdev.database.entity.OrderProduct;
import com.itdev.dto.OrderProductCreateEditDto;
import com.itdev.dto.OrderProductFilter;
import com.itdev.dto.OrderProductReadDto;
import com.itdev.mapper.OrderProductCreateEditMapper;
import com.itdev.mapper.OrderProductReadMapper;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductService {

    private final OrderProductReadMapper orderProductReadMapper;
    private final OrderProductCreateEditMapper orderProductCreateEditMapper;
    private final OrderProductRepository orderProductRepository;

    public Page<OrderProductReadDto> findAll(OrderProductFilter filter, Pageable pageable) {
        Predicate predicate = FilterOrderProductRepositoryImpl.buildPredicate(filter);
        return orderProductRepository.findAll(predicate, pageable)
                .map(orderProductReadMapper::map);
    }

    public List<OrderProductReadDto> findAll() {
        return orderProductRepository.findAll().stream()
                .map(orderProductReadMapper::map)
                .toList();
    }

    public Optional<OrderProductReadDto> findById(Long id) {
        return orderProductRepository.findById(id)
                .map(orderProductReadMapper::map);
    }

    @Transactional
    public OrderProductReadDto create(OrderProductCreateEditDto orderProductDto) {
        return Optional.of(orderProductDto)
                .map(orderProductCreateEditMapper::map)
                .map(orderProductRepository::save)
                .map(orderProductReadMapper::map)
                .orElseThrow();
    }


    @Transactional
    public Optional<OrderProductReadDto> update(Long id, OrderProductCreateEditDto orderProductDto) {
        return orderProductRepository.findById(id)
                .map(orderProduct -> orderProductCreateEditMapper.map(orderProductDto, orderProduct))
                .map(OrderProduct::updateTotalPrice)
                .map(orderProduct -> {
                    orderProduct.getOrder().updatePrice();
                    return orderProduct;
                })
                .map(orderProductRepository::saveAndFlush)
                .map(orderProductReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return orderProductRepository.findById(id)
                .map(entity -> {
                    Order order = entity.getOrder();
                    orderProductRepository.delete(entity);
                    orderProductRepository.flush();
                    order.updatePrice();
                    return true;
                })
                .orElse(false);
    }
}
