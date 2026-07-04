package com.evolutionnext.hexarch.order.domain;

import com.evolutionnext.hexarch.customer.domain.CustomerId;
import com.evolutionnext.inventory.domain.ProductId;

import java.util.List;

public record Order(
    OrderId id,
    CustomerId customerId,
    List<LineItem> lineItems,
    boolean submitted
) {
    public Order {
        lineItems = List.copyOf(lineItems);
    }

    public static Order empty(OrderId id, CustomerId customerId) {
        return new Order(id, customerId, List.of(), false);
    }

    public Order addLineItem(ProductId productId, int quantity) {
        var updated = new java.util.ArrayList<>(lineItems);
        updated.add(new LineItem(productId, quantity));
        return new Order(id, customerId, updated, submitted);
    }

    public Order removeLineItem(ProductId productId) {
        var updated = lineItems.stream()
            .filter(lineItem -> !lineItem.productId().equals(productId))
            .toList();
        return new Order(id, customerId, updated, submitted);
    }

    public Order submit() {
        return new Order(id, customerId, lineItems, true);
    }
}
