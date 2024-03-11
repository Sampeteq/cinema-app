package com.cinema.screenings.infrastructure;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.ScreeningDatePolicy;
import com.cinema.screenings.domain.ScreeningFactory;
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
        var screeningDatePolicy = new ScreeningDatePolicy(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDatePolicy,
                screeningRepository,
                filmService,
                hallService
        );
        return new ScreeningService(screeningFactory, screeningRepository);
    }

    @Bean
    ScreeningRepository screeningRepository(JpaScreeningRepository jpaScreeningRepository) {
        return new JpaScreeningRepositoryAdapter(jpaScreeningRepository);
    }
}
