package com.evolutionnext.inventory.service;

import com.evolutionnext.inventory.domain.Product;
import com.evolutionnext.inventory.domain.ProductId;

import java.util.Optional;

public interface InventoryRepository {
    Optional<Product> load(ProductId id);

    void persist(Product product);
}
