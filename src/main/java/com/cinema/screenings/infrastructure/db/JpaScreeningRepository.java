package com.cinema.screenings.infrastructure.db;

import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface JpaScreeningRepository extends JpaRepository<JpaScreening, Long> {

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

@Repository
@RequiredArgsConstructor
class JpaScreeningRepositoryAdapter implements ScreeningRepository {

    private final JpaScreeningRepository jpaScreeningRepository;
    private final JpaScreeningMapper mapper;

    @Override
    public Screening save(Screening screening) {
        return mapper.toDomain(jpaScreeningRepository.save(mapper.toJpa(screening)));
    }

    @Override
    public void delete(Screening screening) {
        jpaScreeningRepository.delete(mapper.toJpa(screening));
    }

    @Override
    public Optional<Screening> getById(Long id) {
        return jpaScreeningRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Screening> getAll() {
        return jpaScreeningRepository
                .findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Screening> getScreeningsByDateBetween(LocalDateTime start, LocalDateTime end) {
        return jpaScreeningRepository
                .getScreeningsByDateBetween(start, end)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Screening> getCollisions(LocalDateTime date, LocalDateTime endDate, Long hallId) {
        return jpaScreeningRepository
                .getCollisions(date, endDate, hallId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
