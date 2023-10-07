package com.cinema.rooms.application.services;

import com.cinema.rooms.application.dto.RoomDto;
import com.cinema.rooms.application.dto.RoomMapper;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class RoomAvailableService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public RoomDto findFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return roomRepository
                .readAll()
                .stream()
                .filter(room -> room.hasNoOccupationOn(start, end))
                .findFirst()
                .map(roomMapper::toDto)
                .orElseThrow(RoomsNoAvailableException::new);
    }
}
