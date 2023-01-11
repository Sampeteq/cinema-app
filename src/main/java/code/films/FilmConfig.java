package code.films;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmConfig {

    @Bean
    FilmFacade filmFacade(FilmRepository filmRepository) {
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification, filmRepository);
        var filmSearcher = new FilmSearcher(filmRepository);
        return new FilmFacade(filmFactory, filmSearcher);
    }
}
