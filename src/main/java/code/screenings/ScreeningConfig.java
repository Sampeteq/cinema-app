package code.screenings;

import com.google.common.eventbus.EventBus;
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
            FilmRepository filmRepository,
            RoomRepository roomRepository,
            ScreeningRepository screeningRepository,
            EventBus eventBus
    ) {
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification, filmRepository);
        var filmSearcher = new FilmSearcher(filmRepository);
        var roomFactory = new RoomFactory(roomRepository);
        var roomSearcher = new RoomSearcher(roomRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                screeningRepository,
                roomRepository,
                filmRepository
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningEventHandler = new ScreeningEventHandler(screeningRepository);
        var screeningFacade = new ScreeningFacade(
                filmFactory,
                filmSearcher,
                roomFactory,
                roomSearcher,
                screeningFactory,
                screeningSearcher,
                screeningEventHandler
        );
        eventBus.register(screeningFacade);
        return screeningFacade;
    }
}
