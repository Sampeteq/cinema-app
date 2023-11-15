package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.repositories.SeatRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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
    public Optional<Seat> getByScreeningIdRowNumberAndNumber(Long screeningId, int rowNumber, int number) {
        return jpaSeatRepository.findByScreeningIdAndRowNumberAndNumber(screeningId, rowNumber, number);
    }

    @Override
    public List<Seat> getAllByScreeningId(Long screeningId) {
        return jpaSeatRepository.findAllByScreeningId(screeningId);
    }
}

interface JpaSeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findByScreeningIdAndRowNumberAndNumber(Long screeningId, int rowNumber, int number);

    List<Seat> findAllByScreeningId(Long screeningId);
}
