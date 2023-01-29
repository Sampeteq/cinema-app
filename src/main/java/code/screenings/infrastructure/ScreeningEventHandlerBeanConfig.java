package code.screenings.infrastructure;

import code.screenings.application.ScreeningEventHandler;
import code.screenings.domain.ScreeningRepository;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScreeningEventHandlerBeanConfig {

    @Bean
    ScreeningEventHandler screeningEventHandler(
            ScreeningRepository screeningRepository,
            EventBus eventBus
    ) {
        var eventHandler = new ScreeningEventHandler(screeningRepository);
        eventBus.register(eventHandler);
        return eventHandler;
    }
}
