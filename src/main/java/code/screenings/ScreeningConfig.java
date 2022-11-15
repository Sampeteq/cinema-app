package code.screenings;

import code.films.FilmFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningFacade screeningAPI(ScreeningRepository screeningRepository,
                                 ScreeningRoomRepository screeningRoomRepository,
                                 ScreeningTicketRepository screeningTicketRepository,
                                 FilmFacade filmFacade) {
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        return new ScreeningFacade(
                screeningRepository,
                screeningRoomRepository,
                screeningTicketRepository,
                screeningSearcher,
                filmFacade
        );
    }

}
