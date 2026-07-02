package com.evolutionnext.service;
import com.evolutionnext.domain.Customer;
import com.evolutionnext.domain.Order;

import java.util.List;
import java.util.Optional;
public interface OrderService {
    Optional<List<Order>> findOrders(Customer customer);
}
