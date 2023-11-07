package com.cinema.rooms.domain;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room add(Room room);
    Optional<Room> getById(String id);
    List<Room> getAll();
    Long count();
    boolean existsById(String id);
}
