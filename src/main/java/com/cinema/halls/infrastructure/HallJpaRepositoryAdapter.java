package com.cinema.halls.infrastructure;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class HallJpaRepositoryAdapter implements HallRepository {

    private final HallJpaRepository hallRepository;

    @Override
    public Hall save(Hall hall) {
        return hallRepository.save(hall);
    }

    @Override
    public void delete(Hall hall) {
        hallRepository.delete(hall);
    }

    @Override
    public Optional<Hall> getById(Long id) {
        return hallRepository.findById(id);
    }

    @Override
    public List<Hall> getAllWithSeats() {
        return hallRepository.findAllWithSeats();
    }
}
