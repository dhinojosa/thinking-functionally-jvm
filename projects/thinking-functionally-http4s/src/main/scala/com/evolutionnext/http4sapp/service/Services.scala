package com.evolutionnext.http4sapp.service

import cats.MonadThrow
import cats.arrow.FunctionK
import cats.syntax.all.*
import com.evolutionnext.http4sapp.domain.*
import com.evolutionnext.http4sapp.domain.OrderCommand.*
import com.evolutionnext.http4sapp.domain.OrderCommandResult.*
import com.evolutionnext.http4sapp.domain.OrderError.*
import com.evolutionnext.http4sapp.repository.*

final class CustomerService[F[_], G[_]: MonadThrow](
    customerRepository: CustomerRepository[G],
    transaction: FunctionK[G, F]
):
  def find(customerId: CustomerId): F[Customer] =
    transaction {
      customerRepository.find(customerId).flatMap {
        case Some(customer) => customer.pure[G]
        case None => CustomerNotFound(customerId).raiseError[G, Customer]
      }
    }

  def list: F[List[Customer]] =
    transaction(customerRepository.list)

final class InventoryService[F[_], G[_]: MonadThrow](
    inventoryRepository: InventoryRepository[G],
    transaction: FunctionK[G, F]
):
  def find(productId: ProductId): F[Product] =
    transaction {
      inventoryRepository.find(productId).flatMap {
        case Some(product) => product.pure[G]
        case None => ProductNotFound(productId).raiseError[G, Product]
      }
    }

  def list: F[List[Product]] =
    transaction(inventoryRepository.list)

final class OrderService[F[_], G[_]: MonadThrow](
    orderRepository: OrderRepository[G],
    customerRepository: CustomerRepository[G],
    inventoryRepository: InventoryRepository[G],
    transaction: FunctionK[G, F]
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
          case Some(customer) => customer.pure[G]
          case None => CustomerNotFound(customerId).raiseError[G, Customer]
        }
        orderId <- orderRepository.create(customer.id)
      yield OrderCreated(orderId)
    }

  private def addItem(orderId: OrderId, productId: ProductId, quantity: Int): F[OrderCommandResult] =
    transaction {
      for
        _ <- InvalidQuantity(quantity).raiseError[G, Unit].whenA(quantity <= 0)
        order <- loadOrder(orderId)
        _ <- OrderAlreadySubmitted(orderId).raiseError[G, Unit]
          .whenA(order.status == OrderStatus.Submitted)
        product <- inventoryRepository.find(productId).flatMap {
          case Some(product) => product.pure[G]
          case None => ProductNotFound(productId).raiseError[G, Product]
        }
        _ <- InsufficientInventory(productId, quantity, product.quantityAvailable)
          .raiseError[G, Unit]
          .whenA(product.quantityAvailable < quantity)
        _ <- orderRepository.addItem(orderId, product, quantity)
        updated <- loadOrder(orderId)
        customer <- customerRepository.find(updated.customerId).flatMap {
          case Some(customer) => customer.pure[G]
          case None => CustomerNotFound(updated.customerId).raiseError[G, Customer]
        }
        _ <- CreditLimitExceeded(customer.id, updated.total, customer.creditLimit)
          .raiseError[G, Unit]
          .whenA(updated.total > customer.creditLimit)
      yield ItemAdded(orderId, productId)
    }

  private def submitOrder(orderId: OrderId): F[OrderCommandResult] =
    transaction {
      for
        order <- loadOrder(orderId)
        _ <- OrderAlreadySubmitted(orderId).raiseError[G, Unit]
          .whenA(order.status == OrderStatus.Submitted)
        _ <- order.items.traverse_ { item =>
          for
            product <- inventoryRepository.find(item.productId).flatMap {
              case Some(product) => product.pure[G]
              case None => ProductNotFound(item.productId).raiseError[G, Product]
            }
            reserved <- inventoryRepository.reserve(item.productId, item.quantity)
            _ <- InsufficientInventory(item.productId, item.quantity, product.quantityAvailable)
              .raiseError[G, Unit]
              .whenA(reserved == 0)
          yield ()
        }
        _ <- orderRepository.submit(orderId)
      yield OrderSubmitted(orderId)
    }

  private def loadOrder(orderId: OrderId): G[Order] =
    orderRepository.find(orderId).flatMap {
      case Some(order) => order.pure[G]
      case None => OrderNotFound(orderId).raiseError[G, Order]
    }

final class OrderQueryService[F[_], G[_]: MonadThrow](
    orderRepository: OrderRepository[G],
    transaction: FunctionK[G, F]
):
  def find(orderId: OrderId): F[Order] =
    transaction(loadOrder(orderId))

  private def loadOrder(orderId: OrderId): G[Order] =
    orderRepository.find(orderId).flatMap {
      case Some(order) => order.pure[G]
      case None => OrderNotFound(orderId).raiseError[G, Order]
    }
