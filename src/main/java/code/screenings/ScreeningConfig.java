package code.screenings;

import code.films.FilmFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
class ScreeningConfig {

    private final JpaScreeningRepository jpaScreeningRepository;

    private final JpaScreeningRoomRepository jpaScreeningRoomRepository;

    private final JpaScreeningTicketRepository jpaScreeningTicketRepository;

    @Bean
    ScreeningFacade screeningFacade(
            FilmFacade filmFacade
    ) {
        var screeningRepository = new JpaScreeningRepositoryAdapter(jpaScreeningRepository);
        var screeningRoomRepository = new JpaScreeningRoomRepositoryAdapter(jpaScreeningRoomRepository);
        var ticketRepository = new JpaScreeningTicketRepositoryAdapter(jpaScreeningTicketRepository);
        var screeningRoomCreator = new ScreeningRoomCreator(screeningRoomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification();
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                filmFacade,
                screeningRepository,
                screeningRoomRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningTicketBooker = new ScreeningTicketBooker(screeningRepository, ticketRepository);
        return new ScreeningFacade(
                screeningRoomRepository,
                ticketRepository,
                screeningSearcher,
                screeningRoomCreator,
                screeningFactory,
                screeningTicketBooker,
                screeningRepository
        );
    }

}
