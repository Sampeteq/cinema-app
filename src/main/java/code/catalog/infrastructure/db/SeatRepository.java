package code.catalog.infrastructure.db;

import code.catalog.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
