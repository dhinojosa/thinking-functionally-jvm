package com.evolutionnext.sales.service;

import com.evolutionnext.sales.domain.CustomerId;
import com.evolutionnext.sales.domain.Quote;

import java.util.Optional;

public final class QuoteService {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final DiscountService discountService;

    public QuoteService(
        CustomerService customerService,
        OrderService orderService,
        DiscountService discountService
    ) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.discountService = discountService;
    }

    public Optional<Quote> createQuote(CustomerId id) {
        return customerService.findCustomer(id).flatMap(customer ->
            orderService.findOrders(customer).flatMap(listOfOrders ->
                discountService.calculateDiscount(customer, listOfOrders).map(discount ->
                    new Quote(customer, listOfOrders, discount)
                )
            ));
    }
}
