package com.evolutionnext.domain;

public record Order(
    OrderId id,
    Customer customer,
    Dollars total
) {}
