package com.cinema.shared.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ClockBean {

    @Bean
    Clock prodClock() {
        return Clock.systemUTC();
    }
}
