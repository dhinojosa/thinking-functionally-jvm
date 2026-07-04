package com.evolutionnext.hexarch.order.command;

import com.evolutionnext.hexarch.customer.domain.CustomerId;
import com.evolutionnext.hexarch.order.domain.OrderId;
import com.evolutionnext.inventory.domain.ProductId;

public sealed interface OrderCommand {

    record AddLineItem(
        OrderId orderId,
        CustomerId customerId,
        ProductId productId,
        int quantity
    ) implements OrderCommand {
    }

    record RemoveLineItem(
        OrderId orderId,
        ProductId productId
    ) implements OrderCommand {
    }

    record SubmitOrder(
        OrderId orderId
    ) implements OrderCommand {
    }
}
