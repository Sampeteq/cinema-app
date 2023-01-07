package code.screenings;

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
            FilmRepository filmRepository
    ) {
        var screeningRoomFactory = new ScreeningRoomFactory(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                filmRepository,
                screeningRepository,
                screeningRoomRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningTicketBooker = new SeatBooker(screeningRepository, seatBookingRepository);
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification);
        return new ScreeningFacade(
                screeningRoomRepository,
                seatBookingRepository,
                screeningSearcher,
                screeningRoomFactory,
                screeningFactory,
                screeningTicketBooker,
                screeningRepository,
                filmFactory,
                filmRepository
        );
    }

}
