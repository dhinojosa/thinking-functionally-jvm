package com.evolutionnext.weather;

import java.util.concurrent.CompletableFuture;

public final class CityService {
    public static CompletableFuture<String> getCity() {
        return CompletableFuture.completedFuture("Denver");
    }
}
