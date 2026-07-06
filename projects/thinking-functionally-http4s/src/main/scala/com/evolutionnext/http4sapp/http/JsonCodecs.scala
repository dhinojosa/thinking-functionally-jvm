package com.evolutionnext.http4sapp.http

import com.evolutionnext.http4sapp.domain.*
import io.circe.Codec
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.semiauto.deriveCodec

object JsonCodecs:
  given Codec[CustomerId] = Codec.from(
    Decoder.decodeString.map(CustomerId.apply),
    Encoder.encodeString.contramap(_.value)
  )

  given Codec[ProductId] = Codec.from(
    Decoder.decodeString.map(ProductId.apply),
    Encoder.encodeString.contramap(_.value)
  )

  given Codec[OrderId] = Codec.from(
    Decoder.decodeLong.map(OrderId.apply),
    Encoder.encodeLong.contramap(_.value)
  )

  given Codec[Money] = Codec.from(
    Decoder.decodeBigDecimal.map(Money.apply),
    Encoder.encodeBigDecimal.contramap(_.value)
  )

  given Codec[OrderStatus] = Codec.from(
    Decoder.decodeString.emap {
      case "Open" => Right(OrderStatus.Open)
      case "Submitted" => Right(OrderStatus.Submitted)
      case other => Left(s"Unknown order status: $other")
    },
    Encoder.encodeString.contramap(_.toString)
  )

  given Codec[Customer] = deriveCodec
  given Codec[Product] = deriveCodec
  given Codec[OrderItem] = deriveCodec
  given Codec[Order] = deriveCodec
  given Codec[OrderCommandResult.OrderCreated] = deriveCodec
  given Codec[OrderCommandResult.ItemAdded] = deriveCodec
  given Codec[OrderCommandResult.OrderSubmitted] = deriveCodec
  given Codec[OrderCommandResult] = deriveCodec
