package code.films.application.handlers;

import code.films.domain.FilmScreening;
import code.films.infrastructure.db.FilmScreeningRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmScreeningFinishHandler {

    private final FilmScreeningRoomRepository roomRepository;
    private final Clock clock;

    @Transactional
    public void handle() {
        log.info("Searching for finished screenings");
        var finishedScreenings = readFinishedScreenings();
        if (finishedScreenings.isEmpty()) {
            log.info("Finished screenings not found");
        } else {
            log.info("Found finished screenings:");
            finishedScreenings.forEach(screening -> log.info(screening.toString()));
            finishedScreenings.forEach(FilmScreening::finish);
        }
    }

    private List<FilmScreening> readFinishedScreenings() {
        return roomRepository
                .readAll()
                .stream()
                .flatMap(room -> room
                        .getScreenings()
                        .stream()
                        .filter(screening -> screening.isFinished(clock))
                ).toList();
    }
}
