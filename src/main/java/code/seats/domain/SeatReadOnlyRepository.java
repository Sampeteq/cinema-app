package code.seats.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SeatReadOnlyRepository extends Repository<Seat, UUID> {
    Optional<Seat> getById(UUID seatId);
    Set<Seat> getByScreening_Id(UUID screeningId);
}
