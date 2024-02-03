package com.cinema.screenings.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    Optional<Screening> getById(Long id);
    Optional<Screening> getByIdWithTickets(Long id);
    List<Screening> getAll();
    List<Screening> getScreeningsByDate(LocalDate date);
    List<Screening> getScreeningCollisions(LocalDateTime startAt, LocalDateTime endAt, Long hallId);
}
