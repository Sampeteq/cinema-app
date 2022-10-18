package code.screening;

import code.film.FilmFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningFacade screeningAPI(ScreeningRepository screeningRepository,
                                 ScreeningRoomRepository screeningRoomRepository,
                                 TicketRepository ticketRepository,
                                 FilmFacade filmFacade) {
        return new ScreeningFacade(screeningRepository, screeningRoomRepository, ticketRepository, filmFacade);
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
