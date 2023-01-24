package code.screenings;

import code.rooms.RoomFacade;
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
            ScreeningRepository screeningRepository,
            FilmRepository filmRepository,
            RoomFacade roomFacade,
            EventBus eventBus
    ) {
        var filmYearSpecification = new PreviousCurrentOrNextOneFilmYearSpecification();
        var filmFactory = new FilmFactory(filmYearSpecification, filmRepository);
        var filmSearcher = new FilmSearcher(filmRepository);
        var screeningDateSpecification = new CurrentOrNextOneYearScreeningDateSpecification(clock);
        var screeningFactory = new ScreeningFactory(
                screeningDateSpecification,
                screeningRepository,
                filmRepository,
                roomFacade
        );
        var screeningSearcher = new ScreeningSearcher(screeningRepository);
        var screeningEventHandler = new ScreeningEventHandler(screeningRepository);
        var screeningFacade = new ScreeningFacade(
                filmFactory,
                filmSearcher,
                screeningFactory,
                screeningSearcher,
                screeningEventHandler
        );
        eventBus.register(screeningFacade);
        return screeningFacade;
    }
}
