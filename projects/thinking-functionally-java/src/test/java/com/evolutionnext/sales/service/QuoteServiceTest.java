package com.evolutionnext.sales.service;


import com.evolutionnext.sales.domain.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class QuoteServiceTest {

    @Test
    public void testQuoteSuccess() {
        Comparator<Customer> compareCustomerByFirstName = Comparator.comparing(Customer::firstName);
        Comparator<Customer> compareCustomerByLastName = Comparator.comparing(Customer::lastName);

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
        assertThat(quote).isPresent();
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
        assertThat(quote).isEmpty();
    }
}
