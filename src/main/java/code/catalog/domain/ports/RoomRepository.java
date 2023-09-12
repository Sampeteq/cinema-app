package code.catalog.domain.ports;

import code.catalog.domain.Room;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RoomRepository {
    Room add(Room room);

    Optional<Room> readFirstAvailableRoom(LocalDateTime start, LocalDateTime end);

    Long count();

    boolean existsByCustomId(String customId);
}
