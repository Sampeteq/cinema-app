package com.cinema;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;

@Configuration
@Profile("test")
public class MockClock {

    private static final Instant CURRENT_DATE = LocalDateTime
            .of(Year.now().getValue(), 12, 13, 16, 30)
            .toInstant(ZoneOffset.UTC);

    @Bean
    public Clock clock() {
        return Clock.fixed(CURRENT_DATE, ZoneOffset.UTC);
    }
}
