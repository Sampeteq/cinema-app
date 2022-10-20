package code.screenings;

import code.films.FilmFacade;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Year;

@Configuration
class ScreeningConfig {

    @Bean
    ScreeningFacade screeningAPI(ScreeningRepository screeningRepository,
                                 ScreeningRoomRepository screeningRoomRepository,
                                 FilmFacade filmFacade,
                                 EventBus eventBus) {
        var screeningEventHandler = new ScreeningEventHandler(screeningRepository);
        eventBus.register(screeningEventHandler);
        return new ScreeningFacade(screeningRepository, screeningRoomRepository, filmFacade);
    }

    @Bean
    Year currentYear() {
        return Year.now();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
