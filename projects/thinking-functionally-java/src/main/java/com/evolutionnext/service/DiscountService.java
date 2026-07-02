package com.evolutionnext.service;
import com.evolutionnext.domain.Customer;
import com.evolutionnext.domain.Discount;
import com.evolutionnext.domain.Order;

import java.util.List;
import java.util.Optional;
public interface DiscountService {
    Optional<Discount> calculateDiscount(
        Customer customer,
        List<Order> orders
    );
}
