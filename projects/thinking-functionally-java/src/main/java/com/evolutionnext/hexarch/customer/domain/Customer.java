package com.evolutionnext.hexarch.customer.domain;

import java.math.BigDecimal;

public record Customer(
    CustomerId id,
    String name,
    BigDecimal creditLimit
) {
}
