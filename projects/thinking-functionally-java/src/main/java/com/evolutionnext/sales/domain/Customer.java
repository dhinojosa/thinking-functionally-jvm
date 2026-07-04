package com.evolutionnext.sales.domain;

public record Customer(
    CustomerId id,
    String firstName,
    String lastName
) {}
