package code.films.infrastructure.db;

import code.films.domain.Seat;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface SeatReadOnlyRepository extends Repository<Seat, Long> {
    Optional<Seat> getById(Long seatId);
}
