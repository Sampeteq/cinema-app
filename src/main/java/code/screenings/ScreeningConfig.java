package code.screenings;

import code.films.FilmFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AllArgsConstructor
class ScreeningConfig {

    private final JpaScreeningRepository jpaScreeningRepository;

    private final JpaScreeningRoomRepository jpaScreeningRoomRepository;

    private final JpaScreeningTicketRepository jpaScreeningTicketRepository;

    private final Clock clock;

    @Bean
    ScreeningFacade screeningFacade(
            FilmFacade filmFacade
    ) {
        var screeningRepository = new JpaScreeningRepositoryAdapter(jpaScreeningRepository);
        var screeningRoomRepository = new JpaScreeningRoomRepositoryAdapter(jpaScreeningRoomRepository);
        var ticketRepository = new JpaSeatBookingRepositoryAdapter(jpaScreeningTicketRepository);
        var screeningRoomFactory = new ScreeningRoomFactory(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                filmFacade,
                screeningRepository,
                screeningRoomRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningTicketBooker = new SeatBooker(screeningRepository, ticketRepository);
        return new ScreeningFacade(
                screeningRoomRepository,
                ticketRepository,
                screeningSearcher,
                screeningRoomFactory,
                screeningFactory,
                screeningTicketBooker,
                screeningRepository
        );
    }

}
