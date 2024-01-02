package com.cinema.screenings.application;

import com.cinema.films.application.FilmService;
import com.cinema.halls.application.HallService;
import com.cinema.screenings.application.dto.CreateScreeningDto;
import com.cinema.screenings.application.dto.GetScreeningsDto;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningFactory;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.infrastructure.ScreeningMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningFactory screeningFactory;
    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final ScreeningSeatRepository screeningSeatRepository;
    private final FilmService filmService;
    private final HallService hallService;
    private final Clock clock;

    @Transactional
    public void createScreening(CreateScreeningDto dto) {
        log.info("Dto:{}", dto);
        var filmDto = filmService.getFilmById(dto.filmId());
        log.info("Gotten film:{}", filmDto);
        var hallDto = hallService.getHallById(dto.hallId());
        log.info("Gotten hall:{}", hallDto);
        var screening = screeningFactory.createScreening(
                dto.date(),
                filmDto.durationInMinutes(),
                dto.filmId(),
                dto.hallId()
        );
        var addedScreening = screeningRepository.add(screening);
        log.info("Screening added:{}", addedScreening);
        var screeningSeats = hallDto
                .seats()
                .stream()
                .map(seatDto -> new ScreeningSeat(seatDto.rowNumber(), seatDto.number(), true, screening.getId()))
                .toList();
        screeningSeats.forEach(screeningSeatRepository::add);
        log.info("Added seats");
    }

    public void deleteScreening(Long id) {
        log.info("Screening id:{}", id);
        var screening = screeningRepository
                .getById(id)
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }

    public long getTimeToScreeningInHours(Long id) {
        log.info("Screening id:{}", id);
        var screeningDate = screeningRepository
                .getById(id)
                .orElseThrow(ScreeningNotFoundException::new)
                .getDate();
        return timeToScreeningInHours(clock, screeningDate);
    }

    public ScreeningDto getScreeningById(Long id) {
        log.info("Screening id:{}", id);
        return screeningRepository
                .getById(id)
                .map(screening -> {
                    var filmDto = filmService.getFilmById(screening.getFilmId());
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .orElseThrow(ScreeningNotFoundException::new);
    }

    public List<ScreeningDto> getScreenings(GetScreeningsDto dto) {
        log.info("Dto:{}", dto);
        return screeningRepository
                .getAll(dto)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screening -> {
                    var filmDto = filmService.getFilmById(screening.getFilmId());
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .toList();
    }

    private long timeToScreeningInHours(Clock clock, LocalDateTime screeningDate) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, screeningDate)
                .abs()
                .toHours();
    }
}
