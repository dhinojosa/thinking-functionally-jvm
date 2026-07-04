package com.evolutionnext.sales.service;


import com.evolutionnext.sales.domain.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class QuoteServiceTest {

    @Test
    @Disabled
    public void testQuoteSuccess() {
        CustomerService customerService = id ->
            Optional.of(new Customer(id, "Bob", "Frankin"));

        DiscountService discountService = (customer, orders) ->
            Optional.of(new Discount(.03));

        OrderService orderService = customer -> Optional.of(
            List.of(
                new Order(new OrderId(301L), customer, new Dollars(200)),
                new Order(new OrderId(302L), customer, new Dollars(140))));

        QuoteService quoteService = new QuoteService(customerService, orderService, discountService);
        Optional<Quote> quote = quoteService.createQuote(new CustomerId(30L));
        //add assertion
    }

    @Test
    @Disabled
    public void testQuoteFailure() {
        CustomerService customerService = id ->
            Optional.of(new Customer(id, "Bob", "Frankin"));

        DiscountService discountService = (customer, orders) ->
            Optional.empty();

        OrderService orderService = customer -> Optional.of(
            List.of(
                new Order(new OrderId(301L), customer, new Dollars(200)),
                new Order(new OrderId(302L), customer, new Dollars(140))));

        QuoteService quoteService = new QuoteService(customerService, orderService, discountService);
        Optional<Quote> quote = quoteService.createQuote(new CustomerId(30L));
        //add assertion
    }
}
