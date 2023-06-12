package code.catalog.infrastructure.db;

import code.catalog.domain.Seat;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface SeatReadOnlyRepository extends Repository<Seat, Long> {
    Optional<Seat> getById(Long seatId);
}
