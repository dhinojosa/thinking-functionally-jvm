package com.evolutionnext.validation;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

public sealed interface Validated<T>
    permits Validated.Valid, Validated.Invalid {

    static <T> Validated<T> valid(T value) {
        return new Valid<>(value);
    }

    static <T> Validated<T> invalid(String error) {
        return new Invalid<>(List.of(error));
    }

    static <T> Validated<T> invalid(List<String> errors) {
        return new Invalid<>(errors);
    }

    static <A, B, C> Builder3<A, B, C> combine(
        Validated<A> a,
        Validated<B> b,
        Validated<C> c
    ) {
        return new Builder3<>(a, b, c);
    }

    <U> Validated<U> map(Function<T, U> f);

    record Valid<T>(T value) implements Validated<T> {
        @Override
        public <U> Validated<U> map(Function<T, U> f) {
            return new Valid<>(f.apply(value));
        }
    }

    record Invalid<T>(List<String> errors) implements Validated<T> {
        @Override
        public <U> Validated<U> map(Function<T, U> f) {
            return new Invalid<>(errors);
        }
    }

    record Builder3<A, B, C>(
        Validated<A> a,
        Validated<B> b,
        Validated<C> c
    ) {
        public <D> Validated<D> map(TriFunction<A, B, C, D> f) {
            var errors = new ArrayList<String>();

            if (a instanceof Invalid<A> invalid) {
                errors.addAll(invalid.errors());
            }

            if (b instanceof Invalid<B> invalid) {
                errors.addAll(invalid.errors());
            }

            if (c instanceof Invalid<C> invalid) {
                errors.addAll(invalid.errors());
            }

            if (!errors.isEmpty()) {
                return Validated.invalid(errors);
            }

            var validA = (Valid<A>) a;
            var validB = (Valid<B>) b;
            var validC = (Valid<C>) c;

            return Validated.valid(
                f.apply(validA.value(), validB.value(), validC.value()));
        }
    }

    @FunctionalInterface
    interface TriFunction<A, B, C, D> {
        D apply(A a, B b, C c);
    }
}
