package code.catalog.domain.ports;

import code.catalog.domain.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room add(Room room);

    Optional<Room> readFirstAvailableRoom(LocalDateTime start, LocalDateTime end);

    List<Room> readAll();

    boolean existsByCustomId(String customId);
}
