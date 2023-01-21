package code.films;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AllArgsConstructor
class FilmConfig {

    private final Clock clock;

    @Bean
    FilmFacade filmFacade(
            FilmRepository filmRepository,
            ScreeningRoomRepository screeningRoomRepository,
            ScreeningRepository screeningRepository,
            SeatBookingRepository seatBookingRepository
    ) {
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification, filmRepository);
        var filmSearcher = new FilmSearcher(filmRepository);
        var screeningRoomFactory = new ScreeningRoomFactory(screeningRoomRepository);
        var screeningRoomSearcher = new ScreeningRoomSearcher(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                screeningRepository,
                screeningRoomRepository,
                filmRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var seatBooker = new SeatBooker(screeningRepository, seatBookingRepository);
        var seatBookingSearcher = new SeatBookingSearcher(seatBookingRepository);
        return new FilmFacade(
                filmFactory,
                filmSearcher,
                screeningRoomFactory,
                screeningRoomSearcher,
                screeningFactory,
                screeningSearcher,
                seatBooker,
                seatBookingSearcher
        );
    }
}
