package code.screenings.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SeatReadOnlyRepository extends Repository<Seat, Long> {
    Optional<Seat> getById(Long seatId);
    Set<Seat> getByScreening_Id(Long screeningId);
}
