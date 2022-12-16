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
        var screeningCreator = new ScreeningCreator(
                screeningRepository,
                screeningRoomRepository,
                filmFacade
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningTicketBooker = new ScreeningTicketBooker(screeningRepository, ticketRepository);
        return new ScreeningFacade(
                screeningRoomRepository,
                ticketRepository,
                screeningSearcher,
                screeningRoomCreator,
                screeningCreator,
                screeningTicketBooker
        );
    }

}
