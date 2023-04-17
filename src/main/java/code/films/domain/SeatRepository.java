package code.films.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    Set<Seat> getByScreening_Id(UUID screeningId);
}
