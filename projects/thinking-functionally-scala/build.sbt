ThisBuild / scalaVersion := "3.8.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.thinkingfunctionally"

lazy val root = (project in file("."))
  .settings(
    name := "thinking-functionally-scala",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.13.0",
      "org.typelevel" %% "cats-effect" % "3.7.0",
      "org.scalameta" %% "munit" % "1.3.2" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
