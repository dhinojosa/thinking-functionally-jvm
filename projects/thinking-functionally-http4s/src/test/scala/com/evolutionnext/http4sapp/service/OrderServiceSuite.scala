package com.evolutionnext.http4sapp.service

import cats.arrow.FunctionK
import cats.effect.IO
import cats.effect.Ref
import cats.syntax.all.*
import com.evolutionnext.http4sapp.domain.*
import com.evolutionnext.http4sapp.domain.OrderCommand.*
import com.evolutionnext.http4sapp.domain.OrderCommandResult.*
import com.evolutionnext.http4sapp.domain.OrderError.*
import com.evolutionnext.http4sapp.repository.*
import weaver.SimpleIOSuite

object OrderServiceSuite extends SimpleIOSuite:
  private val customer = Customer(CustomerId("cust-1"), "Ada Lovelace", Money(BigDecimal(100)))
  private val product = Product(ProductId("prod-1"), "Keyboard", Money(BigDecimal(40)), 10)
  private val orderId = OrderId(1)

  test("service can be tested with IO repositories instead of ConnectionIO") {
    for
      state <- IO.ref(TestState())
      service = OrderService[IO, IO](
        TestOrderRepository(state),
        TestCustomerRepository,
        TestInventoryRepository(state),
        FunctionK.id[IO]
      )
      created <- service.execute(CreateOrder(customer.id))
      added <- service.execute(AddItem(orderId, product.id, 2))
      submitted <- service.execute(SubmitOrder(orderId))
      finalState <- state.get
    yield
      expect(created == OrderCreated(orderId)) &&
        expect(added == ItemAdded(orderId, product.id)) &&
        expect(submitted == OrderSubmitted(orderId)) &&
        expect(finalState.order.exists(_.status == OrderStatus.Submitted)) &&
        expect(finalState.reserved == 2)
  }

  test("credit limit failures stay in the error channel") {
    for
      state <- IO.ref(TestState())
      service = OrderService[IO, IO](
        TestOrderRepository(state),
        TestCustomerRepository,
        TestInventoryRepository(state),
        FunctionK.id[IO]
      )
      _ <- service.execute(CreateOrder(customer.id))
      result <- service.execute(AddItem(orderId, product.id, 3)).attempt
    yield expect(result.left.exists {
      case CreditLimitExceeded(_, _, _) => true
      case _ => false
    })
  }

  private final case class TestState(
      order: Option[Order] = None,
      reserved: Int = 0
  )

  private object TestCustomerRepository extends CustomerRepository[IO]:
    def find(customerId: CustomerId): IO[Option[Customer]] =
      IO.pure(Option.when(customerId == customer.id)(customer))

    def list: IO[List[Customer]] =
      IO.pure(List(customer))

  private final class TestInventoryRepository(state: Ref[IO, TestState]) extends InventoryRepository[IO]:
    def find(productId: ProductId): IO[Option[Product]] =
      IO.pure(Option.when(productId == product.id)(product))

    def list: IO[List[Product]] =
      IO.pure(List(product))

    def reserve(productId: ProductId, quantity: Int): IO[Int] =
      if productId == product.id && quantity <= product.quantityAvailable then
        state.update(s => s.copy(reserved = s.reserved + quantity)).as(1)
      else IO.pure(0)

  private final class TestOrderRepository(state: Ref[IO, TestState]) extends OrderRepository[IO]:
    def create(customerId: CustomerId): IO[OrderId] =
      state
        .update(s => s.copy(order = Some(Order(orderId, customerId, OrderStatus.Open, List.empty))))
        .as(orderId)

    def find(orderId: OrderId): IO[Option[Order]] =
      state.get.map(_.order.filter(_.id == orderId))

    def addItem(orderId: OrderId, product: Product, quantity: Int): IO[Unit] =
      state.update { s =>
        val item = OrderItem(product.id, product.name, quantity, product.price)
        s.copy(order = s.order.map(order => order.copy(items = order.items :+ item)))
      }

    def submit(orderId: OrderId): IO[Unit] =
      state.update(s =>
        s.copy(order = s.order.map(order => order.copy(status = OrderStatus.Submitted)))
      )
