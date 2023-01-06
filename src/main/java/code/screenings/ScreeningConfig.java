package code.screenings;

import code.films.FilmFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AllArgsConstructor
class ScreeningConfig {

    private final Clock clock;

    @Bean
    ScreeningFacade screeningFacade(
            FilmFacade filmFacade,
            ScreeningRepository screeningRepository,
            ScreeningRoomRepository screeningRoomRepository,
            SeatBookingRepository seatBookingRepository
    ) {
        var screeningRoomFactory = new ScreeningRoomFactory(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                filmFacade,
                screeningRepository,
                screeningRoomRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningTicketBooker = new SeatBooker(screeningRepository, seatBookingRepository);
        return new ScreeningFacade(
                screeningRoomRepository,
                seatBookingRepository,
                screeningSearcher,
                screeningRoomFactory,
                screeningFactory,
                screeningTicketBooker,
                screeningRepository
        );
    }

}
