package com.example.film.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmConfig {

    @Bean
    FilmAPI filmAPI(FilmRepository filmRepository) {
        return new FilmAPI(filmRepository);
    }
}
