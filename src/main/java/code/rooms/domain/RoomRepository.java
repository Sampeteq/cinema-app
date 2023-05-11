package code.rooms.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    Room add(Room room);

    Optional<Room> readById(Long roomId);

    List<Room> readAll();

    boolean existsByCustomId(String customId);
}
