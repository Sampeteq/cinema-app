package com.cinema.tickets.domain.ports;

import com.cinema.tickets.domain.Screening;

import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    Optional<Screening> readByIdWithSeats(Long id);
}
