package code.bookings.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    boolean existsByNumber(int number);
}
