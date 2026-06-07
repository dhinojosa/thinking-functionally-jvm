package com.evolutionnext;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SetupTest {

    @Test
    void junitAndAssertJAreWired() {
        assertThat(1 + 1).isEqualTo(2);
    }
}
