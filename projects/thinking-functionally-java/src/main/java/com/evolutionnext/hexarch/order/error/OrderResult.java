package com.evolutionnext.hexarch.order.error;

import com.evolutionnext.hexarch.order.domain.OrderId;

public sealed interface OrderResult
    permits OrderResult.Success, OrderResult.Failure {

    record Success(OrderId orderId) implements OrderResult {
    }

    record Failure(String message) implements OrderResult {
    }
}
