package com.evolutionnext.http4sapp.repository

import cats.syntax.all.*
import com.evolutionnext.http4sapp.domain.*
import doobie.ConnectionIO
import doobie.implicits.*

trait CustomerRepository[F[_]]:
  def find(customerId: CustomerId): F[Option[Customer]]
  def list: F[List[Customer]]

trait InventoryRepository[F[_]]:
  def find(productId: ProductId): F[Option[Product]]
  def list: F[List[Product]]
  def reserve(productId: ProductId, quantity: Int): F[Int]

trait OrderRepository[F[_]]:
  def create(customerId: CustomerId): F[OrderId]
  def find(orderId: OrderId): F[Option[Order]]
  def addItem(orderId: OrderId, product: Product, quantity: Int): F[Unit]
  def submit(orderId: OrderId): F[Unit]

final class DoobieCustomerRepository extends CustomerRepository[ConnectionIO]:
  def find(customerId: CustomerId): ConnectionIO[Option[Customer]] =
    sql"""
      SELECT id, name, credit_limit
      FROM customers
      WHERE id = ${customerId.value}
    """
      .query[(String, String, BigDecimal)]
      .map((id, name, creditLimit) => Customer(CustomerId(id), name, Money(creditLimit)))
      .option

  def list: ConnectionIO[List[Customer]] =
    sql"""
      SELECT id, name, credit_limit
      FROM customers
      ORDER BY id
    """
      .query[(String, String, BigDecimal)]
      .map((id, name, creditLimit) => Customer(CustomerId(id), name, Money(creditLimit)))
      .to[List]

final class DoobieInventoryRepository extends InventoryRepository[ConnectionIO]:
  def find(productId: ProductId): ConnectionIO[Option[Product]] =
    sql"""
      SELECT id, name, price, quantity_available
      FROM products
      WHERE id = ${productId.value}
    """
      .query[(String, String, BigDecimal, Int)]
      .map((id, name, price, available) => Product(ProductId(id), name, Money(price), available))
      .option

  def list: ConnectionIO[List[Product]] =
    sql"""
      SELECT id, name, price, quantity_available
      FROM products
      ORDER BY id
    """
      .query[(String, String, BigDecimal, Int)]
      .map((id, name, price, available) => Product(ProductId(id), name, Money(price), available))
      .to[List]

  def reserve(productId: ProductId, quantity: Int): ConnectionIO[Int] =
    sql"""
      UPDATE products
      SET quantity_available = quantity_available - $quantity
      WHERE id = ${productId.value}
        AND quantity_available >= $quantity
    """.update.run

final class DoobieOrderRepository extends OrderRepository[ConnectionIO]:
  def create(customerId: CustomerId): ConnectionIO[OrderId] =
    sql"""
      INSERT INTO orders (customer_id, status)
      VALUES (${customerId.value}, 'Open')
    """.update.withUniqueGeneratedKeys[Long]("id").map(OrderId.apply)

  def find(orderId: OrderId): ConnectionIO[Option[Order]] =
    for
      order <- sql"""
        SELECT id, customer_id, status
        FROM orders
        WHERE id = ${orderId.value}
      """
        .query[(Long, String, String)]
        .option
      items <- order match
        case None => List.empty[OrderItem].pure[ConnectionIO]
        case Some(_) =>
          sql"""
            SELECT oi.product_id, p.name, oi.quantity, oi.unit_price
            FROM order_items oi
            JOIN products p ON p.id = oi.product_id
            WHERE oi.order_id = ${orderId.value}
            ORDER BY oi.product_id
          """
            .query[(String, String, Int, BigDecimal)]
            .map((productId, name, quantity, unitPrice) =>
              OrderItem(ProductId(productId), name, quantity, Money(unitPrice))
            )
            .to[List]
    yield order.map((id, customerId, status) =>
      Order(OrderId(id), CustomerId(customerId), OrderStatus.valueOf(status), items)
    )

  def addItem(orderId: OrderId, product: Product, quantity: Int): ConnectionIO[Unit] =
    sql"""
      INSERT INTO order_items (order_id, product_id, quantity, unit_price)
      VALUES (${orderId.value}, ${product.id.value}, $quantity, ${product.price.value})
      ON CONFLICT (order_id, product_id)
      DO UPDATE SET quantity = order_items.quantity + EXCLUDED.quantity
    """.update.run.void

  def submit(orderId: OrderId): ConnectionIO[Unit] =
    sql"""
      UPDATE orders
      SET status = 'Submitted'
      WHERE id = ${orderId.value}
    """.update.run.void
