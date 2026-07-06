package com.evolutionnext.http4sapp

import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.*
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import com.evolutionnext.http4sapp.db.Database
import com.evolutionnext.http4sapp.http.Routes
import com.evolutionnext.http4sapp.repository.*
import com.evolutionnext.http4sapp.service.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

object Main extends IOApp.Simple:
  given LoggerFactory[IO] = Slf4jFactory.create[IO]

  def run: IO[Unit] =
    Database.transactor[IO](Database.Config()).use { xa =>
      val transaction = xa.trans

      val customerRepository = DoobieCustomerRepository()
      val inventoryRepository = DoobieInventoryRepository()
      val orderRepository = DoobieOrderRepository()

      val customerService = CustomerService[IO](customerRepository, transaction)
      val inventoryService = InventoryService[IO](inventoryRepository, transaction)
      val orderService = OrderService[IO](
        orderRepository,
        customerRepository,
        inventoryRepository,
        transaction
      )
      val orderQueryService = OrderQueryService[IO](orderRepository, transaction)

      val routes = Router(
        "/api" -> Routes.all[IO](orderService, orderQueryService, customerService, inventoryService)
      ).orNotFound

      for
        _ <- EmberServerBuilder
          .default[IO]
          .withHost(Host.fromString("0.0.0.0").get)
          .withPort(Port.fromInt(sys.env.get("PORT").flatMap(_.toIntOption).getOrElse(8080)).get)
          .withHttpApp(routes)
          .build
          .useForever
      yield ()
    }
