package com.evolutionnext.sealed.haskelllist;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyListTest {
    @Test
    public void testHaskellListTestAsEmpty() {
        MyList<Integer> integerMyList = new MyNil<>();
    }

    @Test
    void testHaskellListAsOneElement() {
        MyList<Integer> integerMyList = new MyCons<>(1, new MyNil<>());
    }

    /* Not optimized (TCO); Not tail recursive */
    public <A> int length(MyList<A> myList) {
        return switch (myList) {
            case MyNil<A> _ -> 0;
            case MyCons(var _, var tail) -> length(tail) + 1;
        };
    }

    @Test
    void testHaskellLength() {
        MyList<Integer> integerMyList =
            new MyCons<>(1,
                new MyCons<>(2,
                    new MyCons<>(3,
                        new MyCons<>(4,
                            new MyCons<>(5,
                                new MyNil<>())))));
        Assertions.assertThat(length(integerMyList)).isEqualTo(5);
    }

}
