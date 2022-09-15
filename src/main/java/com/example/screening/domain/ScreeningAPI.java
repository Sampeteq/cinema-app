package com.example.screening.domain;

import com.example.film.domain.FilmAPI;
import com.example.film.domain.FilmId;
import com.example.film.domain.exception.FilmNotFoundException;
import com.example.screening.domain.dto.AddScreeningDTO;
import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.ScreeningNotFoundException;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import com.example.ticket.domain.exception.WrongTicketAgeException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningAPI {

    private final ScreeningRepository screeningRepository;
    private final FilmAPI filmAPI;

    public ScreeningDTO addScreening(AddScreeningDTO dto, Year currentYear) {
        if (filmAPI.isFilmPresent(FilmId.of(dto.filmId() ) ) ) {
            var screening = new Screening(
                    ScreeningDate.of(dto.date(), currentYear),
                    FreeSeats.of(dto.freeSeats() ),
                    MinAge.of(dto.minAge() ),
                    FilmId.of(dto.filmId() )
            );
            return screeningRepository
                    .save(screening)
                    .toDTO();
        } else {
            throw new FilmNotFoundException(FilmId.of(dto.filmId()));
        }
    }

    public ScreeningDTO readScreeningById(ScreeningId screeningId) {
        return getScreeningOrThrowException(screeningId).toDTO();
    }

    public List<ScreeningDTO> readAllScreenings() {
        return screeningRepository
                .findAll()
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public List<ScreeningDTO> readScreeningsByFilmId(FilmId filmId) {
        return screeningRepository
                .findAllByFilmId(filmId)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public List<ScreeningDTO> readAllScreeningsByDate(ScreeningDate date) {
        return screeningRepository
                .findByDate(date)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public void checkReservationPossibility(ScreeningId screeningId, int age) {
        var screening = getScreeningOrThrowException(screeningId);
        if (!screening.hasFreeSeats()) {
            throw new NoScreeningFreeSeatsException(screeningId);
        }
        if (!screening.isAgeEnough(age)) {
            throw new WrongTicketAgeException(age);
        }
    }

    public void decreaseFreeSeatsByOne(ScreeningId screeningId) {
        var screening = getScreeningOrThrowException(screeningId);
        screening.decreaseFreeSeatsByOne();
    }

    public void checkCancelReservationPossibility(ScreeningId screeningId, LocalDateTime currentDate) {
        var screening = getScreeningOrThrowException(screeningId);
        if (!screening.canCancelReservation(currentDate)) {
            throw new TooLateToCancelTicketReservationException();
        }
    }

    private Screening getScreeningOrThrowException(ScreeningId screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }
}
