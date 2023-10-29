package com.cinema.rooms.application.rest;

import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.handlers.ReadAllRoomsHandler;
import com.cinema.rooms.application.queries.ReadAllRooms;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Slf4j
public class RoomController {

    private final ReadAllRoomsHandler readAllRoomsHandler;

    @GetMapping
    @SecurityRequirement(name = "basic")
    public List<RoomDto> readAllRooms() {
        var query = new ReadAllRooms();
        log.info("Query:{}", query);
        return readAllRoomsHandler.handle(query);
    }
}
