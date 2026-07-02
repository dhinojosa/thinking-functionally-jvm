package com.evolutionnext.service;
import com.evolutionnext.domain.Customer;
import com.evolutionnext.domain.CustomerId;

import java.util.Optional;
public interface CustomerService {
    Optional<Customer> findCustomer(CustomerId id);
}
