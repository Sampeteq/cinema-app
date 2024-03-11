package com.cinema.films.infrastrcture;

import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.FilmService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmConfig {

    @Bean
    FilmService filmService(FilmRepository filmRepository) {
        return new FilmService(filmRepository);
    }

    @Bean
    FilmRepository filmRepository(JpaFilmRepository jpaFilmRepository) {
        return new JpaFilmRepositoryAdapter(jpaFilmRepository);
    }
}
