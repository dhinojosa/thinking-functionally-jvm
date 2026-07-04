package com.evolutionnext.hexarch.order.service;

import com.evolutionnext.hexarch.customer.domain.Customer;
import com.evolutionnext.hexarch.customer.domain.CustomerId;
import com.evolutionnext.hexarch.customer.service.CustomerRepository;
import com.evolutionnext.hexarch.order.command.OrderCommand;
import com.evolutionnext.hexarch.order.domain.Order;
import com.evolutionnext.hexarch.order.domain.OrderId;
import com.evolutionnext.hexarch.order.error.OrderResult;
import com.evolutionnext.inventory.domain.Product;
import com.evolutionnext.inventory.domain.ProductId;
import com.evolutionnext.inventory.service.InventoryRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderApplicationServiceTest {

    @Test
    void handlesOrderCommandsThroughPorts() {
        var customerId = new CustomerId(100L);
        var orderId = new OrderId(200L);
        var productId = new ProductId(300L);

        var customers = new StubCustomerRepository();
        var orders = new StubOrderRepository();
        var inventory = new StubInventoryRepository();

        customers.persist(
            new Customer(customerId, "Ada Lovelace", new BigDecimal("500.00")));
        inventory.persist(new Product(productId, "Keyboard"));

        var service =
            new OrderApplicationService(customers, orders, inventory);

        var addResult = service.handle(
            new OrderCommand.AddLineItem(orderId, customerId, productId, 2));
        var submitResult = service.handle(new OrderCommand.SubmitOrder(orderId));

        assertThat(addResult).isEqualTo(new OrderResult.Success(orderId));
        assertThat(submitResult).isEqualTo(new OrderResult.Success(orderId));
        assertThat(orders.load(orderId))
            .get()
            .extracting(Order::submitted)
            .isEqualTo(true);
    }

    @Test
    void returnsFailureWhenProductDoesNotExist() {
        var customerId = new CustomerId(100L);
        var orderId = new OrderId(200L);
        var productId = new ProductId(300L);

        var customers = new StubCustomerRepository();
        var orders = new StubOrderRepository();
        var inventory = new StubInventoryRepository();

        customers.persist(
            new Customer(customerId, "Ada Lovelace", new BigDecimal("500.00")));

        var service =
            new OrderApplicationService(customers, orders, inventory);

        var result = service.handle(
            new OrderCommand.AddLineItem(orderId, customerId, productId, 2));

        assertThat(result)
            .isEqualTo(new OrderResult.Failure("Product not found"));
        assertThat(orders.load(orderId)).isEmpty();
    }

    private static final class StubCustomerRepository
        implements CustomerRepository {
        private final Map<CustomerId, Customer> customers = new HashMap<>();

        @Override
        public Optional<Customer> load(CustomerId id) {
            return Optional.ofNullable(customers.get(id));
        }

        void persist(Customer customer) {
            customers.put(customer.id(), customer);
        }
    }

    private static final class StubInventoryRepository
        implements InventoryRepository {
        private final Map<ProductId, Product> products = new HashMap<>();

        @Override
        public Optional<Product> load(ProductId id) {
            return Optional.ofNullable(products.get(id));
        }

        @Override
        public void persist(Product product) {
            products.put(product.id(), product);
        }
    }

    private static final class StubOrderRepository implements OrderRepository {
        private final Map<OrderId, Order> orders = new HashMap<>();

        @Override
        public Optional<Order> load(OrderId id) {
            return Optional.ofNullable(orders.get(id));
        }

        @Override
        public void save(Order order) {
            orders.put(order.id(), order);
        }
    }
}
