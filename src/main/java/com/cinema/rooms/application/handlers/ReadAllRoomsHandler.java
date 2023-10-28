package com.cinema.rooms.application.handlers;

import com.cinema.rooms.application.dto.RoomDto;
import com.cinema.rooms.application.dto.RoomMapper;
import com.cinema.rooms.application.queries.ReadAllRooms;
import com.cinema.rooms.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadAllRoomsHandler {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDto> handle(ReadAllRooms query) {
        log.info("Query:{}", query);
        return roomRepository
                .readAll()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }
}
