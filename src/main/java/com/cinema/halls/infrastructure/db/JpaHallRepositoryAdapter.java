package com.cinema.halls.infrastructure.db;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaHallRepositoryAdapter implements HallRepository {

    private final JpaHallRepository jpaHallRepository;

    @Override
    public Hall add(Hall hall) {
        return jpaHallRepository.save(hall);
    }

    @Override
    public Optional<Hall> getById(String id) {
        return jpaHallRepository.findById(id);
    }

    @Override
    public List<Hall> getAll() {
        return jpaHallRepository.findAll();
    }

    @Override
    public Long count() {
        return jpaHallRepository.count();
    }

    @Override
    public boolean existsById(String id) {
        return jpaHallRepository.existsById(id);
    }
}

interface JpaHallRepository extends JpaRepository<Hall, String> {
}
