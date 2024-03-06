package com.cinema.screenings.infrastructure;

import com.cinema.screenings.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

interface JpaScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Screening> findByHallIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end);
}
