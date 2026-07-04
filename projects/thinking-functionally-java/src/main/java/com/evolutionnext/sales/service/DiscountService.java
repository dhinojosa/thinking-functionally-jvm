package com.evolutionnext.sales.service;
import com.evolutionnext.sales.domain.Customer;
import com.evolutionnext.sales.domain.Discount;
import com.evolutionnext.sales.domain.Order;

import java.util.List;
import java.util.Optional;
public interface DiscountService {
    Optional<Discount> calculateDiscount(
        Customer customer,
        List<Order> orders
    );
}
