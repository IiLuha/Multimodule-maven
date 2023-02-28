package com.dmdev.mapper;

import com.dmdev.database.dao.repositories.BookRepository;
import com.dmdev.database.dao.repositories.OrderRepository;
import com.dmdev.database.dao.repositories.UserRepository;
import com.dmdev.database.entity.Book;
import com.dmdev.database.entity.Order;
import com.dmdev.database.entity.OrderProduct;
import com.dmdev.database.entity.fields.Status;
import com.dmdev.dto.OrderProductCreateEditDto;
import com.dmdev.exceptions.OutOfQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderProductCreateEditMapper implements Mapper<OrderProductCreateEditDto, OrderProduct> {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Override
    public OrderProduct map(OrderProductCreateEditDto fromObject, OrderProduct toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public OrderProduct map(OrderProductCreateEditDto object) {
        OrderProduct orderProduct = new OrderProduct();
        copy(object, orderProduct);
        return orderProduct;
    }

    private void copy(OrderProductCreateEditDto object, OrderProduct orderProduct) {
        Optional<Order> maybeOrder = orderRepository.findById(object.getOrderId());
        Optional<Book> maybeBook = bookRepository.findById(object.getBookId());
        orderProduct.setOrder(maybeOrder.orElseThrow());
        Book book = maybeBook.orElseThrow();
        orderProduct.setBook(book);
        orderProduct.setQuantity(object.getQuantity());
        if (object.getQuantity() > book.getQuantity()){
            throw new OutOfQuantityException("There are not enough books in the storage");
        }
        orderProduct.setTotalPrice(object.getTotalPrice());
    }
}
