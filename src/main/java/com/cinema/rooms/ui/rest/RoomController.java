package com.cinema.rooms.ui.rest;

import com.cinema.rooms.application.queries.GetAllRooms;
import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.handlers.GetAllRoomsHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final GetAllRoomsHandler getAllRoomsHandler;

    @GetMapping
    @SecurityRequirement(name = "basic")
    public List<RoomDto> getAllRooms() {
        var query = new GetAllRooms();
        log.info("Query:{}", query);
        return getAllRoomsHandler.handle(query);
    }
}
