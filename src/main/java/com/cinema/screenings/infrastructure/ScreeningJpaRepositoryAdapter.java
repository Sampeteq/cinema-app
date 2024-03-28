package com.cinema.screenings.infrastructure;

import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class ScreeningJpaRepositoryAdapter implements ScreeningRepository {

    private final ScreeningJpaRepository screeningJpaRepository;

    @Override
    public Screening save(Screening screening) {
        return screeningJpaRepository.save(screening);
    }

    @Override
    public void delete(Screening screening) {
        screeningJpaRepository.delete(screening);
    }

    @Override
    public Optional<Screening> getById(Long id) {
        return screeningJpaRepository.findById(id);
    }

    @Override
    public List<Screening> getAll() {
        return screeningJpaRepository.findAll();
    }

    @Override
    public List<Screening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end) {
        return screeningJpaRepository.findScreeningsByDateBetween(start, end);
    }

    @Override
    public List<Screening> getCollisions(LocalDateTime date, LocalDateTime endDate, Long hallId) {
        return screeningJpaRepository.findCollisions(date, endDate, hallId);
    }
}
