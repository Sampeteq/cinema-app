package com.cinema.screenings.domain;

import com.cinema.screenings.application.dto.GetScreeningsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    Optional<Screening> getById(Long id);
    Optional<Screening> getByIdWithTickets(Long id);
    List<Screening> getAll(GetScreeningsDto dto);
    List<Screening> getScreeningCollisions(LocalDateTime startAt, LocalDateTime endAt, Long hallId);
}
