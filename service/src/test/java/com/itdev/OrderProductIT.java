package com.itdev;


import com.itdev.database.dao.repositories.OrderProductRepository;
import com.itdev.database.dao.repositories.OrderRepository;
import com.itdev.database.entity.Author;
import com.itdev.database.entity.Book;
import com.itdev.database.entity.Order;
import com.itdev.database.entity.OrderProduct;
import com.itdev.database.entity.User;
import com.itdev.util.HibernateTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderProductIT extends IntegrationTestBase {

    private Order order;
    private Book book;
    private OrderProduct orderProduct;
    @Autowired
    private EntityManager session;
    @Autowired
    private OrderProductRepository repository;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void prepareOrderProductTable() {
        User user = HibernateTestUtil.createUserToReadUpdateDelete();
        order = HibernateTestUtil.createOrder();
        user.addOrder(order);
        Author author = HibernateTestUtil.createAuthorToReadUpdateDelete();
        book = HibernateTestUtil.createBook(author);
        orderProduct = HibernateTestUtil.createOrderProduct();
        order.addOrderProduct(orderProduct);
        book.addOrderProduct(orderProduct);
        session.persist(user);
        session.persist(order);
        session.persist(author);
        session.persist(book);
        session.persist(orderProduct);
        session.flush();
    }

    @Test
    void createOrderProductTest() {
        OrderProduct orderProduct = HibernateTestUtil.createOrderProduct();
        order.addOrderProduct(orderProduct);
        book.addOrderProduct(orderProduct);

        repository.save(orderProduct);

        assertNotNull(orderProduct.getId());
    }

    @Test
    void readOrderTest() {
        session.detach(orderProduct);
        Optional<OrderProduct> maybeOrderProduct = repository.findById(orderProduct.getId());

        assertFalse(maybeOrderProduct.isEmpty());
        assertEquals(orderProduct, maybeOrderProduct.get());
    }

    @Test
    void deleteOrderProductTest() {
        order.getOrderProducts().remove(orderProduct);
        book.getOrderProducts().remove(orderProduct);
        repository.delete(orderProduct);
        OrderProduct actualOrderProduct = session.find(OrderProduct.class, orderProduct.getId());

        assertNull(actualOrderProduct);
    }

    @Test
    void findAllTest() {
        List<OrderProduct> results = repository.findAll();
        assertThat(results).hasSize(12);

        List<Integer> quantities = results.stream().map(OrderProduct::getQuantity).collect(toList());
        assertThat(quantities).containsExactlyInAnyOrder(5, 10, 15, 25, 25, 25, 25, 25, 25, 25, 25, 25);
    }

    @Test
    void deleteAllByOrders() {
        List<Order> orders = orderRepository.findTop3By();
        List<OrderProduct> expectedOrderProducts = new ArrayList<>(repository.findAllByNotOrders(orders));
        expectedOrderProducts.add(orderProduct);

        repository.deleteAllByOrders(orders);

        assertThat(expectedOrderProducts).containsAll(repository.findAll());
    }

    @Test
    void deleteAllByOrdersWithEmptyCollection() {
        List<Order> orders = new ArrayList<>();
        List<OrderProduct> expectedOrderProducts = new ArrayList<>(repository.findAll());
        expectedOrderProducts.add(orderProduct);

        repository.deleteAllByOrders(orders);

        assertThat(expectedOrderProducts).containsAll(repository.findAll());
    }
}
