package com.evolutionnext.hexarch.order.error;

public sealed interface OrderError
    permits OrderError.NegativeQuantity, OrderError.CustomerNotFound,
    OrderError.ProductNotFound, OrderError.OrderNotFound,
    OrderError.OrderAlreadySubmitted, OrderError.EmptyOrder {

    record NegativeQuantity() implements OrderError {
    }

    record CustomerNotFound() implements OrderError {
    }

    record ProductNotFound() implements OrderError {
    }

    record OrderNotFound() implements OrderError {
    }

    record OrderAlreadySubmitted() implements OrderError {
    }

    record EmptyOrder() implements OrderError {
    }
}
