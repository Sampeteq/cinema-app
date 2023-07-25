package code.bookings.domain.ports;

import code.bookings.domain.Seat;

import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> readById(Long id);
}
