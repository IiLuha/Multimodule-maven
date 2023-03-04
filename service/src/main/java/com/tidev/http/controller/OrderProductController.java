package com.tidev.http.controller;

import com.tidev.dto.BookFilter;
import com.tidev.dto.BookReadDto;
import com.tidev.dto.OrderProductCreateEditDto;
import com.tidev.dto.PageResponse;
import com.tidev.service.BookService;
import com.tidev.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Controller
@RequestMapping("/orderProducts")
@RequiredArgsConstructor
public class OrderProductController {

    private final OrderProductService orderProductService;
    private final BookService bookService;

    @PostMapping("/{id}/findBook")
    public String findBookToAdd(Model model, @PathVariable("id") Long id, BookFilter filter, Pageable pageable) {
        Page<BookReadDto> page = bookService.findAll(filter, pageable);
        model.addAttribute("books", PageResponse.of(page));
        model.addAttribute("clientId", id);
        model.addAttribute("filter", filter);
        return "order/books";
    }

    @PostMapping("/{clientId}/createOrderProduct")
    public String createOrderProduct(Model model, @PathVariable("clientId") Long clientId, Long bookId, Integer bookQuantity, BigDecimal bookPrice) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookQuantity", bookQuantity);
        model.addAttribute("bookPrice", bookPrice);
        return "order/orderProduct";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @Validated OrderProductCreateEditDto orderProduct) {
        return orderProductService.update(id, orderProduct)
                .map(it -> "redirect:/orders/" + orderProduct.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, Long clientId) {
        if (orderProductService.delete(id)) {
            return "redirect:/orders/" + clientId;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
