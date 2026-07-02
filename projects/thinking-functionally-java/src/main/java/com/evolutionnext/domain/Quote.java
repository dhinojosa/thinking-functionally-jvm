package com.evolutionnext.domain;

import java.util.List;

public record Quote(
    Customer customer,
    List<Order> orders,
    Discount discount
) { }
