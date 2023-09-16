package com.cinema.bookings.domain.ports;

import com.cinema.bookings.domain.Screening;

import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    Optional<Screening> readByIdWithSeats(Long id);
}
