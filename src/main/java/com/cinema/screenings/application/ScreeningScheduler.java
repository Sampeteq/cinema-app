package com.cinema.screenings.application;

import com.cinema.films.application.queries.handlers.ReadFilmHandler;
import com.cinema.films.application.queries.ReadFilm;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.events.ScreeningEndedEvent;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class ScreeningScheduler {

    private final ScreeningRepository screeningRepository;
    private final ReadFilmHandler readFilmHandler;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    /** 3600000ms - 1h */
    @Scheduled(fixedDelay = 3600000)
    @Transactional
    public void run() {
        log.info("Searching for ended screenings");
        var endedScreenings = searchEndedScreenings();
        if (endedScreenings.isEmpty()) {
            log.info("Ended screenings not found");
        } else {
            log.info("Found ended screenings:");
            endedScreenings.forEach(screening -> log.info(screening.toString()));
            endedScreenings
                    .stream()
                    .map(screening -> {
                        var event = new ScreeningEndedEvent(
                                screening.getDate(),
                                screening.getRoomId()
                        );
                        screening.removeRoom();
                        return event;
                    })
                    .forEach(event -> {
                        log.info(event.toString());
                        eventPublisher.publish(event);
                    });
        }
    }

    private List<Screening> searchEndedScreenings() {
        return screeningRepository
                .readWithRoom()
                .stream()
                .filter(screening -> {
                    var endDate = calculateScreeningEndDate(screening.getDate(), screening.getFilmId());
                    return endDate.isBefore(LocalDateTime.now(clock));
                })
                .toList();
    }

    private LocalDateTime calculateScreeningEndDate(LocalDateTime screeningDate, Long filmId) {
        var query = new ReadFilm(filmId);
        var filmDto = readFilmHandler.handle(query);
        return screeningDate.plusMinutes(filmDto.durationInMinutes());
    }
}
