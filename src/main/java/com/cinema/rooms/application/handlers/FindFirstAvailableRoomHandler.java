package com.cinema.rooms.application.handlers;

import com.cinema.rooms.application.dto.RoomDto;
import com.cinema.rooms.application.dto.RoomMapper;
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
    public RoomDto findFirstAvailableRoom(FindFirstAvailableRoom query) {
        log.info("Query:{}", query);
        return roomRepository
                .readAll()
                .stream()
                .filter(room -> room.hasNoOccupationOn(query.start(), query.end()))
                .findFirst()
                .map(roomMapper::toDto)
                .orElseThrow(RoomsNoAvailableException::new);
    }
}
