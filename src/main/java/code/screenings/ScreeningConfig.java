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
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification);
        var screeningRoomFactory = new ScreeningRoomFactory(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                filmRepository,
                screeningRepository,
                screeningRoomRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var seatBooker = new SeatBooker(screeningRepository, seatBookingRepository);
        return new ScreeningFacade(
                filmFactory,
                filmRepository,
                screeningRoomFactory,
                screeningRoomRepository,
                screeningFactory,
                screeningRepository,
                seatBookingRepository,
                screeningSearcher,
                seatBooker
        );
    }

}
