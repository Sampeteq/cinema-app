package com.cinema.tickets.domain.repositories;

import com.cinema.tickets.domain.Seat;
import io.netty.util.AsyncMapping;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Seat add(Seat seat);
    Optional<Seat> getById(Long id);
    List<Seat> getAllByScreeningId(Long screeningId);
}
