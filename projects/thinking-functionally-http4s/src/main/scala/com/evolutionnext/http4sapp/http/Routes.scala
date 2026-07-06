package com.evolutionnext.http4sapp.http

import cats.effect.Concurrent
import cats.syntax.all.*
import com.evolutionnext.http4sapp.domain.*
import com.evolutionnext.http4sapp.domain.OrderCommand.*
import com.evolutionnext.http4sapp.http.JsonCodecs.given
import com.evolutionnext.http4sapp.service.*
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl

final case class CreateOrderRequest(customerId: CustomerId)
final case class AddItemRequest(productId: ProductId, quantity: Int)
final case class ErrorResponse(message: String)

object Routes:
  given Codec[CreateOrderRequest] = deriveCodec
  given Codec[AddItemRequest] = deriveCodec
  given Codec[ErrorResponse] = deriveCodec

  def all[F[_]: Concurrent](
      orderService: OrderService[F],
      orderQueryService: OrderQueryService[F],
      customerService: CustomerService[F],
      inventoryService: InventoryService[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*

    def handle[A: io.circe.Encoder](program: F[A]) =
      program.flatMap(Ok(_)).handleErrorWith(error => BadRequest(ErrorResponse(error.getMessage)))

    HttpRoutes.of[F] {
      case GET -> Root / "customers" =>
        handle(customerService.list)

      case GET -> Root / "inventory" =>
        handle(inventoryService.list)

      case request @ POST -> Root / "orders" =>
        request.as[CreateOrderRequest].flatMap(request =>
          handle(orderService.execute(CreateOrder(request.customerId)))
        )

      case GET -> Root / "orders" / LongVar(orderId) =>
        handle(orderQueryService.find(OrderId(orderId)))

      case request @ POST -> Root / "orders" / LongVar(orderId) / "items" =>
        request.as[AddItemRequest].flatMap(request =>
          handle(orderService.execute(AddItem(OrderId(orderId), request.productId, request.quantity)))
        )

      case POST -> Root / "orders" / LongVar(orderId) / "submit" =>
        handle(orderService.execute(SubmitOrder(OrderId(orderId))))
    }
