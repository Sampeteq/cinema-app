package com.cinema.shared.time;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@Profile("prod")
class ProdTimeProvider implements TimeProvider {

    @Override
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now(Clock.systemUTC());
    }
}
