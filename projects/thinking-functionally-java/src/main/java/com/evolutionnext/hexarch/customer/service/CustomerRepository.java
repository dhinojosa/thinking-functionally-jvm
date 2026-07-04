package com.evolutionnext.hexarch.customer.service;

import com.evolutionnext.hexarch.customer.domain.Customer;
import com.evolutionnext.hexarch.customer.domain.CustomerId;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> load(CustomerId id);
}
