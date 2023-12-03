package com.cinema.halls.domain;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Seat add(Seat seat);
    Optional<Seat> getByHallIdAndPosition(Long hallId, int rowNumber, int number);
    Optional<Seat> getById(Long seatId);
    List<Seat> getAllByHallId(Long hallId);
}
