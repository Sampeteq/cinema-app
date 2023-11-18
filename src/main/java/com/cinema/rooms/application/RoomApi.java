package com.cinema.rooms.application;

import com.cinema.rooms.application.queries.GetFirstAvailableRoom;
import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.handlers.GetFirstAvailableRoomHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RoomApi {

    private final GetFirstAvailableRoomHandler getFirstAvailableRoomHandler;

    public RoomDto getFirstAvailableRoom(LocalDateTime from, LocalDateTime to) {
        return getFirstAvailableRoomHandler.handle(new GetFirstAvailableRoom(from, to));
    }
}
