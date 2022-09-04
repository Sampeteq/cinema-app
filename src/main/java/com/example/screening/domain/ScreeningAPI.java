package com.example.screening.domain;

import com.example.film.domain.FilmAPI;
import com.example.film.domain.exception.FilmNotFoundException;
import com.example.screening.domain.dto.AddScreeningDTO;
import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import com.example.screening.domain.exception.ScreeningNotFoundException;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import com.example.ticket.domain.exception.WrongTicketAgeException;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningAPI {

    private final ScreeningRepository screeningRepository;
    private final FilmAPI filmAPI;

    public ScreeningDTO addScreening(AddScreeningDTO cmd) {
        if (filmAPI.isFilmPresent(cmd.filmId() ) ) {
            return screeningRepository.save(
                    new Screening(
                    cmd.shownAt(),
                    cmd.freeSeats(),
                    cmd.minAge(),
                    cmd.filmId()
                    )
            ).toDTO();
        } else {
            throw new FilmNotFoundException(cmd.filmId() );
        }
    }

    public ScreeningDTO readScreeningById(UUID screeningId) {
        return getScreeningOrThrowException(screeningId).toDTO();
    }

    public List<ScreeningDTO> readAllScreenings() {
        return screeningRepository
                .findAll()
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public List<ScreeningDTO> readScreeningsByFilmId(UUID filmId) {
        return screeningRepository
                .findAllByFilmId(filmId)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public void decreaseFreeSeatsByOne(UUID screeningId) {
        var screening = getScreeningOrThrowException(screeningId);
        screening.decreaseFreeSeatsByOne();
    }

    public void checkReservePossibility(UUID screeningId, int age) {
        var screening= screeningRepository
                .findById(screeningId)
                .orElseThrow( () -> new ScreeningNotFoundException(screeningId) );
        if (!screening.hasFreeSeats() ) {
            throw new NoScreeningFreeSeatsException(screeningId);
        }
        if (!screening.isAgeEnough(age ) ) {
            throw new WrongTicketAgeException(age);
        }
    }

    public void checkCancelPossibility(UUID screeningId, LocalDateTime currentDate) {
        var screening= screeningRepository
                .findById(screeningId )
                .orElseThrow( () -> new ScreeningNotFoundException(screeningId) );
        if (Duration.between(currentDate, screening.getShownAt() ).toHours() < 24) {
            throw new TooLateToCancelTicketReservationException();
        }
    }

    private Screening getScreeningOrThrowException(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow( () -> new ScreeningNotFoundException(screeningId) );
    }

    public List<ScreeningDTO> readAllScreeningsByDate(LocalDateTime date) {
        return screeningRepository
                .findAllByShownAt(date)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }
}
