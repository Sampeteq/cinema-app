package com.cinema.catalog.domain.ports;

import com.cinema.catalog.application.dto.ScreeningDetailsDto;
import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.Screening;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningReadOnlyRepository {
    List<Screening> readAll();

    List<Screening> readByFilmTitle(String filmTitle);

    List<Screening> readByFilmCategory(FilmCategory filmCategory);

    List<Screening> readByDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Screening s where s.endDate < CURRENT_DATE")
    List<Screening> readEnded();

    Optional<ScreeningDetailsDto> readDetailsBySeatId(Long seatId);
}
