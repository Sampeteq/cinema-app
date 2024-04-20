package com.cinema.screenings.application;

import com.cinema.films.application.FilmService;
import com.cinema.halls.application.HallService;
import com.cinema.screenings.application.dto.ScreeningCreateDto;
import com.cinema.screenings.application.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.application.exceptions.ScreeningsCollisionsException;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.cinema.screenings.domain.ScreeningConstants.MAX_DAYS_BEFORE_SCREENING;
import static com.cinema.screenings.domain.ScreeningConstants.MIN_DAYS_BEFORE_SCREENING;

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
        if (daysDifference < MIN_DAYS_BEFORE_SCREENING || daysDifference > MAX_DAYS_BEFORE_SCREENING) {
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
                UUID.randomUUID(),
                screeningCreateDto.date(),
                screeningEndDate,
                film.getId(),
                hall.getId()
        );
        var addedScreening = screeningRepository.save(screening);
        log.info("Screening added:{}", addedScreening);
        return addedScreening;
    }

    public void deleteScreening(UUID id) {
        log.info("Screening id:{}", id);
        var screening = screeningRepository
                .getById(id)
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }

    public Screening getScreeningById(UUID screeningId) {
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
