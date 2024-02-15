package com.cinema.screenings.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Screening> findByHallIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end);
}
