package com.tidev.http.controller;

import com.tidev.database.entity.fields.Role;
import com.tidev.database.entity.fields.Status;
import com.tidev.dto.OrderCreateEditDto;
import com.tidev.dto.OrderFilter;
import com.tidev.dto.OrderProductCreateEditDto;
import com.tidev.dto.OrderReadDto;
import com.tidev.dto.PageResponse;
import com.tidev.service.OrderService;
import com.tidev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("")
    public String findAll(Model model, OrderFilter filter, Pageable pageable) {
        Page<OrderReadDto> page = orderService.findAll(filter, pageable);
        model.addAttribute("orders", PageResponse.of(page));
        model.addAttribute("filter", filter);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("roles", Role.values());
        return "order/orders";
    }

    @GetMapping("my")
    public String findAllByUser(Model model, OrderFilter filter, Pageable pageable) {
        Page<OrderReadDto> page = orderService.findAll(filter, pageable);
        model.addAttribute("orders", PageResponse.of(page));
        model.addAttribute("filter", filter);
        model.addAttribute("statuses", Status.values());
        return "order/orders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return orderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("client", userService.findById(order.getClientId()).orElseThrow());
                    model.addAttribute("statuses", Status.values());
                    return "order/order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addOrderProduct")
    public String addOrderProduct(OrderProductCreateEditDto orderProduct, BigDecimal bookPrice) {
        return orderService.addOrderProduct(orderProduct, bookPrice)
                .map(order -> "redirect:/orders/" + order.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create/{clientId}")
    public String create(@PathVariable Integer clientId) {
        return "redirect:/orders/" + orderService.create(clientId).getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @Validated OrderCreateEditDto order) {
        return orderService.update(id, order)
                .map(it -> "redirect:/orders/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (orderService.delete(id)) {
            return "redirect:/orders";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
