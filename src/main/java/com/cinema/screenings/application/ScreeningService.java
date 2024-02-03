package com.cinema.screenings.application;

import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.exceptions.HallNotFoundException;
import com.cinema.screenings.application.dto.CreateScreeningDto;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningFactory;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.infrastructure.ScreeningMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningFactory screeningFactory;
    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    @Transactional
    public void createScreening(CreateScreeningDto dto) {
        log.info("Dto:{}", dto);
        var film = filmRepository
                .findById(dto.filmId())
                .orElseThrow(FilmNotFoundException::new);
        log.info("Gotten film:{}", film);
        var hall = hallRepository
                .findById(dto.hallId())
                .orElseThrow(HallNotFoundException::new);
        log.info("Gotten hall:{}", hall);
        var screening = screeningFactory.createScreening(
                dto.date(),
                film,
                hall
        );
        var addedScreening = screeningRepository.save(screening);
        log.info("Screening added:{}", addedScreening);
    }

    public void deleteScreening(Long id) {
        log.info("Screening id:{}", id);
        var screening = screeningRepository
                .findById(id)
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }

    public List<ScreeningDto> getAllScreenings() {
        return screeningRepository
                .findAll()
                .stream()
                .map(screeningMapper::mapScreeningToDto)
                .toList();
    }

    public List<ScreeningDto> getScreeningsByDate(LocalDate date) {
        return screeningRepository
                .findScreeningsByDateBetween(date.atStartOfDay(), date.atStartOfDay().plusHours(23).plusMinutes(59))
                .stream().map(screeningMapper::mapScreeningToDto)
                .toList();
    }

    public List<ScreeningSeatDto> getSeatsByScreeningId(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        return screeningRepository
                .findByIdWithTickets(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .getTickets()
                .stream()
                .map(ticket -> new ScreeningSeatDto(
                        ticket.getSeat().getId(),
                        ticket.getSeat().getRowNumber(),
                        ticket.getSeat().getNumber(),
                        ticket.isFree()
                ))
                .sorted(Comparator.comparing(ScreeningSeatDto::id))
                .toList();
    }
}
