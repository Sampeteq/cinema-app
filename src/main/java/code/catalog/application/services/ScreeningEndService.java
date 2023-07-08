package code.catalog.application.services;

import code.catalog.domain.Screening;
import code.catalog.infrastructure.db.ScreeningReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningEndService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    @Transactional
    public void removeRoomsFromEndedScreenings() {
        log.info("Searching for ended screenings");
        var endedScreenings = screeningReadOnlyRepository.findEnded();
        if (endedScreenings.isEmpty()) {
            log.info("Ended screenings not found");
        } else {
            log.info("Found ended screenings:");
            endedScreenings.forEach(screening -> log.info(screening.toString()));
            endedScreenings.forEach(Screening::removeRoom);
        }
    }
}
