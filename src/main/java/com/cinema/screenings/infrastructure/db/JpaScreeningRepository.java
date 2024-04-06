package com.cinema.screenings.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaScreeningRepository extends JpaRepository<JpaScreening, Long> {

    List<JpaScreening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
                select screening from JpaScreening screening where
                screening.hall.id = :hallId and
                (:date >= screening.date and :date <= screening.endDate) or
                (:date <= screening.date and :endDate >= screening.endDate) or
                (:date <= screening.date and :endDate <= screening.endDate)
            """)
    List<JpaScreening> getCollisions(LocalDateTime date, LocalDateTime endDate, Long hallId);
}

