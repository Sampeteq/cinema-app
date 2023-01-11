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
            ScreeningRepository screeningRepository,
            ScreeningRoomRepository screeningRoomRepository,
            SeatBookingRepository seatBookingRepository,
            FilmFacade filmFacade
    ) {
        var screeningRoomFactory = new ScreeningRoomFactory(screeningRoomRepository);
        var screeningRoomSearcher = new ScreeningRoomSearcher(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                screeningRepository,
                screeningRoomRepository,
                filmFacade
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);

        var seatBooker = new SeatBooker(screeningRepository, seatBookingRepository);
        var seatBookingSearcher = new SeatBookingSearcher(seatBookingRepository);

        return new ScreeningFacade(
                screeningRoomFactory,
                screeningRoomSearcher,
                screeningFactory,
                screeningSearcher,
                seatBooker,
                seatBookingSearcher
        );
    }

}
