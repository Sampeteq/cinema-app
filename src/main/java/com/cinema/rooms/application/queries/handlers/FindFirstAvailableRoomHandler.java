package com.cinema.rooms.application.queries.handlers;

import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.dto.RoomMapper;
import com.cinema.rooms.application.queries.FindFirstAvailableRoom;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindFirstAvailableRoomHandler {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public RoomDto handle(FindFirstAvailableRoom query) {
        log.info("Query:{}", query);
        return roomRepository
                .getAll()
                .stream()
                .filter(room -> room.hasNoOccupationOn(query.start(), query.end()))
                .findFirst()
                .map(roomMapper::toDto)
                .orElseThrow(RoomsNoAvailableException::new);
    }
}
