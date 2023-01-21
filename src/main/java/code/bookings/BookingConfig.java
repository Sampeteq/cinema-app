package code.bookings;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AllArgsConstructor
class BookingConfig {

    private final Clock clock;

    @Bean
    BookingFacade filmFacade(
            FilmRepository filmRepository,
            RoomRepository roomRepository,
            ScreeningRepository screeningRepository,
            BookingRepository bookingRepository
    ) {
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification, filmRepository);
        var filmSearcher = new FilmSearcher(filmRepository);
        var screeningRoomFactory = new RoomFactory(roomRepository);
        var screeningRoomSearcher = new RoomSearcher(roomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                screeningRepository,
                roomRepository,
                filmRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var seatBooker = new Booker(screeningRepository, bookingRepository);
        var seatBookingSearcher = new BookingSearcher(bookingRepository);
        return new BookingFacade(
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
