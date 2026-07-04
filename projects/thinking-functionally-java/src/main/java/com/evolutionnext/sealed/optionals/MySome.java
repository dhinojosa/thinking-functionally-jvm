package com.evolutionnext.sealed.optionals;

import java.util.Objects;
import java.util.StringJoiner;

public record MySome<T> (T value) implements MyOption<T> {
    public MySome {
        Objects.requireNonNull(value, "Cannot be null");
    }
}
