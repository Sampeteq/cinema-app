package com.cinema.rooms.application.services;

import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.rooms.application.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RoomFacade {

    private final RoomAvailableService roomAvailableService;
    private final RoomCreateService roomCreateService;

    public RoomDto findFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return roomAvailableService.findAvailableRoom(start, end);
    }

    public void createRoom(RoomCreateDto dto) {
        roomCreateService.createRoom(dto);
    }
}
