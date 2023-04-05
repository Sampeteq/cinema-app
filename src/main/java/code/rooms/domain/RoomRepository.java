package code.rooms.domain;

import code.rooms.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    boolean existsByNumber(int number);
}
