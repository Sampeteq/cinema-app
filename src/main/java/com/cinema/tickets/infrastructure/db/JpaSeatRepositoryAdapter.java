package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSeatRepositoryAdapter implements SeatRepository {

    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public Seat add(Seat seat) {
        return jpaSeatRepository.save(seat);
    }

    @Override
    public Optional<Seat> getById(Long id) {
        return jpaSeatRepository.findById(id);
    }

    @Override
    public List<Seat> getAllByScreeningId(Long screeningId) {
        return jpaSeatRepository.findAllByScreeningId(screeningId);
    }
}

interface JpaSeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByScreeningId(Long screeningId);
}
