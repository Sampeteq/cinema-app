package com.cinema.screenings.infrastructure.db;

import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JpaScreeningRepositoryAdapter implements ScreeningRepository {

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
    public Optional<Screening> getById(UUID id) {
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
    public List<Screening> getCollisions(LocalDateTime date, LocalDateTime endDate, UUID hallId) {
        return jpaScreeningRepository
                .getCollisions(date, endDate, hallId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
