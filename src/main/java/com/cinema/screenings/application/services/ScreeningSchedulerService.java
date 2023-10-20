package com.cinema.screenings.application.services;

import com.cinema.films.application.services.FilmService;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.events.ScreeningEndedEvent;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class ScreeningSchedulerService {

    private final ScreeningRepository screeningRepository;
    private final FilmService filmService;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    /** 3600000ms - 1h */
    @Scheduled(fixedDelay = 3600000)
    public void run() {
        log.info("Searching for ended screenings");
        var endedScreenings = screeningRepository
                .readWithRoom()
                .stream()
                .filter(screening -> {
                    var filmDurationInMinutes = filmService.readFilmDurationInMinutes(screening.getFilmTitle());
                    var endDate = screening.getDate().plusMinutes(filmDurationInMinutes);
                    return endDate.isBefore(LocalDateTime.now(clock));
                })
                .toList();
        if (endedScreenings.isEmpty()) {
            log.info("Ended screenings not found");
        } else {
            log.info("Found ended screenings:");
            endedScreenings.forEach(screening -> log.info(screening.toString()));
            endedScreenings.forEach(Screening::removeRoom);
            endedScreenings
                    .stream()
                    .map(screening -> {
                        var filmDurationInMinutes = filmService.readFilmDurationInMinutes(screening.getFilmTitle());
                        var endDate = screening.getDate().plusMinutes(filmDurationInMinutes);
                        return new ScreeningEndedEvent(
                                screening.getRoomId(),
                                screening.getDate(),
                                endDate
                        );
                    }).forEach(eventPublisher::publish);
        }
    }
}
