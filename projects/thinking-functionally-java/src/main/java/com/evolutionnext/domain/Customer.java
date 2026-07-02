package com.evolutionnext.domain;

public record Customer(
    CustomerId id,
    String firstName,
    String lastName
) {}
