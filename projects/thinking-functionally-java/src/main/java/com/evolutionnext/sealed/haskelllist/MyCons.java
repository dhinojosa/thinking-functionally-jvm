package com.evolutionnext.sealed.haskelllist;

public record MyCons<A>(A a, MyList<A> rest) implements MyList<A> {
}
