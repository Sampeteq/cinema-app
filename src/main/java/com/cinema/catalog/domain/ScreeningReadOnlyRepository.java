package com.cinema.catalog.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningReadOnlyRepository {

    Optional<Screening> readById(Long id);

    List<Screening> readAll();

    List<Screening> readByFilmTitle(String filmTitle);

    List<Screening> readByFilmCategory(FilmCategory filmCategory);

    List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to);

    List<Screening> readEnded();
}
