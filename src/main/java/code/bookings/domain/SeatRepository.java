package code.bookings.domain;

import code.bookings.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    Set<Seat> findByScreening_Id(UUID screeningId);
}
