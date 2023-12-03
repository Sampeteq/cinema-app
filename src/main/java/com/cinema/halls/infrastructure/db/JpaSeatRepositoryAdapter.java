package com.cinema.halls.infrastructure.db;

import com.cinema.halls.domain.Seat;
import com.cinema.halls.domain.SeatRepository;
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
    public Optional<Seat> getById(Long seatId) {
        return jpaSeatRepository.findById(seatId);
    }

    @Override
    public Optional<Seat> getByHallIdAndPosition(Long hallId, int rowNumber, int number) {
        return jpaSeatRepository.findByHallIdAndRowNumberAndNumber(hallId, rowNumber, number);
    }

    @Override
    public List<Seat> getAllByHallId(Long hallId) {
        return jpaSeatRepository.findAllByHallId(hallId);
    }
}

interface JpaSeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByHallIdAndRowNumberAndNumber(Long hallId, int rowNumber, int number);

    List<Seat> findAllByHallId(Long hallId);
}
