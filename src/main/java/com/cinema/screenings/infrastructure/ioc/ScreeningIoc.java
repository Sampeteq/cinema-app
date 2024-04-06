package com.cinema.screenings.infrastructure.ioc;

import com.cinema.films.domain.FilmService;
import com.cinema.films.infrastrcture.db.JpaFilmMapper;
import com.cinema.halls.domain.HallService;
import com.cinema.halls.infrastructure.db.JpaHallMapper;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningService;
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
            FilmService filmService,
            HallService hallService,
            Clock clock
    ) {
        return new ScreeningService(screeningRepository, hallService, filmService, clock);
    }

    @Bean
    ScreeningRepository screeningRepository(
            JpaScreeningRepository jpaScreeningRepository,
            JpaScreeningMapper mapper
    ) {
        return new JpaScreeningRepositoryAdapter(jpaScreeningRepository, mapper);
    }

    @Bean
    JpaScreeningMapper jpaScreeningMapper(JpaFilmMapper jpaFilmMapper, JpaHallMapper jpaHallMapper) {
        return new JpaScreeningMapper(jpaFilmMapper, jpaHallMapper);
    }

    @Bean
    ScreeningMapper screeningMapper() {
        return new ScreeningMapper();
    }
}
