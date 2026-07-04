package com.evolutionnext.sealed.optionals;

public sealed interface MyOption<T> permits MyNone, MySome {
}
