package com.gabor.carrental;

import com.gabor.carrental.data.OrderEntity;
import com.gabor.carrental.models.OrderModel;
import com.gabor.carrental.services.OrderService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class OrderServiceTest {


    @Autowired
    private OrderService orderService;

    @Test
    void shouldReturnAllOrders(){
        List<OrderEntity> orders = orderService.findAll();
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(3);
        assertThat(orders.get(0).getName()).isEqualTo("Gabor");
    }

    @Test
    void shouldReturnOneOrderWithRequestedId(){
        OrderEntity order = orderService.findById(1);
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(1);
        assertThat(order.getName()).isEqualTo("Gabor");
    }

    @Test
    void shouldReturnNullIfIdDoesNotExist(){
        OrderEntity order = orderService.findById(10);
        assertThat(order).isNull();
    }

    @Test
    @Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldSaveANewOrder(){
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        List<OrderEntity> orders = orderService.findAll();
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(3);

        OrderModel order = new OrderModel(0, "Dominik", "email", "addr", "+36", 2, 42.2,
        today, today, 2);

        OrderEntity savedOrder = orderService.save(order);
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(4); //4th inserted element

        orders = orderService.findAll();
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(4);
    }


    @Test
    @Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Transactional
    void shouldDeleteAllOrdersWithRequestedCarId(){
        List<OrderEntity> orders = orderService.findAll();
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(3);

        orderService.deleteByCarId(1);

        orders = orderService.findAll();
        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(1);
    }
}
