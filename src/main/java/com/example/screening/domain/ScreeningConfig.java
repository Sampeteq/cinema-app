package com.example.screening.domain;

import com.example.film.domain.FilmAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningAPI screeningAPI(ScreeningRepository screeningRepository, FilmAPI filmAPI) {
        return new ScreeningAPI(screeningRepository, filmAPI);
    }

    @Bean
    Year currentYear() {
        return Year.now();
    }
}
