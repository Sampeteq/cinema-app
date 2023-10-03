package com.cinema.catalog.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    List<Screening> readAll();
    List<Screening> readByFilmTitle(String filmTitle);
    List<Screening> readByFilmCategory(FilmCategory category);
    List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to);
    List<Screening> readEndedWithRoom(LocalDateTime currentDate);
    Optional<Screening> readById(Long id);
    void delete(Screening screening);
}
