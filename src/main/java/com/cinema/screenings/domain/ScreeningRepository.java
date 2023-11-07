package com.cinema.screenings.domain;

import com.cinema.screenings.application.queries.GetScreeningsBy;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    List<Screening> getAllBy(GetScreeningsBy query);
    List<Screening> getWithRoom();
    Optional<Screening> getById(Long id);
}
