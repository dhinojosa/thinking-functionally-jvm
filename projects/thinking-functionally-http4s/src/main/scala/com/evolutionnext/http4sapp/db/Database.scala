package com.evolutionnext.http4sapp.db

import cats.effect.Async
import cats.effect.Resource
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor

object Database:
  final case class Config(
      url: String = sys.env.getOrElse("DATABASE_URL", "jdbc:postgresql://localhost:15432/orders"),
      user: String = sys.env.getOrElse("DATABASE_USERNAME", "postgres"),
      password: String = sys.env.getOrElse("DATABASE_PASSWORD", "postgres")
  )

  def transactor[F[_]: Async](config: Config): Resource[F, HikariTransactor[F]] =
    for
      connectEc <- ExecutionContexts.fixedThreadPool[F](8)
      transactor <- HikariTransactor.newHikariTransactor[F](
        "org.postgresql.Driver",
        config.url,
        config.user,
        config.password,
        connectEc
      )
    yield transactor
