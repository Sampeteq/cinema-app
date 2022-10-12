package code.screening;

import code.film.FilmFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningFacade screeningAPI(ScreeningRepository screeningRepository,
                                 ScreeningRoomRepository screeningRoomRepository,
                                 FilmFacade filmFacade) {
        return new ScreeningFacade(screeningRepository, screeningRoomRepository, filmFacade);
    }

    @Bean
    Year currentYear() {
        return Year.now();
    }
}
