package code.film;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmConfig {

    @Bean
    FilmFacade filmAPI(FilmRepository filmRepository) {
        return new FilmFacade(filmRepository);
    }
}
