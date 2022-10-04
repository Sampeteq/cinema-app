package com.example.screening.domain;

import com.example.film.domain.FilmFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningAPI screeningAPI(ScreeningRepository screeningRepository, FilmFacade filmFacade) {
        return new ScreeningAPI(screeningRepository, filmFacade);
    }

    @Bean
    Year currentYear() {
        return Year.now();
    }
}
