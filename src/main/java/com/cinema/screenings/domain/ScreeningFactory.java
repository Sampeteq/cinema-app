package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import com.cinema.tickets.domain.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScreeningFactory {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;

    public Screening createScreening(LocalDateTime date, Film film, Hall hall) {
        screeningDatePolicy.checkScreeningDate(date);
        var endDate = date.plusMinutes(film.getDurationInMinutes());
        var start = date.toLocalDate().atStartOfDay();
        var end = start.plusHours(24).minusMinutes(1);
        var isCollision = screeningRepository
                .findByHallIdAndDateBetween(hall.getId(), start, end)
                .stream()
                .anyMatch(screening -> screening.collide(date, endDate));
        if (isCollision) {
            throw new ScreeningsCollisionsException();
        }
        var screening = new Screening(date, film, hall);
        var screeningSeats = hall
                .getSeats()
                .stream()
                .map(seat -> new Ticket(screening, seat))
                .toList();
        screening.assignTickets(screeningSeats);
        return screening;
    }
}
