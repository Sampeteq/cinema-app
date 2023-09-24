package com.cinema.catalog.application.services;

import com.cinema.catalog.domain.ScreeningReadOnlyRepository;
import com.cinema.catalog.domain.events.ScreeningEndedEvent;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
class ScreeningEndService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void removeRoomsFromEndedScreenings() {
        log.info("Searching for ended screenings");
        var endedScreenings = screeningReadOnlyRepository.readEnded();
        if (endedScreenings.isEmpty()) {
            log.info("Ended screenings not found");
        } else {
            log.info("Found ended screenings:");
            endedScreenings.forEach(screening -> log.info(screening.toString()));
//            endedScreenings.forEach(Screening::removeRoom);
            endedScreenings
                    .stream()
                    .map(screening -> new ScreeningEndedEvent(
                            screening.getRoomCustomId(),
                            screening.getDate(),
                            screening.getEndDate()
                    )).forEach(eventPublisher::publish);
        }
    }
}