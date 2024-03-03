package com.cinema.screenings.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {

    Screening save(Screening screening);

    void delete(Screening screening);

    Optional<Screening> getById(Long id);

    List<Screening> getAll();

    List<Screening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Screening> getByHallIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end);
}
