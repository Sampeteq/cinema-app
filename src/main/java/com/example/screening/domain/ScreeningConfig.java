package com.example.screening.domain;

import com.example.film.domain.FilmFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningFacade screeningAPI(ScreeningRepository screeningRepository, FilmFacade filmFacade) {
        return new ScreeningFacade(screeningRepository, filmFacade);
    }

    @Bean
    Year currentYear() {
        return Year.now();
    }
}
