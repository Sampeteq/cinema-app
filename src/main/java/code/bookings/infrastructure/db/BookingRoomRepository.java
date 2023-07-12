package code.bookings.infrastructure.db;

import code.bookings.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRoomRepository extends JpaRepository<Room, Long> {
}
