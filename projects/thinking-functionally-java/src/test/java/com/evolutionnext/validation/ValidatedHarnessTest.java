package com.evolutionnext.validation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Workshop exercise: create Validated and make these tests pass")
class ValidatedHarnessTest {
    record Customer(String name, String email, int age) {}

    @Test
    void validMapTransformsTheValue() {
        var result = Validated
            .valid("Ada")
            .map(String::toUpperCase);

        assertThat(result).isEqualTo(Validated.valid("ADA"));
    }

    @Test
    void invalidMapKeepsTheErrors() {
        var result = Validated
            .<String>invalid("Name cannot be blank")
            .map(String::toUpperCase);

        assertThat(result).isEqualTo(
            Validated.invalid("Name cannot be blank"));
    }

    @Test
    void validValuesMapToCustomer() {
        var name = Validated.valid("Ada");
        var email = Validated.valid("ada@example.com");
        var age = Validated.valid(36);

        var result = Validated
            .combine(name, email, age)
            .map(Customer::new);

        assertThat(result).isEqualTo(
            Validated.valid(new Customer("Ada", "ada@example.com", 36)));
    }

    @Test
    void invalidValuesAccumulateErrors() {
        var name = Validated.<String>invalid("Name cannot be blank");
        var email = Validated.<String>invalid("Email must contain @");
        var age = Validated.valid(36);

        var result = Validated
            .combine(name, email, age)
            .map(Customer::new);

        assertThat(result).isEqualTo(
            Validated.invalid(List.of(
                "Name cannot be blank",
                "Email must contain @")));
    }
}
