package com.evolutionnext

import arrow.core.Some
import arrow.core.some
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SetupTest : FunSpec({

    test("arrow and kotest are wired") {
        1.some().map { it + 1 } shouldBe Some(2)
    }
})
