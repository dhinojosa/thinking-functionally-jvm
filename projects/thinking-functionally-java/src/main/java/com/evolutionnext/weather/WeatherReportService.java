package com.evolutionnext.weather;

import java.util.concurrent.CompletableFuture;

public final class WeatherReportService {
    private WeatherReportService() {
    }

    public static CompletableFuture<Fahrenheit> currentTemperature(String city) {
        return CompletableFuture.completedFuture(
            switch (city) {
                case "Denver" -> new Fahrenheit(91.0);
                case "Chicago" -> new Fahrenheit(63.0);
                default -> new Fahrenheit(68.0);
            });
    }
}
