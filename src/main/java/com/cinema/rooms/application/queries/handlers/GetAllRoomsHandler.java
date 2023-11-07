package com.cinema.rooms.application.queries.handlers;

import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.dto.RoomMapper;
import com.cinema.rooms.application.queries.GetAllRooms;
import com.cinema.rooms.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllRoomsHandler {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDto> handle(GetAllRooms query) {
        log.info("Query:{}", query);
        return roomRepository
                .getAll()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }
}
