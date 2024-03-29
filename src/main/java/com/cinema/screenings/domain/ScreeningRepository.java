package com.cinema.screenings.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
                select screening from Screening screening where
                screening.hall.id = :hallId and
                (:date >= screening.date and :date <= screening.endDate) or
                (:date <= screening.date and :endDate >= screening.endDate) or
                (:date <= screening.date and :endDate <= screening.endDate)
            """)
    List<Screening> getCollisions(LocalDateTime date, LocalDateTime endDate, Long hallId);
}
