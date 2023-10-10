package com.cinema.repertoire.application.services;

import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
import com.cinema.repertoire.domain.events.ScreeningEndedEvent;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class ScreeningSchedulerService {

    private final ScreeningRepository screeningRepository;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    /** 3600000ms - 1h */
    @Scheduled(fixedDelay = 3600000)
    void run() {
        log.info("Searching for ended screenings");
        var endedScreenings = screeningRepository
                .readWithRoom()
                .stream()
                .filter(screening -> screening.isEnded(clock))
                .toList();
        if (endedScreenings.isEmpty()) {
            log.info("Ended screenings not found");
        } else {
            log.info("Found ended screenings:");
            endedScreenings.forEach(screening -> log.info(screening.toString()));
            endedScreenings.forEach(Screening::removeRoom);
            endedScreenings
                    .stream()
                    .map(screening -> new ScreeningEndedEvent(
                            screening.getRoomId(),
                            screening.getDate(),
                            screening.calculateEndDate()
                    )).forEach(eventPublisher::publish);
        }
    }
}
