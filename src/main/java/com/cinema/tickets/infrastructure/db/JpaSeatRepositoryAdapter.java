package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.repositories.SeatRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
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
        return jpaSeatRepository.findByIdWithPessimisticLock(id);
    }

    @Override
    public List<Seat> getAllByScreeningId(Long screeningId) {
        return jpaSeatRepository.findAllByScreeningId(screeningId);
    }
}

interface JpaSeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT S FROM Seat S WHERE S.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findByIdWithPessimisticLock(Long id);

    List<Seat> findAllByScreeningId(Long screeningId);
}
