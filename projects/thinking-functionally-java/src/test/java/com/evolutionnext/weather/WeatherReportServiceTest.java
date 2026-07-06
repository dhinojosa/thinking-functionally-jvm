package com.evolutionnext.weather;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherReportServiceTest {
    @Test
    void usesThenComposeToFetchTemperatureAfterCity() {
        CompletableFuture<Fahrenheit> result =
            CityService.getCity().thenCompose(WeatherReportService::currentTemperature);

        assertThat(result.join())
            .isEqualTo(new Fahrenheit(72.0));
    }
}
