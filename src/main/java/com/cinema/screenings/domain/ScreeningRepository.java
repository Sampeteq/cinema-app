package com.cinema.screenings.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    List<Screening> findByHallIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end);
}
