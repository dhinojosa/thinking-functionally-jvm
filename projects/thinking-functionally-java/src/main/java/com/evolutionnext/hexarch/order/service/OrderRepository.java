package com.evolutionnext.hexarch.order.service;

import com.evolutionnext.hexarch.order.domain.Order;
import com.evolutionnext.hexarch.order.domain.OrderId;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> load(OrderId id);

    void save(Order order);
}
