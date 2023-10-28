package com.cinema.screenings.domain;

import com.cinema.screenings.application.queries.ReadScreeningsBy;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    void delete(Screening screening);
    List<Screening> readAllBy(ReadScreeningsBy query);
    List<Screening> readWithRoom();
    Optional<Screening> readById(Long id);
}
