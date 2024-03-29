package com.cinema.screenings.infrastructure;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningService screeningService(
            ScreeningRepository screeningRepository,
            FilmService filmService,
            HallService hallService,
            Clock clock
    ) {
        return new ScreeningService(
                screeningRepository,
                hallService,
                filmService,
                clock
        );
    }

    @Bean
    ScreeningRepository screeningRepository(ScreeningJpaRepository screeningJpaRepository) {
        return new ScreeningJpaRepositoryAdapter(screeningJpaRepository);
    }

    @Bean
    ScreeningMapper screeningMapper() {
        return new ScreeningMapper();
    }
}
