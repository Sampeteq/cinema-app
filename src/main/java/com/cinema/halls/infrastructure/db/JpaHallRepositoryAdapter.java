package com.cinema.halls.infrastructure.db;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JpaHallRepositoryAdapter implements HallRepository {

    private final JpaHallRepository jpaHallRepository;
    private final JpaHallMapper mapper;

    @Override
    public Hall save(Hall hall) {
        return mapper.toDomain(jpaHallRepository.save(mapper.toJpa(hall)));
    }

    @Override
    public void delete(Hall hall) {
        jpaHallRepository.delete(mapper.toJpa(hall));
    }

    @Override
    public Optional<Hall> getById(UUID id) {
        return jpaHallRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Hall> getAllWithSeats() {
        return jpaHallRepository
                .getAllWithSeats()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
