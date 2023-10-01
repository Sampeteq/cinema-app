package com.cinema.rooms.domain;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room add(Room room);

    Optional<Room> readById(String id);

    List<Room> readAll();

    Long count();

    boolean existsById(String id);
}
