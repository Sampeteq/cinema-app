package com.cinema.shared.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Configuration
@Profile("prod")
class ClockBean {

    @Bean
    Clock prodClock() {
        return Clock.systemUTC();
    }
}
