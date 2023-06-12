package code.catalog.application;

import code.catalog.application.handlers.ScreeningFinishHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FilmScreeningScheduler {

    private final ScreeningFinishHandler screeningFinishHandler;

    /** 3600000ms - 1h */
    @Scheduled(fixedDelay = 3600000)
    public void run() {
      screeningFinishHandler.handle();
    }
}
