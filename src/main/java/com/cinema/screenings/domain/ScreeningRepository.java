package com.cinema.screenings.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {

    Screening save(Screening screening);

    void delete(Screening screening);

    Optional<Screening> getById(long id);

    List<Screening> getAll();

    List<Screening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Screening> getCollisions(LocalDateTime date, LocalDateTime endDate, long hallId);
}
