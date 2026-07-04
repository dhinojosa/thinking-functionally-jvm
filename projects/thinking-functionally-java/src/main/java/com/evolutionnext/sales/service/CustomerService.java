package com.evolutionnext.sales.service;
import com.evolutionnext.sales.domain.Customer;
import com.evolutionnext.sales.domain.CustomerId;

import java.util.Optional;
public interface CustomerService {
    Optional<Customer> findCustomer(CustomerId id);
}
