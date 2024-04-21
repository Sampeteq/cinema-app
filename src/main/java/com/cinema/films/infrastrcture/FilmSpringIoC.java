package com.cinema.films.infrastrcture;

import com.cinema.films.domain.FilmRepository;
import com.cinema.films.application.FilmService;
import com.cinema.films.infrastrcture.db.JpaFilmMapper;
import com.cinema.films.infrastrcture.db.JpaFilmRepository;
import com.cinema.films.infrastrcture.db.JpaFilmRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmSpringIoC {

    @Bean
    FilmService filmService(FilmRepository filmRepository) {
        return new FilmService(filmRepository);
    }

    @Bean
    FilmRepository filmRepository(JpaFilmRepository jpaFilmRepository, JpaFilmMapper jpaFilmMapper) {
        return new JpaFilmRepositoryAdapter(jpaFilmRepository, jpaFilmMapper);
    }

    @Bean
    JpaFilmMapper jpaFilmMapper() {
        return new JpaFilmMapper();
    }
}
