package code.films;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class FilmConfig {

    private final JpaFilmRepository jpaFilmRepository;

    @Bean
    FilmFacade filmAPI() {
        var filmRepository = new JpaFilmRepositoryAdapter(jpaFilmRepository);
        return new FilmFacade(filmRepository);
    }
}
