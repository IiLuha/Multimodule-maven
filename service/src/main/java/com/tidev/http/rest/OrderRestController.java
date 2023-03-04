package com.tidev.http.rest;

import com.tidev.database.entity.fields.Status;
import com.tidev.dto.OrderCreateEditDto;
import com.tidev.dto.OrderFilter;
import com.tidev.dto.OrderProductCreateEditDto;
import com.tidev.dto.OrderReadDto;
import com.tidev.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/statuses")
    public Status[] statuses() {
        return Status.values();
    }

    @GetMapping("")
    public Page<OrderReadDto> findAll(OrderFilter filter, Pageable pageable) {
        return orderService.findAll(filter, pageable);
    }

    @GetMapping("my")
    public Page<OrderReadDto> findAllByUser(OrderFilter filter, Pageable pageable) {
        return orderService.findAll(filter, pageable);
    }

    @GetMapping("/{id}")
    public OrderReadDto findById(@PathVariable("id") Long id) {
        return orderService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/addOrderProduct/{bookPrice}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderReadDto addOrderProduct(@RequestBody OrderProductCreateEditDto orderProductCreateEditDto, @PathVariable BigDecimal bookPrice) {
        return orderService.addOrderProduct(orderProductCreateEditDto, bookPrice)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create/{clientId}")
    public OrderReadDto create(@PathVariable Integer clientId) {
        return orderService.create(clientId);
    }

    @PutMapping("/{id}")
    public OrderReadDto update(@PathVariable("id") Long id, @Validated @RequestBody OrderCreateEditDto order) {
        return orderService.update(id, order)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (!orderService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
