ThisBuild / scalaVersion := "3.8.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.evolutionnext"

lazy val http4sVersion = "1.0.0-M45"
lazy val doobieVersion = "1.0.0-RC12"
lazy val circeVersion = "0.14.14"

lazy val root = (project in file("."))
  .settings(
    name := "thinking-functionally-http4s",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.13.0",
      "org.typelevel" %% "cats-effect" % "3.7.0",
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.typelevel" %% "log4cats-slf4j" % "2.8.0",
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "com.disneystreaming" %% "weaver-cats" % "0.8.4" % Test
    ),
    testFrameworks += new TestFramework("weaver.framework.CatsEffect")
  )
