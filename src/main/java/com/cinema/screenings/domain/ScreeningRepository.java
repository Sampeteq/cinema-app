package com.cinema.screenings.domain;

import com.cinema.screenings.application.queries.GetScreenings;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    List<Screening> getAll(GetScreenings query);
    List<Screening> getWithRoom();
    Optional<Screening> getById(Long id);
}
