package com.evolutionnext.http4sapp.domain

import java.math.BigDecimal as JBigDecimal

final case class CustomerId(value: String) extends AnyVal
final case class ProductId(value: String) extends AnyVal
final case class OrderId(value: Long) extends AnyVal

final case class Money(value: BigDecimal):
  def +(that: Money): Money = Money(value + that.value)
  def *(quantity: Int): Money = Money(value * BigDecimal(quantity))
  def >(that: Money): Boolean = value > that.value

object Money:
  val zero: Money = Money(BigDecimal(0))
  def fromJava(value: JBigDecimal): Money = Money(BigDecimal(value))

enum OrderStatus:
  case Open, Submitted

final case class Customer(id: CustomerId, name: String, creditLimit: Money)
final case class Product(id: ProductId, name: String, price: Money, quantityAvailable: Int)
final case class OrderItem(productId: ProductId, name: String, quantity: Int, unitPrice: Money):
  def total: Money = unitPrice * quantity

final case class Order(id: OrderId, customerId: CustomerId, status: OrderStatus, items: List[OrderItem]):
  def total: Money = items.foldLeft(Money.zero)((sum, item) => sum + item.total)

sealed trait OrderCommand

object OrderCommand:
  final case class CreateOrder(customerId: CustomerId) extends OrderCommand
  final case class AddItem(orderId: OrderId, productId: ProductId, quantity: Int) extends OrderCommand
  final case class SubmitOrder(orderId: OrderId) extends OrderCommand

sealed trait OrderCommandResult

object OrderCommandResult:
  final case class OrderCreated(orderId: OrderId) extends OrderCommandResult
  final case class ItemAdded(orderId: OrderId, productId: ProductId) extends OrderCommandResult
  final case class OrderSubmitted(orderId: OrderId) extends OrderCommandResult

sealed abstract class OrderError(message: String) extends RuntimeException(message)

object OrderError:
  final case class CustomerNotFound(customerId: CustomerId)
      extends OrderError(s"Customer ${customerId.value} was not found")

  final case class ProductNotFound(productId: ProductId)
      extends OrderError(s"Product ${productId.value} was not found")

  final case class OrderNotFound(orderId: OrderId)
      extends OrderError(s"Order ${orderId.value} was not found")

  final case class OrderAlreadySubmitted(orderId: OrderId)
      extends OrderError(s"Order ${orderId.value} has already been submitted")

  final case class InvalidQuantity(quantity: Int)
      extends OrderError(s"Quantity must be positive: $quantity")

  final case class InsufficientInventory(productId: ProductId, requested: Int, available: Int)
      extends OrderError(
        s"Product ${productId.value} has $available available; requested $requested"
      )

  final case class CreditLimitExceeded(customerId: CustomerId, total: Money, creditLimit: Money)
      extends OrderError(
        s"Order total ${total.value} exceeds credit limit ${creditLimit.value} for ${customerId.value}"
      )
