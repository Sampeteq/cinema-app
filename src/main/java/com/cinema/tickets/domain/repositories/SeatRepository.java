package com.cinema.tickets.domain.repositories;

import com.cinema.tickets.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Seat add(Seat seat);
    Optional<Seat> getByScreeningIdRowNumberAndNumber(Long screeningId, int rowNumber, int number);
    List<Seat> getAllByScreeningId(Long screeningId);
}
