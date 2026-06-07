package com.evolutionnext

import cats.syntax.all.*

class SetupSuite extends munit.FunSuite {

  test("cats and munit are wired") {
    assertEquals(1.some.map(_ + 1), 2.some)
  }
}
