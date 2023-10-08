package com.cinema.repertoire.application.services;

import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
import com.cinema.repertoire.domain.events.ScreeningEndedEvent;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Slf4j
class ScreeningEndService {

    private final ScreeningRepository screeningRepository;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    @Transactional
    public void removeRoomsFromEndedScreenings() {
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
