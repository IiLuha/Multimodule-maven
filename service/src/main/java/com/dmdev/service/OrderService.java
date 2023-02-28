package com.dmdev.service;

import com.dmdev.database.dao.repositories.AuthorRepository;
import com.dmdev.database.dao.repositories.OrderProductRepository;
import com.dmdev.database.dao.repositories.OrderRepository;
import com.dmdev.database.dao.repositories.FilterOrderRepositoryImpl;
import com.dmdev.database.entity.Author;
import com.dmdev.database.entity.OrderProduct;
import com.dmdev.database.entity.fields.Status;
import com.dmdev.dto.AuthorFilter;
import com.dmdev.dto.OrderCreateEditDto;
import com.dmdev.dto.OrderFilter;
import com.dmdev.dto.OrderProductCreateEditDto;
import com.dmdev.dto.OrderReadDto;
import com.dmdev.mapper.OrderCreateEditMapper;
import com.dmdev.mapper.OrderProductCreateEditMapper;
import com.dmdev.mapper.OrderReadMapper;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderReadMapper orderReadMapper;
    private final OrderCreateEditMapper orderCreateEditMapper;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCreateEditMapper orderProductCreateEditMapper;

    public Page<OrderReadDto> findAll(OrderFilter filter, Pageable pageable) {
        Predicate predicate = FilterOrderRepositoryImpl.buildPredicate(filter);
        return orderRepository.findAll(predicate, pageable)
                .map(orderReadMapper::map);
    }

    public List<OrderReadDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderReadMapper::map)
                .toList();
    }

    public Optional<OrderReadDto> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderReadMapper::map);
    }

    @Transactional
    public OrderReadDto create(Integer clientId) {
        OrderCreateEditDto orderDto = new OrderCreateEditDto(
                LocalDateTime.now(),
                null,
                Status.OPEN,
                BigDecimal.valueOf(0),
                clientId,
                new ArrayList<OrderProduct>()
        );
        return Optional.of(orderDto)
                .map(orderCreateEditMapper::map)
                .map(orderRepository::save)
                .map(orderReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public OrderReadDto create(OrderCreateEditDto orderDto) {
        return Optional.of(orderDto)
                .map(orderCreateEditMapper::map)
                .map(orderRepository::save)
                .map(orderReadMapper::map)
                .orElseThrow();
    }


    @Transactional
    public Optional<OrderReadDto> update(Long id, OrderCreateEditDto orderDto) {
        return orderRepository.findById(id)
                .map(order -> orderCreateEditMapper.map(orderDto, order))
                .map(orderRepository::saveAndFlush)
                .map(orderReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return orderRepository.findById(id)
                .map(entity -> {
                    orderRepository.delete(entity);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<OrderReadDto> addOrderProduct(OrderProductCreateEditDto orderProductDto, BigDecimal bookPrice) {
        OrderProduct product = orderProductCreateEditMapper.map(orderProductDto);
        product.setTotalPrice(BigDecimal.valueOf(orderProductDto.getQuantity()).multiply(bookPrice));
        return Optional.of(orderProductRepository.save(product))
                .flatMap(orderProduct ->
                    Optional.of(orderProduct.getOrder())
                    .map(order -> {
                        order.addOrderProduct(orderProduct);
                        order.updatePrice();
                        orderRepository.saveAndFlush(order);
                        return order;
                    })
                    .map(orderReadMapper::map)
                );
    }
}
