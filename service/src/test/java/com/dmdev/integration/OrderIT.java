package com.dmdev.integration;

import com.dmdev.database.dao.repositories.OrderRepository;
import com.dmdev.database.dao.repositories.UserRepository;
import com.dmdev.database.entity.Order;
import com.dmdev.database.entity.User;
import com.dmdev.database.entity.fields.Status;
import com.dmdev.util.HibernateTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderIT extends IntegrationTestBase {

    private User user;
    private Order rudOrder;
    @Autowired
    private EntityManager session;
    @Autowired
    private OrderRepository repository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void prepareOrderTable() {
        user = HibernateTestUtil.createUserToReadUpdateDelete();
        rudOrder = HibernateTestUtil.createOrder();
        user.addOrder(rudOrder);
        session.persist(user);
        session.persist(rudOrder);
    }

    @Test
    void createOrderTest() {
        Order order = HibernateTestUtil.createToSaveOrder();
        user.addOrder(order);

        repository.save(order);

        assertNotNull(order.getId());
    }

    @Test
    void readOrderTest() {
        session.detach(rudOrder);
        Optional<Order> maybeOrder = repository.findById(rudOrder.getId());

        assertFalse(maybeOrder.isEmpty());
        assertEquals(rudOrder, maybeOrder.get());
    }

    @Test
    void deleteOrderTest() {
        user.getOrders().remove(rudOrder);
        repository.delete(rudOrder);
        Order actualOrder = session.find(Order.class, rudOrder.getId());

        assertNull(actualOrder);
    }

    @Test
    void findAllTest() {
        List<Order> results = repository.findAll();
        assertThat(results).hasSize(5);

        List<Status> fullNames = results.stream().map(Order::getStatus).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder(Status.OPEN, Status.OPEN, Status.OPEN, Status.OPEN, Status.OPEN);
    }

    @Test
    void findAllByUserTest() {
        Optional<User> userForFind = userRepository.findTopBy();

        assertFalse(userForFind.isEmpty());
        List<Order> results = repository.findAllByUserId(userForFind.get().getId());
        assertThat(results).hasSize(userForFind.get().getOrders().size());
    }

    @Test
    void updateStatusByIdTest() {
        repository.updateStatusByIds(Status.CLOSED, rudOrder.getId());
        session.flush();
        session.detach(rudOrder);
        Order actualOrder = session.find(Order.class, rudOrder.getId());

        assertEquals(rudOrder, actualOrder);
    }

    @Test
    void deleteOrdersMadeEarlierDateTest() {
        repository.deleteAllByCreatedAtBefore(LocalDateTime.now().minus(Period.ofYears(2)));

        List<Order> actualOrders = repository.findAll();
        assertThat(actualOrders).hasSize(2);

        repository.deleteAllByCreatedAtBefore(LocalDateTime.now());
        user.getOrders().remove(rudOrder);

        actualOrders = repository.findAll();
        assertThat(actualOrders).hasSize(0);
    }
}