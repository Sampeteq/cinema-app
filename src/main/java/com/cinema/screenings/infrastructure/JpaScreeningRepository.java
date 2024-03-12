package com.cinema.screenings.infrastructure;

import com.cinema.screenings.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

interface JpaScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
                select screening from Screening screening where
                screening.hallId = :hallId and
                (:date >= screening.date and :date <= screening.endDate) or
                (:date <= screening.date and :endDate >= screening.endDate) or
                (:date <= screening.date and :endDate <= screening.endDate)
            """)
    List<Screening> getCollisions(LocalDateTime date, LocalDateTime endDate, Long hallId);
}
