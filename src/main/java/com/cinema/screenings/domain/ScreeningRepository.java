package com.cinema.screenings.domain;

import com.cinema.screenings.application.queries.GetScreenings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    Optional<Screening> getById(Long id);
    List<Screening> getAll(GetScreenings query);
    List<Screening> getScreeningCollisions(LocalDateTime startAt, LocalDateTime endAt, Long hallId);
}
