package com.evolutionnext.http4sapp.service

import cats.MonadThrow
import cats.arrow.FunctionK
import cats.syntax.all.*
import com.evolutionnext.http4sapp.domain.*
import com.evolutionnext.http4sapp.domain.OrderCommand.*
import com.evolutionnext.http4sapp.domain.OrderCommandResult.*
import com.evolutionnext.http4sapp.domain.OrderError.*
import com.evolutionnext.http4sapp.repository.*
import doobie.ConnectionIO

final class CustomerService[F[_]: MonadThrow](
    customerRepository: CustomerRepository,
    transaction: FunctionK[ConnectionIO, F]
):
  def find(customerId: CustomerId): F[Customer] =
    transaction(customerRepository.find(customerId)).flatMap {
      case Some(customer) => customer.pure[F]
      case None => CustomerNotFound(customerId).raiseError[F, Customer]
    }

  def list: F[List[Customer]] =
    transaction(customerRepository.list)

final class InventoryService[F[_]: MonadThrow](
    inventoryRepository: InventoryRepository,
    transaction: FunctionK[ConnectionIO, F]
):
  def find(productId: ProductId): F[Product] =
    transaction(inventoryRepository.find(productId)).flatMap {
      case Some(product) => product.pure[F]
      case None => ProductNotFound(productId).raiseError[F, Product]
    }

  def list: F[List[Product]] =
    transaction(inventoryRepository.list)

final class OrderService[F[_]: MonadThrow](
    orderRepository: OrderRepository,
    customerRepository: CustomerRepository,
    inventoryRepository: InventoryRepository,
    transaction: FunctionK[ConnectionIO, F]
):
  def execute(command: OrderCommand): F[OrderCommandResult] =
    command match
      case CreateOrder(customerId) => createOrder(customerId)
      case AddItem(orderId, productId, quantity) => addItem(orderId, productId, quantity)
      case SubmitOrder(orderId) => submitOrder(orderId)

  private def createOrder(customerId: CustomerId): F[OrderCommandResult] =
    transaction {
      for
        customer <- customerRepository.find(customerId).flatMap {
          case Some(customer) => customer.pure[ConnectionIO]
          case None => CustomerNotFound(customerId).raiseError[ConnectionIO, Customer]
        }
        orderId <- orderRepository.create(customer.id)
      yield OrderCreated(orderId)
    }

  private def addItem(orderId: OrderId, productId: ProductId, quantity: Int): F[OrderCommandResult] =
    transaction {
      for
        _ <- InvalidQuantity(quantity).raiseError[ConnectionIO, Unit].whenA(quantity <= 0)
        order <- loadOrder(orderId)
        _ <- OrderAlreadySubmitted(orderId).raiseError[ConnectionIO, Unit]
          .whenA(order.status == OrderStatus.Submitted)
        product <- inventoryRepository.find(productId).flatMap {
          case Some(product) => product.pure[ConnectionIO]
          case None => ProductNotFound(productId).raiseError[ConnectionIO, Product]
        }
        _ <- InsufficientInventory(productId, quantity, product.quantityAvailable)
          .raiseError[ConnectionIO, Unit]
          .whenA(product.quantityAvailable < quantity)
        _ <- orderRepository.addItem(orderId, product, quantity)
        updated <- loadOrder(orderId)
        customer <- customerRepository.find(updated.customerId).flatMap {
          case Some(customer) => customer.pure[ConnectionIO]
          case None => CustomerNotFound(updated.customerId).raiseError[ConnectionIO, Customer]
        }
        _ <- CreditLimitExceeded(customer.id, updated.total, customer.creditLimit)
          .raiseError[ConnectionIO, Unit]
          .whenA(updated.total > customer.creditLimit)
      yield ItemAdded(orderId, productId)
    }

  private def submitOrder(orderId: OrderId): F[OrderCommandResult] =
    transaction {
      for
        order <- loadOrder(orderId)
        _ <- OrderAlreadySubmitted(orderId).raiseError[ConnectionIO, Unit]
          .whenA(order.status == OrderStatus.Submitted)
        _ <- order.items.traverse_ { item =>
          for
            product <- inventoryRepository.find(item.productId).flatMap {
              case Some(product) => product.pure[ConnectionIO]
              case None => ProductNotFound(item.productId).raiseError[ConnectionIO, Product]
            }
            reserved <- inventoryRepository.reserve(item.productId, item.quantity)
            _ <- InsufficientInventory(item.productId, item.quantity, product.quantityAvailable)
              .raiseError[ConnectionIO, Unit]
              .whenA(reserved == 0)
          yield ()
        }
        _ <- orderRepository.submit(orderId)
      yield OrderSubmitted(orderId)
    }

  private def loadOrder(orderId: OrderId): ConnectionIO[Order] =
    orderRepository.find(orderId).flatMap {
      case Some(order) => order.pure[ConnectionIO]
      case None => OrderNotFound(orderId).raiseError[ConnectionIO, Order]
    }

final class OrderQueryService[F[_]: MonadThrow](
    orderRepository: OrderRepository,
    transaction: FunctionK[ConnectionIO, F]
):
  def find(orderId: OrderId): F[Order] =
    transaction(loadOrder(orderId))

  private def loadOrder(orderId: OrderId): ConnectionIO[Order] =
    orderRepository.find(orderId).flatMap {
      case Some(order) => order.pure[ConnectionIO]
      case None => OrderNotFound(orderId).raiseError[ConnectionIO, Order]
    }
