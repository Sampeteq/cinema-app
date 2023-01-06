package code.films;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class FilmConfig {

    @Bean
    FilmFacade filmAPI(FilmRepository filmRepository) {
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification);
        return new FilmFacade(filmFactory, filmRepository);
    }
}
