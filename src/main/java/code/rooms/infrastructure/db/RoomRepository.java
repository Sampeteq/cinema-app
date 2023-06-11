package code.rooms.infrastructure.db;

import code.rooms.domain.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room add(Room room);

    Optional<Room> readById(Long roomId);

    List<Room> readAll();

    boolean existsByCustomId(String customId);
}
