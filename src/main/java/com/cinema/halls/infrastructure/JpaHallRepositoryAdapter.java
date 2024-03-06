package com.cinema.halls.infrastructure;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaHallRepositoryAdapter implements HallRepository {

    private final JpaHallRepository hallRepository;

    @Override
    public Hall save(Hall hall) {
        return hallRepository.save(hall);
    }

    @Override
    public void delete(Hall hall) {
        hallRepository.delete(hall);
    }

    @Override
    public boolean existsById(Long id) {
        return hallRepository.existsById(id);
    }

    @Override
    public Optional<Hall> getById(Long id) {
        return hallRepository.findById(id);
    }

    @Override
    public List<Hall> getAllWithSeats() {
        return hallRepository.findAllWithSeats();
    }

    @Override
    public Optional<Hall> getByIdWithSeats(Long hallId) {
        return hallRepository.findByIdWithSeats(hallId);
    }
}
