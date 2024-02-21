package com.cinema.screenings.application;

import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningFactory;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningFactory screeningFactory;
    private final ScreeningRepository screeningRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    @Transactional
    public Screening createScreening(LocalDateTime date, Long filmId, Long hallId) {
        log.info("Date:{}", date);
        log.info("Film id:{}", filmId);
        log.info("Hall id:{}", hallId);
        var film = filmRepository
                .findById(filmId)
                .orElseThrow(FilmNotFoundException::new);
        log.info("Gotten film:{}", film);
        var hall = hallRepository
                .findById(hallId)
                .orElseThrow(HallNotFoundException::new);
        log.info("Gotten hall:{}", hall);
        var screening = screeningFactory.createScreening(
                date,
                film,
                hall
        );
        var addedScreening = screeningRepository.save(screening);
        log.info("Screening added:{}", addedScreening);
        return addedScreening;
    }

    public void deleteScreening(Long id) {
        log.info("Screening id:{}", id);
        var screening = screeningRepository
                .findById(id)
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }

    public Screening getScreeningById(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public List<Screening> getScreeningsByDate(LocalDate date) {
        return screeningRepository.findScreeningsByDateBetween(
                date.atStartOfDay(),
                date.atStartOfDay().plusHours(23).plusMinutes(59)
        );
    }
}
