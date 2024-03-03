package com.cinema.screenings.infrastructure;

import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaScreeningRepositoryAdapter implements ScreeningRepository {

    interface JpaScreeningRepository extends JpaRepository<Screening, Long> {

        List<Screening> findScreeningsByDateBetween(LocalDateTime start, LocalDateTime end);

        List<Screening> findByHallIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end);
    }

    private final JpaScreeningRepository jpaScreeningRepository;

    @Override
    public Screening save(Screening screening) {
        return jpaScreeningRepository.save(screening);
    }

    @Override
    public void delete(Screening screening) {
        jpaScreeningRepository.delete(screening);
    }

    @Override
    public Optional<Screening> getById(Long id) {
        return jpaScreeningRepository.findById(id);
    }

    @Override
    public List<Screening> getAll() {
        return jpaScreeningRepository.findAll();
    }

    @Override
    public List<Screening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end) {
        return jpaScreeningRepository.findScreeningsByDateBetween(start, end);
    }

    @Override
    public List<Screening> getByHallIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end) {
        return jpaScreeningRepository.findByHallIdAndDateBetween(id, start, end);
    }
}
