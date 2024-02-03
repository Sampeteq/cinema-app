package com.cinema.screenings.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    @Query("""
                from Screening screening
                left join fetch screening.tickets ticket
                left join fetch ticket.seat
            """)
    Optional<Screening> findByIdWithTickets(Long id);

    List<Screening> findScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    @Query(""" 
            from Screening s where
            ((:endAt >= s.date and :endAt <= s.endDate) or
            (:startAt >= s.date and :startAt <= s.endDate) or
            (:startAt <= s.endDate and :endAt >= s.endDate))
            and s.hall.id = :hallId
            """)
    List<Screening> findScreeningCollisions(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("hallId") Long hallId
    );
}
