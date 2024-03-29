package com.itdev.mapper;

import com.itdev.database.dao.repositories.BookRepository;
import com.itdev.database.dao.repositories.OrderRepository;
import com.itdev.database.entity.Book;
import com.itdev.database.entity.Order;
import com.itdev.database.entity.OrderProduct;
import com.itdev.dto.OrderProductCreateEditDto;
import com.itdev.exceptions.OutOfQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
