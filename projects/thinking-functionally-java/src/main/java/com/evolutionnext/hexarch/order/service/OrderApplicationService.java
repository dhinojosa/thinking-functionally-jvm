package com.evolutionnext.hexarch.order.service;

import com.evolutionnext.hexarch.common.Either;
import com.evolutionnext.hexarch.customer.service.CustomerRepository;
import com.evolutionnext.hexarch.order.command.OrderCommand;
import com.evolutionnext.hexarch.order.domain.Order;
import com.evolutionnext.hexarch.order.error.OrderError;
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

    public Either<OrderError, OrderResult> handle(OrderCommand command) {
        return switch (command) {
            case OrderCommand.AddLineItem addLineItem -> addLineItem(addLineItem);
            case OrderCommand.RemoveLineItem removeLineItem -> removeLineItem(removeLineItem);
            case OrderCommand.SubmitOrder submitOrder -> submitOrder(submitOrder);
        };
    }

    private Either<OrderError, OrderResult> addLineItem(OrderCommand.AddLineItem command) {
        if (command.quantity() <= 0) {
            return Either.left(new OrderError.NegativeQuantity());
        }

        var customer = customers.load(command.customerId());
        if (customer.isEmpty()) {
            return Either.left(new OrderError.CustomerNotFound());
        }

        var product = inventory.load(command.productId());
        if (product.isEmpty()) {
            return Either.left(new OrderError.ProductNotFound());
        }

        var order = orders.load(command.orderId())
            .orElseGet(() -> Order.empty(command.orderId(), command.customerId()));

        if (order.submitted()) {
            return Either.left(new OrderError.OrderAlreadySubmitted());
        }

        orders.save(order.addLineItem(command.productId(), command.quantity()));
        return Either.right(new OrderResult.ItemAdded(command.orderId(), command.productId()));
    }

    private Either<OrderError, OrderResult> removeLineItem(OrderCommand.RemoveLineItem command) {
        var order = orders.load(command.orderId());
        if (order.isEmpty()) {
            return Either.left(new OrderError.OrderNotFound());
        }

        if (order.get().submitted()) {
            return Either.left(new OrderError.OrderAlreadySubmitted());
        }

        orders.save(order.get().removeLineItem(command.productId()));
        return Either.right(new OrderResult.ItemRemoved(command.orderId(), command.productId()));
    }

    private Either<OrderError, OrderResult> submitOrder(OrderCommand.SubmitOrder command) {
        var order = orders.load(command.orderId());
        if (order.isEmpty()) {
            return Either.left(new OrderError.OrderNotFound());
        }

        if (order.get().lineItems().isEmpty()) {
            return Either.left(new OrderError.EmptyOrder());
        }

        orders.save(order.get().submit());
        return Either.right(new OrderResult.OrderSubmitted(command.orderId()));
    }
}
