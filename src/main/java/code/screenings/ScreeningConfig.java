package code.screenings;

import code.films.FilmFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningFacade screeningAPI(ScreeningRepository screeningRepository,
                                 ScreeningRoomRepository screeningRoomRepository,
                                 ScreeningTicketRepository screeningTicketRepository,
                                 FilmFacade filmFacade) {
        return new ScreeningFacade(screeningRepository, screeningRoomRepository, screeningTicketRepository, filmFacade);
    }

    @Bean
    Year currentYear() {
        return Year.now();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
