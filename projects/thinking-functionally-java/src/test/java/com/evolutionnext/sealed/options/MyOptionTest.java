package com.evolutionnext.sealed.options;


import com.evolutionnext.sealed.optionals.MyNone;
import com.evolutionnext.sealed.optionals.MyOption;
import com.evolutionnext.sealed.optionals.MySome;
import org.junit.jupiter.api.Test;

public class MyOptionTest {

    public <T> String toString(MyOption<T> o) {
        return switch (o) {
            case MySome(var value) -> value.toString();
            case MyNone _ -> "Empty";
        };
    }


    @Test
    void patternMatchWithSealedClass() {
        String result = toString(new MySome<>(40));
        System.out.println(result);
    }
}
