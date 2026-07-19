package com.evolutionnext.validation;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        Validated<Customer> result = Validated
            .combine(name, email, age)
            .map(Customer::new);

        assertThat(result).isEqualTo(
            Validated.valid(new Customer("Ada", "ada@example.com", 36)));
    }

    @Test
    void invalidValuesAccumulateErrors() {
        var name = validateName("");
        var email = validateEmailFailure("N/A");
        var ageValid = validateAge(36);

        Validated<Customer> result = Validated
            .combine(name, email, ageValid)
            .map(Customer::new);

        assertThat(result).isEqualTo(
            Validated.invalid(List.of(
                "Name cannot be blank",
                "Email must contain @")));
    }

    private static @NonNull Validated<Integer> validateAge(int age) {
        return Validated.valid(age);
    }

    private static @NonNull Validated<String> validateEmailFailure(String email) {
        return Validated.<String>invalid("Email must contain @");
    }

    private static @NonNull Validated<String> validateName(String name) {
        if (name.isEmpty()) {
            return Validated.invalid("Name cannot be blank");
        }
        return Validated.valid(name);
    }
}
