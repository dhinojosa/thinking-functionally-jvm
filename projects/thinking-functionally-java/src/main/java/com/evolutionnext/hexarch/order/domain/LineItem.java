package com.evolutionnext.hexarch.order.domain;

import com.evolutionnext.inventory.domain.ProductId;

public record LineItem(
    ProductId productId,
    int quantity
) {
    public LineItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
