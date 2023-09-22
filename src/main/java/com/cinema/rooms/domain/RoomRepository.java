package com.cinema.rooms.domain;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room add(Room room);

    Optional<Room> readByCustomId(String customId);

    List<Room> readAll();

    Long count();

    boolean existsByCustomId(String customId);
}
