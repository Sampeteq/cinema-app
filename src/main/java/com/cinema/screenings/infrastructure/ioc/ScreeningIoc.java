package com.cinema.screenings.infrastructure.ioc;

import com.cinema.films.application.FilmService;
import com.cinema.halls.application.HallService;
import com.cinema.screenings.domain.ScreeningDatePolicy;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.application.ScreeningService;
import com.cinema.screenings.infrastructure.db.JpaScreeningMapper;
import com.cinema.screenings.infrastructure.db.JpaScreeningRepository;
import com.cinema.screenings.infrastructure.db.JpaScreeningRepositoryAdapter;
import com.cinema.screenings.infrastructure.ui.ScreeningMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ScreeningIoc {

    @Bean
    ScreeningService screeningService(
            ScreeningRepository screeningRepository,
            ScreeningDatePolicy screeningDatePolicy,
            FilmService filmService,
            HallService hallService,
            Clock clock
    ) {
        return new ScreeningService(screeningRepository, screeningDatePolicy, hallService, filmService, clock);
    }

    @Bean
    ScreeningDatePolicy screeningDatePolicy() {
        return new ScreeningDatePolicy();
    }

    @Bean
    ScreeningRepository screeningRepository(
            JpaScreeningRepository jpaScreeningRepository,
            JpaScreeningMapper mapper
    ) {
        return new JpaScreeningRepositoryAdapter(jpaScreeningRepository, mapper);
    }

    @Bean
    JpaScreeningMapper jpaScreeningMapper() {
        return new JpaScreeningMapper();
    }

    @Bean
    ScreeningMapper screeningMapper() {
        return new ScreeningMapper();
    }
}
