package com.evolutionnext.hexarch.order.service;

import com.evolutionnext.hexarch.customer.service.CustomerRepository;
import com.evolutionnext.hexarch.order.command.OrderCommand;
import com.evolutionnext.hexarch.order.domain.Order;
import com.evolutionnext.hexarch.order.error.OrderResult;
import com.evolutionnext.inventory.service.InventoryRepository;

public final class OrderApplicationService {
    private final CustomerRepository customers;
    private final OrderRepository orders;
    private final InventoryRepository inventory;

    public OrderApplicationService(
        CustomerRepository customers,
        OrderRepository orders,
        InventoryRepository inventory
    ) {
        this.customers = customers;
        this.orders = orders;
        this.inventory = inventory;
    }

    public OrderResult handle(OrderCommand command) {
        return switch (command) {
            case OrderCommand.AddLineItem addLineItem -> addLineItem(addLineItem);
            case OrderCommand.RemoveLineItem removeLineItem -> removeLineItem(removeLineItem);
            case OrderCommand.SubmitOrder submitOrder -> submitOrder(submitOrder);
        };
    }

    private OrderResult addLineItem(OrderCommand.AddLineItem command) {
        if (command.quantity() <= 0) {
            return new OrderResult.Failure("Quantity must be positive");
        }

        var customer = customers.load(command.customerId());
        if (customer.isEmpty()) {
            return new OrderResult.Failure("Customer not found");
        }

        var product = inventory.load(command.productId());
        if (product.isEmpty()) {
            return new OrderResult.Failure("Product not found");
        }

        var order = orders.load(command.orderId())
            .orElseGet(() -> Order.empty(command.orderId(), command.customerId()));

        if (order.submitted()) {
            return new OrderResult.Failure("Order already submitted");
        }

        orders.save(order.addLineItem(command.productId(), command.quantity()));
        return new OrderResult.Success(command.orderId());
    }

    private OrderResult removeLineItem(OrderCommand.RemoveLineItem command) {
        var order = orders.load(command.orderId());
        if (order.isEmpty()) {
            return new OrderResult.Failure("Order not found");
        }

        if (order.get().submitted()) {
            return new OrderResult.Failure("Order already submitted");
        }

        orders.save(order.get().removeLineItem(command.productId()));
        return new OrderResult.Success(command.orderId());
    }

    private OrderResult submitOrder(OrderCommand.SubmitOrder command) {
        var order = orders.load(command.orderId());
        if (order.isEmpty()) {
            return new OrderResult.Failure("Order not found");
        }

        if (order.get().lineItems().isEmpty()) {
            return new OrderResult.Failure("Order has no line items");
        }

        orders.save(order.get().submit());
        return new OrderResult.Success(command.orderId());
    }
}
