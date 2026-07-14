package com.evolutionnext.hexarch.order.error;

import com.evolutionnext.hexarch.order.domain.OrderId;
import com.evolutionnext.inventory.domain.ProductId;

public sealed interface OrderResult
    permits OrderResult.ItemAdded, OrderResult.ItemRemoved, OrderResult.OrderSubmitted {

    record ItemAdded(OrderId orderId, ProductId productId) implements OrderResult {
    }

    record ItemRemoved(OrderId orderId, ProductId productId) implements OrderResult {
    }

    record OrderSubmitted(OrderId orderId) implements OrderResult {
    }
}
