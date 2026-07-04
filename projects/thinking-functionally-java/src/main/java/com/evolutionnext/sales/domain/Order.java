package com.evolutionnext.sales.domain;

public record Order(
    OrderId id,
    Customer customer,
    Dollars total
) {}
