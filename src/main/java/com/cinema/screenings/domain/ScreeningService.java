package com.cinema.screenings.domain;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final HallService hallService;
    private final FilmService filmService;
    private final Clock clock;

    @Transactional
    public Screening createScreening(ScreeningCreateDto screeningCreateDto) {
        log.info("ScreeningCreateDto:{}", screeningCreateDto);
        var daysDifference = Duration
                .between(LocalDateTime.now(clock), screeningCreateDto.date())
                .abs()
                .toDays();
        if (daysDifference < 7 || daysDifference > 21) {
            throw new ScreeningDateOutOfRangeException();
        }
        var hall = hallService.getHallById(screeningCreateDto.hallId());
        var film = filmService.getFilmById(screeningCreateDto.filmId());
        var screeningEndDate = screeningCreateDto.date().plusMinutes(film.getDurationInMinutes());
        var collisions = screeningRepository.getCollisions(
                screeningCreateDto.date(),
                screeningEndDate,
                screeningCreateDto.hallId()
        );
        if (!collisions.isEmpty()) {
            throw new ScreeningsCollisionsException();
        }
        var screening = new Screening(
                null,
                screeningCreateDto.date(),
                screeningEndDate,
                film.getId(),
                hall.getId()
        );
        var addedScreening = screeningRepository.save(screening);
        log.info("Screening added:{}", addedScreening);
        return addedScreening;
    }

    public void deleteScreening(long id) {
        log.info("Screening id:{}", id);
        var screening = screeningRepository
                .getById(id)
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }

    public Screening getScreeningById(long screeningId) {
        log.info("Screening id:{}", screeningId);
        return screeningRepository
                .getById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.getAll();
    }

    public List<Screening> getScreeningsByDate(LocalDate date) {
        return screeningRepository.getScreeningsByDateBetween(
                date.atStartOfDay(),
                date.atStartOfDay().plusHours(23).plusMinutes(59)
        );
    }
}
