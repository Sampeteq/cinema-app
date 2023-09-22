package com.cinema.rooms.application.dto;

import com.cinema.rooms.domain.Room;
import org.mapstruct.Mapper;

@Mapper
public interface RoomMapper {

    RoomDto toDto(Room room);
}
